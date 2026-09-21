package com.luxixi.backend.common;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/** A bounded fixed-window limiter for one application instance. */
public final class InMemoryRateLimiter {
    private static final long WINDOW_MILLIS = 60_000L;

    private final int perIpLimit;
    private final int globalLimit;
    private final Semaphore concurrentRequests;
    private final Clock clock;
    private final Map<String, IpWindow> ipWindows = new ConcurrentHashMap<>();
    private final Object stateLock = new Object();
    private long globalWindowStart;
    private int globalCount;
    private long lastCleanupAt;

    public InMemoryRateLimiter(int perIpLimit, int globalLimit, int maxConcurrent, Clock clock) {
        if (perIpLimit < 1 || globalLimit < 1 || maxConcurrent < 1) {
            throw new IllegalArgumentException("限流配置必须大于 0");
        }
        this.perIpLimit = perIpLimit;
        this.globalLimit = globalLimit;
        this.concurrentRequests = new Semaphore(maxConcurrent);
        this.clock = clock;
        this.globalWindowStart = clock.millis();
        this.lastCleanupAt = this.globalWindowStart;
    }

    public boolean tryAcquire(String clientIp) {
        if (!concurrentRequests.tryAcquire()) {
            return false;
        }

        long now = clock.millis();
        String normalizedIp = clientIp == null || clientIp.isBlank() ? "unknown" : clientIp;
        synchronized (stateLock) {
            resetGlobalWindowIfNeeded(now);
            if (globalCount >= globalLimit) {
                concurrentRequests.release();
                return false;
            }

            IpWindow ipWindow = ipWindows.computeIfAbsent(normalizedIp, ignored -> new IpWindow(now));
            if (now - ipWindow.windowStart >= WINDOW_MILLIS) {
                ipWindow.windowStart = now;
                ipWindow.count = 0;
            }
            if (ipWindow.count >= perIpLimit) {
                concurrentRequests.release();
                return false;
            }

            ipWindow.count++;
            globalCount++;
            cleanupExpiredWindowsIfNeeded(now);
            return true;
        }
    }

    public void release() {
        concurrentRequests.release();
    }

    private void resetGlobalWindowIfNeeded(long now) {
        if (now - globalWindowStart >= WINDOW_MILLIS) {
            globalWindowStart = now;
            globalCount = 0;
        }
    }

    private void cleanupExpiredWindowsIfNeeded(long now) {
        if (now - lastCleanupAt < WINDOW_MILLIS) {
            return;
        }
        ipWindows.entrySet().removeIf(entry -> now - entry.getValue().windowStart >= WINDOW_MILLIS);
        lastCleanupAt = now;
    }

    private static final class IpWindow {
        private long windowStart;
        private int count;

        private IpWindow(long windowStart) {
            this.windowStart = windowStart;
        }
    }
}

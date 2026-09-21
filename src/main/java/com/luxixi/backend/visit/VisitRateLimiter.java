package com.luxixi.backend.visit;

import com.luxixi.backend.common.InMemoryRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class VisitRateLimiter {
    private final InMemoryRateLimiter delegate;

    public VisitRateLimiter(
            @Value("${app.security.visit-rate-limit.per-ip-per-minute:30}") int perIpLimit,
            @Value("${app.security.visit-rate-limit.global-per-minute:600}") int globalLimit,
            @Value("${app.security.visit-rate-limit.max-concurrent:8}") int maxConcurrent,
            Clock clock) {
        this.delegate = new InMemoryRateLimiter(perIpLimit, globalLimit, maxConcurrent, clock);
    }

    public boolean tryAcquire(String clientIp) {
        return delegate.tryAcquire(clientIp);
    }

    public void release() {
        delegate.release();
    }
}

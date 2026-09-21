package com.luxixi.backend.contact;

import com.luxixi.backend.common.InMemoryRateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class ContactRateLimiter {
    private final InMemoryRateLimiter delegate;

    public ContactRateLimiter(
            @Value("${app.security.rate-limit.per-ip-per-minute:5}") int perIpLimit,
            @Value("${app.security.rate-limit.global-per-minute:120}") int globalLimit,
            @Value("${app.security.rate-limit.max-concurrent:16}") int maxConcurrent,
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

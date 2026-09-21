package com.luxixi.backend.visit;

import com.luxixi.backend.common.RateLimitResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class VisitRequestRateLimitFilter extends OncePerRequestFilter {
    private static final String VISIT_PATH = "/api/v1/visit-events";
    private final VisitRateLimiter rateLimiter;

    public VisitRequestRateLimitFilter(VisitRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (!"POST".equalsIgnoreCase(request.getMethod())
                || !VISIT_PATH.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!rateLimiter.tryAcquire(request.getRemoteAddr())) {
            RateLimitResponseWriter.write(response);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            rateLimiter.release();
        }
    }
}

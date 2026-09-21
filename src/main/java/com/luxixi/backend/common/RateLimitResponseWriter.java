package com.luxixi.backend.common;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class RateLimitResponseWriter {
    private RateLimitResponseWriter() {
    }

    public static void write(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setHeader("Retry-After", "60");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String body = "{\"code\":\"RATE_LIMITED\",\"message\":\"请求过于频繁，请稍后再试\"}";
        response.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);
        response.getWriter().write(body);
    }
}

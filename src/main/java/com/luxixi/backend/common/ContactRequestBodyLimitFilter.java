package com.luxixi.backend.common;

import com.luxixi.backend.contact.ContactRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Bounds the contact endpoint body before Jackson parses it. Reading at most
 * maxBytes + 1 bytes also protects chunked requests that omit Content-Length.
 */
@Component
public class ContactRequestBodyLimitFilter extends OncePerRequestFilter {
    private static final String CONTACT_PATH = "/api/v1/contact-messages";

    private final int maxBytes;
    private final ContactRateLimiter rateLimiter;

    public ContactRequestBodyLimitFilter(
            @Value("${app.security.request.max-body-bytes:16384}") int maxBytes,
            ContactRateLimiter rateLimiter) {
        if (maxBytes < 1) {
            throw new IllegalArgumentException("请求体大小配置必须大于 0");
        }
        this.maxBytes = maxBytes;
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, jakarta.servlet.ServletException {
        if (!"POST".equalsIgnoreCase(request.getMethod())
                || !CONTACT_PATH.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!rateLimiter.tryAcquire(request.getRemoteAddr())) {
            RateLimitResponseWriter.write(response);
            return;
        }

        try {
            long declaredLength = request.getContentLengthLong();
            if (declaredLength > maxBytes) {
                writeTooLargeResponse(response);
                return;
            }

            byte[] body = request.getInputStream().readNBytes(maxBytes + 1);
            if (body.length > maxBytes) {
                writeTooLargeResponse(response);
                return;
            }

            filterChain.doFilter(new CachedBodyRequest(request, body), response);
        } finally {
            rateLimiter.release();
        }
    }

    private void writeTooLargeResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String body = "{\"code\":\"REQUEST_TOO_LARGE\",\"message\":\"请求内容过大，请缩短留言后重试\"}";
        response.setContentLength(body.getBytes(StandardCharsets.UTF_8).length);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(body);
        }
    }

    private static final class CachedBodyRequest extends HttpServletRequestWrapper {
        private final byte[] body;

        private CachedBodyRequest(HttpServletRequest request, byte[] body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream input = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return input.read();
                }

                @Override
                public int read(byte[] bytes, int offset, int length) {
                    return input.read(bytes, offset, length);
                }

                @Override
                public boolean isFinished() {
                    return input.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // Synchronous MVC requests do not use non-blocking reads.
                }
            };
        }

        @Override
        public java.io.BufferedReader getReader() throws IOException {
            String encoding = getCharacterEncoding();
            return new java.io.BufferedReader(new java.io.InputStreamReader(
                    getInputStream(), encoding == null ? StandardCharsets.UTF_8 : java.nio.charset.Charset.forName(encoding)));
        }
    }
}

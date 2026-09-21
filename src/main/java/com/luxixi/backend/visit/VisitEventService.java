package com.luxixi.backend.visit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;

@Service
public class VisitEventService {
    private final VisitEventRepository repository;
    private final GeoRegionResolver geoRegionResolver;
    private final boolean trustForwardedHeaders;
    private final boolean geoEnabled;

    public VisitEventService(
            VisitEventRepository repository,
            GeoRegionResolver geoRegionResolver,
            @Value("${app.security.trust-forwarded-headers:false}") boolean trustForwardedHeaders,
            @Value("${app.geo.enabled:true}") boolean geoEnabled) {
        this.repository = repository;
        this.geoRegionResolver = geoRegionResolver;
        this.trustForwardedHeaders = trustForwardedHeaders;
        this.geoEnabled = geoEnabled;
    }

    @Transactional
    public void create(VisitEventRequest request, HttpServletRequest httpRequest) {
        String userAgent = value(httpRequest.getHeader("User-Agent"), 4000);
        VisitEvent event = new VisitEvent();
        event.setPath(request.path());
        String clientIp = resolveClientIp(httpRequest);
        event.setClientIp(clientIp);
        event.setReferrer(value(httpRequest.getHeader("Referer"), 2000));
        event.setUserAgent(userAgent);
        event.setBrowserName(parseBrowser(userAgent));
        event.setDeviceType(parseDevice(userAgent));
        event.setGeoRegion(geoEnabled ? geoRegionResolver.resolve(clientIp) : null);
        repository.save(event);
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (trustForwardedHeaders) {
            String forwarded = request.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                String candidate = forwarded.split(",")[0].trim();
                if (isIpLiteral(candidate)) {
                    return candidate;
                }
            }
        }
        return value(request.getRemoteAddr(), 64);
    }

    private boolean isIpLiteral(String value) {
        try {
            boolean ipv4 = value.matches("(?:\\d{1,3}\\.){3}\\d{1,3}");
            boolean ipv6 = value.contains(":") && value.matches("[0-9A-Fa-f:.]+");
            if (!ipv4 && !ipv6) {
                return false;
            }
            InetAddress.getByName(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
    private String value(String value, int max){return value == null ? null : value.substring(0, Math.min(value.length(), max));}
    private String parseBrowser(String ua){
        if (ua == null) return "Unknown"; if (ua.contains("Edg/")) return "Edge"; if (ua.contains("Chrome/")) return "Chrome";
        if (ua.contains("Firefox/")) return "Firefox"; if (ua.contains("Safari/")) return "Safari"; return "Unknown";
    }
    private String parseDevice(String ua){
        if (ua == null) return "unknown"; if (ua.contains("Mobile") || ua.contains("Android")) return "mobile";
        if (ua.contains("Tablet") || ua.contains("iPad")) return "tablet"; return "desktop";
    }
}

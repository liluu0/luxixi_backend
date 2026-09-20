package com.luxixi.backend.visit;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VisitEventService {
    private final VisitEventRepository repository;
    private final GeoRegionResolver geoRegionResolver;
    public VisitEventService(VisitEventRepository repository, GeoRegionResolver geoRegionResolver){this.repository=repository;this.geoRegionResolver=geoRegionResolver;}

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
        event.setGeoRegion(geoRegionResolver.resolve(clientIp));
        repository.save(event);
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",")[0].trim();
        return request.getRemoteAddr();
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

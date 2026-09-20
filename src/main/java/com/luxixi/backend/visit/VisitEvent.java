package com.luxixi.backend.visit;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "visit_event")
public class VisitEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "visited_at", nullable = false, updatable = false)
    private OffsetDateTime visitedAt;
    @Column(name = "client_ip", length = 64) private String clientIp;
    @Column(nullable = false, length = 255) private String path;
    @Column(columnDefinition = "text") private String referrer;
    @Column(name = "user_agent", columnDefinition = "text") private String userAgent;
    @Column(name = "browser_name", length = 64) private String browserName;
    @Column(name = "device_type", length = 32) private String deviceType;
    @Column(name = "geo_region", length = 128) private String geoRegion;
    @PrePersist void onCreate() { visitedAt = OffsetDateTime.now(); }
    public void setClientIp(String v){clientIp=v;} public void setPath(String v){path=v;} public void setReferrer(String v){referrer=v;}
    public void setUserAgent(String v){userAgent=v;} public void setBrowserName(String v){browserName=v;} public void setDeviceType(String v){deviceType=v;}
    public void setGeoRegion(String v){geoRegion=v;} public Long getId(){return id;} public OffsetDateTime getVisitedAt(){return visitedAt;}
}

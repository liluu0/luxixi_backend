package com.luxixi.backend.visit;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.net.InetAddress;

@Component
public class GeoRegionResolver {
    private final RestClient client = RestClient.builder()
            .baseUrl("https://ipapi.co")
            .requestFactory(requestFactory())
            .build();

    private static SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1500);
        factory.setReadTimeout(1500);
        return factory;
    }

    public String resolve(String ip) {
        if (ip == null || ip.isBlank() || isPrivate(ip)) return null;
        try {
            GeoResponse response = client.get().uri("/{ip}/json/", ip)
                    .retrieve().body(GeoResponse.class);
            return response == null ? null : trim(response.region);
        } catch (RuntimeException ignored) {
            return null;
        }
    }

    private boolean isPrivate(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isAnyLocalAddress() || address.isLoopbackAddress()
                    || address.isLinkLocalAddress() || address.isSiteLocalAddress();
        } catch (Exception ignored) {
            return true;
        }
    }

    private String trim(String value) {
        return value == null || value.isBlank() ? null : value.substring(0, Math.min(value.length(), 128));
    }

    private static class GeoResponse {
        public String region;
    }
}

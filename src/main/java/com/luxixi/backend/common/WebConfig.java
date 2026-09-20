package com.luxixi.backend.common;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.Configuration; import org.springframework.web.servlet.config.annotation.*;
@Configuration public class WebConfig implements WebMvcConfigurer {
 @Value("${app.cors.allowed-origins}") private String origins;
 @Override public void addCorsMappings(CorsRegistry registry){registry.addMapping("/api/**").allowedOrigins(origins.split(",")).allowedMethods("POST","OPTIONS").allowedHeaders("*");}
}

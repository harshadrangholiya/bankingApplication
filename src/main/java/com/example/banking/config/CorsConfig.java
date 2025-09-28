package com.example.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // allow cookies / JWT headers
        config.addAllowedOriginPattern("*"); // allow any origin (works with allowCredentials)
        config.addAllowedHeader("*");        // allow all headers
        config.addAllowedMethod("*");        // allow GET, POST, PUT, DELETE, etc.

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

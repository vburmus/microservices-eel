package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

import static com.epam.esm.config.Constants.*;

@Configuration
public class CustomCorsConfiguration extends CorsConfiguration {
    @Value("${allowed.origin}")
    private String allowedOrigin;

    @Bean
    public CorsWebFilter corsWebFilter() {
        final CustomCorsConfiguration corsConfig = new CustomCorsConfiguration();
        corsConfig.setAllowedOrigins(Collections.singletonList(allowedOrigin));
        corsConfig.setAllowedMethods(ALLOWED_METHODS);
        corsConfig.setAllowedHeaders(ALLOWED_HEADERS);
        corsConfig.addExposedHeader(AUTHORIZATION);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

}
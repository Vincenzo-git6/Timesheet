package com.axcent.TimeSheet.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig
{
    // Filtro per CORS
    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Consenti credenziali (se necessario)
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200")); // Permetti l'accesso da localhost:4200
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Metodi consentiti
        config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Authorization")); // Headers esposti

        source.registerCorsConfiguration("/**", config); // Applica questa configurazione a tutte le rotte
        return new CorsFilter(source);
    }
}

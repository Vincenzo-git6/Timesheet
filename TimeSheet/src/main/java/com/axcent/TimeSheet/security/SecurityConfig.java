package com.axcent.TimeSheet.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;

// Classe di configurazione per la sicurezza dell'applicazione (Spring Security)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CorsFilter corsFilter;

    // Definisce il bean che cripta le password usando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    // Recupera l'AuthenticationManager dal contesto di Spring Security
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception
    {
        return authConfig.getAuthenticationManager();
    }

    // Configura la catena di filtri di sicurezza: cosa è protetto, chi può accedere, e in che modo
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable())  // Disabilita CSRF poiché si usa JWT
                .exceptionHandling(exception -> exception  // Gestisce errori di autenticazione con un gestore personalizzato
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Specifica che la sessione è stateless (ogni richiesta si autentica da sola con JWT)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()     // accesso libero per login/registrazione
                        .requestMatchers("/api/public/**").permitAll()   // risorse pubbliche accessibili a tutti
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN") // accesso solo admin
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated());                         // qualsiasi altra richiesta → autenticazione obbligatoria

        // Aggiunge il filtro CORS prima del filtro JWT
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

        // Aggiunge il filtro JWT per elaborare il token nella richiesta
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


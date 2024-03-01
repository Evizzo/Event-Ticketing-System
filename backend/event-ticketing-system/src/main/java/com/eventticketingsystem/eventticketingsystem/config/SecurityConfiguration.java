package com.eventticketingsystem.eventticketingsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.eventticketingsystem.eventticketingsystem.config.Permission.*;
import static com.eventticketingsystem.eventticketingsystem.entities.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                    req
                            .requestMatchers(GET,"/event").permitAll()
                            .requestMatchers(GET,"/search").permitAll()
                            .requestMatchers(GET,"/event/{id}").permitAll()
                            .requestMatchers(GET,"/event/popular").permitAll()
                            .requestMatchers(GET,"/comment/{eventId}").permitAll()
                            .requestMatchers(GET,"/currency-converter").permitAll()
                            .requestMatchers("/admin/**").hasRole(ADMIN.name())
                            .requestMatchers(GET, "/admin/**").hasAuthority(ADMIN_READ.name())
                            .requestMatchers(POST, "/admin/**").hasAuthority(ADMIN_CREATE.name())
                            .requestMatchers(PUT, "/admin/**").hasAuthority(ADMIN_UPDATE.name())
                            .requestMatchers(DELETE, "/admin/**").hasAuthority(ADMIN_DELETE.name())

                            .requestMatchers(WHITE_LIST_URL).permitAll()
                    .anyRequest()
                    .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                                logout
                                        .logoutUrl("/api/v1/auth/logout")
                                        .addLogoutHandler(logoutHandler)
                                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                        );

        return http.build();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            public void addCorsMappings(final CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("*");
            }
        };
    }
}

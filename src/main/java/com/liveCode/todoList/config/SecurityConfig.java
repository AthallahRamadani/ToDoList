package com.liveCode.todoList.config;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.security.JwtAuthenticationFilter;
import com.liveCode.todoList.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserSecurity userSecurity;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/admin/**").access(adminSuperadminAuthorizationManager())
                        .requestMatchers(HttpMethod.POST, "/api/admin/users/super-admin").access(adminSuperadminAuthorizationManager())
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").access(adminSuperadminAuthorizationManager())
                        .requestMatchers(HttpMethod.PUT, "/api/admin/users/{id}/role").access(adminSuperadminAuthorizationManager())

                        .requestMatchers(HttpMethod.POST, "/api/todos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/todos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/todos/{id}").access(userAuthorizationManager())
                        .requestMatchers(HttpMethod.PUT, "/api/todos/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/todos/{id}/status").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/todos/{id}").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthorizationManager<RequestAuthorizationContext> adminSuperadminAuthorizationManager() {
        AuthorizationManager<RequestAuthorizationContext> adminAuth = AuthorityAuthorizationManager.hasAuthority("ROLE_ADMIN");
        AuthorizationManager<RequestAuthorizationContext> superadmin = AuthorityAuthorizationManager.hasAuthority("ROLE_SUPER_ADMIN");
        return (authentication, context) -> {
            if (superadmin.check(authentication, context).isGranted()) {
                return new AuthorizationDecision(true);
            } else if (adminAuth.check(authentication, context).isGranted()) {
                return new AuthorizationDecision(true);
            } else {
                return new AuthorizationDecision(false);
            }
        };
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> userAuthorizationManager() {
        AuthorizationManager<RequestAuthorizationContext> adminAuth = AuthorityAuthorizationManager.hasAuthority("ROLE_ADMIN");
        AuthorizationManager<RequestAuthorizationContext> userAuth = AuthorityAuthorizationManager.hasAuthority("ROLE_USER");
        AuthorizationManager<RequestAuthorizationContext> superadmin = AuthorityAuthorizationManager.hasAuthority("ROLE_SUPER_ADMIN");

        return (authentication, context) -> {
            if (superadmin.check(authentication, context).isGranted()) {
                return new AuthorizationDecision(true);
            } else {
                try {
                    Long userId = Long.parseLong(context.getVariables().get("id"));

                    if (userAuth.check(authentication, context).isGranted() && userSecurity.hasUserId(authentication.get(), userId)) {
                        return new AuthorizationDecision(true);
                    }

                    if (adminAuth.check(authentication, context).isGranted() && userSecurity.hasUserId(authentication.get(), userId)) {
                        return new AuthorizationDecision(true);
                    }

                    return new AuthorizationDecision(false);
                } catch (NumberFormatException e) {
                    return new AuthorizationDecision(false);
                }
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Add your frontend URL here
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

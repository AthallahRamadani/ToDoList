package com.liveCode.todoList.config;

import com.liveCode.todoList.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserRepository repository;

    @Bean
    public UserDetailsService userDetailsService() {
//      mengambil user dari database berdasarkan email/username
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
//      DoAuthenticationProvider berfungsi untuk melakukan autentikasi user berdasarkan user credential yang disimpan pada UserDetailsService
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }

    @Bean
    public ExecutorService executorService() {return Executors.newFixedThreadPool(5); }
}
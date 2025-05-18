package com.ooad.code.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ooad.code.service.CustomUserDetailService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //cho user
    @Order(1)
    @Bean
    SecurityFilterChain UserSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .securityMatcher("/user/**", "/login", "/register", "/logout", "/login/**", "/api/**")
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/register", "/login/**").permitAll()
            .requestMatchers("/user/**").authenticated()
            .anyRequest().authenticated()
        ).formLogin(login->login 
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/user/list",true)
        ).logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
        )
        .userDetailsService(customUserDetailService)
        .build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                        .debug(false)
                        .ignoring()
                        .requestMatchers("/assets/**", "/terms-of-service", "/privacy-policy");
    }
}

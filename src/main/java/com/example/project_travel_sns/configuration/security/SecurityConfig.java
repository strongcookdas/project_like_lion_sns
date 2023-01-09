package com.example.project_travel_sns.configuration.security;

import com.example.project_travel_sns.configuration.security.exception.AuthenticationManager;
import com.example.project_travel_sns.configuration.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final JwtFilter jwtFilter;
    private final String[] PERMIT_URL = {
            "/api/v1/hello",
            "/api/v1/users/join",
            "/api/v1/users/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL).permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/api/v1/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/api/v1/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/posts/my").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/alarms").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationManager)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

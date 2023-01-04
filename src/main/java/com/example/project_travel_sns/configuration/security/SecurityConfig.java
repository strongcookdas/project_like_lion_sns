package com.example.project_travel_sns.configuration.security;

import com.example.project_travel_sns.configuration.security.exception.AccessDeniedManager;
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

    private final AccessDeniedManager accessDeniedManager;
    private final AuthenticationManager authenticationManager;
    private final JwtFilter jwtFilter;
    private String[] PERMIT_URL = {
            "/api/v1/hello",
            "/api/v1/users/**"
    };

    private String[] NOT_PERMIT_URL = {
            "/api/v1/posts/my"
    };
    private String[] SWAGGER = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers(PERMIT_URL).permitAll()
                .antMatchers(SWAGGER).permitAll()
                .antMatchers(NOT_PERMIT_URL).authenticated()
                .antMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
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

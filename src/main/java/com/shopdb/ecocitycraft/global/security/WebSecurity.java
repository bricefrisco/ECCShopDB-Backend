package com.shopdb.ecocitycraft.global.security;

import com.shopdb.ecocitycraft.global.filters.RequestFilter;
import com.shopdb.ecocitycraft.security.config.JWTConfiguration;
import com.shopdb.ecocitycraft.security.database.repositories.UserRepository;
import com.shopdb.ecocitycraft.security.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Component
@EnableWebSecurity
@EnableConfigurationProperties(JWTConfiguration.class)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final JWTConfiguration jwtConfig;

    @Autowired
    private AuthenticationService authenticationService;

    public WebSecurity(JWTConfiguration jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        RequestFilter authenticationFilter = new RequestFilter(authenticationManager(), jwtConfig, authenticationService);

        http.cors().and().csrf().disable()
                .antMatcher("/**").addFilter(authenticationFilter);
    }
}

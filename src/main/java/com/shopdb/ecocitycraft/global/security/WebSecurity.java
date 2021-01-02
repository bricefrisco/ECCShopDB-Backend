package com.shopdb.ecocitycraft.global.security;

import com.shopdb.ecocitycraft.security.config.JWTConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@EnableConfigurationProperties(JWTConfiguration.class)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final JWTConfiguration jwtConfig;

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
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(authenticationManager(), jwtConfig);

        http.cors().and().csrf().disable()
                .antMatcher("/**").addFilter(authenticationFilter);


//        // Authentication
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/authentication").permitAll();
//
//        // Chest shops
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/chest-shops").permitAll();
//
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/chest-shops").authenticated()
//                .and()
//                .addFilter(authenticationFilter);
//
//        // Regions
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/regions").permitAll()
//                .antMatchers(HttpMethod.GET, "/regions/**").permitAll();
//
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/regions").authenticated()
//                .antMatchers(HttpMethod.PUT, "/regions").authenticated()
//                .antMatchers(HttpMethod.PUT, "/regions/**").authenticated();
//
//        // Players
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/players**").permitAll();
//
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/players").authenticated()
//                .antMatchers(HttpMethod.PUT, "/players").authenticated();
    }
}

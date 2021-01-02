package com.shopdb.ecocitycraft.global.security;

import com.shopdb.ecocitycraft.security.config.JWTConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@EnableConfigurationProperties(JWTConfiguration.class)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final JWTConfiguration jwtConfig;

    @Value("${server.servlet.context-path}")
    private String contextPath;

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
                .antMatcher("/**");

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/chest-shops").authenticated()
                .and()
                .addFilter(authenticationFilter);

//                .antMatchers(HttpMethod.PUT, contextPath + "/chest-shops").authenticated()
//                .antMatchers(HttpMethod.DELETE, contextPath + "/chest-shops").authenticated()
//                .and()
//                .addFilter(authenticationFilter)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//        http.cors().and().csrf().disable().authorizeRequests()
//                .antMatchers(contextPath + "/authorization/*")
//                .antMatchers(contextPath + "/")
//                // .antMatchers("/notes/*").authenticated()
//                .and()
//                .addFilter(authenticationFilter)
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}

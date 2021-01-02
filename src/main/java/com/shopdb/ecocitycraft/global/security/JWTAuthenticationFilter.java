package com.shopdb.ecocitycraft.global.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.shopdb.ecocitycraft.global.exceptions.ExceptionResponse;
import com.shopdb.ecocitycraft.security.config.JWTConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private final JWTConfiguration jwtConfig;

    public JWTAuthenticationFilter(AuthenticationManager authManager, JWTConfiguration jwtConfig) {
        super(authManager);
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Filter being called.");
        String token = request.getHeader(HEADER_STRING);

        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            LOGGER.info("Token is null");
            chain.doFilter(request, response);
            return;
        }

        String user;
        
        try {
            user = JWT.require(Algorithm.HMAC512(jwtConfig.getSecret().getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .getSubject();
        } catch (JWTVerificationException ex) {
            response.getWriter().write(getExceptionResponse(ex.getMessage(), response));
            return;
        }
        
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(getExceptionResponse("JWT has no subject", response));
            return;
        }
        
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private String getExceptionResponse(String message, HttpServletResponse response) throws JsonProcessingException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return new ObjectMapper().writeValueAsString(
                new ExceptionResponse(
                    new Timestamp(System.currentTimeMillis()),
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    message
                )
        );
    }
}
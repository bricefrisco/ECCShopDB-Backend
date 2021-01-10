package com.shopdb.ecocitycraft.global.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.shopdb.ecocitycraft.global.exceptions.ErrorResponse;
import com.shopdb.ecocitycraft.security.config.JWTConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopdb.ecocitycraft.security.database.repositories.UserRepository;
import com.shopdb.ecocitycraft.security.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;


public class RequestFilter extends BasicAuthenticationFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private final JWTConfiguration jwtConfig;
    private final AuthenticationService authenticationService;

    public RequestFilter(AuthenticationManager authManager, JWTConfiguration jwtConfig, AuthenticationService authenticationService) {
        super(authManager);
        this.jwtConfig = jwtConfig;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(HEADER_STRING);

        // Accept gzipped requests on POST requests, decompress on the fly
        if (request.getHeader(HttpHeaders.CONTENT_ENCODING) != null && request.getHeader(HttpHeaders.CONTENT_ENCODING).toUpperCase().contains("GZIP") && request.getMethod().equals(HttpMethod.POST.name())) {
            request = new GzippedInputStreamWrapper(request);
        }

        // Allow GETs on all endpoints, and the POST authentication endpoint
        if (request.getRequestURI().equals("/api/v3/authentication") || request.getMethod().equals(HttpMethod.GET.name())) {
            chain.doFilter(request, response);
            return;
        }

        // Allow use of an API key for POSTing chest shops for updates
        if (request.getMethod().equals(HttpMethod.POST.name()) && request.getRequestURI().equals("/api/v3/chest-shops")) {
            if (token == null || !authenticationService.apiKeyValid(token)) {
                sendExceptionResponse("Unauthorized", response);
                return;
            }

            chain.doFilter(request, response);
            return;
        }

        // Restrict everything else.
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            sendExceptionResponse("Unauthorized.", response);
            return;
        }

        String user;
        
        try {
            user = JWT.require(Algorithm.HMAC512(jwtConfig.getSecret().getBytes()))
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .getSubject();
        } catch (JWTVerificationException ex) {
            sendExceptionResponse(ex.getMessage(), response);
            return;
        }
        
        if (user == null) {
            sendExceptionResponse("JWT has no subject", response);
            return;
        }
        
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private void sendExceptionResponse(String message, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(getExceptionResponse(message));
    }

    private String getExceptionResponse(String message) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(
                new ErrorResponse(
                    new Timestamp(System.currentTimeMillis()),
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    message
                )
        );
    }
}

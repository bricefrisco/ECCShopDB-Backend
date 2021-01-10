package com.shopdb.ecocitycraft.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.shopdb.ecocitycraft.security.config.JWTConfiguration;
import com.shopdb.ecocitycraft.security.database.entities.User;
import com.shopdb.ecocitycraft.security.database.repositories.UserRepository;
import com.shopdb.ecocitycraft.security.models.exceptions.AuthenticationException;
import com.shopdb.ecocitycraft.security.models.LoginResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@EnableConfigurationProperties(JWTConfiguration.class)
public class AuthenticationService {
    private final BCryptPasswordEncoder encoder;
    private final JWTConfiguration jwtConfig;
    private final UserRepository userRepository;

    private static final String API_KEY_USER = "ShopDBAPI";

    public AuthenticationService(BCryptPasswordEncoder encoder, JWTConfiguration jwtConfig, UserRepository userRepository) {
        this.encoder = encoder;
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password.");
        }

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtConfig.getIssuer())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .sign(Algorithm.HMAC512(jwtConfig.getSecret().getBytes()));

        return new LoginResponse(user.getUsername(), token);
    }

    public boolean apiKeyValid(String key) {
        User user = userRepository.findByUsername(API_KEY_USER);
        return user != null && encoder.matches(key, user.getPassword());
    }
}

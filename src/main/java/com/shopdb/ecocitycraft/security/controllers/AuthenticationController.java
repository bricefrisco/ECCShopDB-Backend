package com.shopdb.ecocitycraft.security.controllers;

import com.shopdb.ecocitycraft.security.models.LoginRequest;
import com.shopdb.ecocitycraft.security.models.LoginResponse;
import com.shopdb.ecocitycraft.security.services.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }
}

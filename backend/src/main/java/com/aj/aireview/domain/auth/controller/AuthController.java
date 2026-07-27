package com.aj.aireview.domain.auth.controller;

import com.aj.aireview.domain.auth.AuthenticationService;
import com.aj.aireview.domain.auth.entity.LoginRequest;
import com.aj.aireview.domain.auth.entity.LoginResponse;
import com.aj.aireview.domain.auth.entity.RegisterRequest;
import com.aj.aireview.domain.auth.entity.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request){
         RegisterResponse response = authenticationService.register(request);

         return ResponseEntity.ok(response);
    }

}

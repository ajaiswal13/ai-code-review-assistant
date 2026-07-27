package com.aj.aireview.domain.auth;

import com.aj.aireview.domain.auth.entity.LoginRequest;
import com.aj.aireview.domain.auth.entity.LoginResponse;
import com.aj.aireview.domain.auth.entity.RegisterRequest;
import com.aj.aireview.domain.auth.entity.RegisterResponse;
import com.aj.aireview.domain.auth.exception.UserAlreadyExistsException;
import com.aj.aireview.domain.user.entity.Role;
import com.aj.aireview.domain.user.entity.User;
import com.aj.aireview.domain.user.repository.UserRepository;
import com.aj.aireview.security.jwt.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService,
                                 UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest loginRequest){
      Authentication authentication = authenticationManager.authenticate
              (new UsernamePasswordAuthenticationToken(
                      loginRequest.email(),
                      loginRequest.password()));
      String token = jwtService.generateToken(authentication);

      return new LoginResponse(token,"accessToken");
    }

    public RegisterResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "User with email '" + request.email() + "' already exists."
            );
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPasswordHash(encodedPassword);
        user.setRole(Role.USER);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                "User registered successfully."
        );
    }
}

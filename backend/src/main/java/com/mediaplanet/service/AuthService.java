package com.mediaplanet.service;

import com.mediaplanet.dto.JwtResponse;
import com.mediaplanet.dto.LoginRequest;
import com.mediaplanet.entity.User;
import com.mediaplanet.repository.UserRepository;
import com.mediaplanet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtResponse login(LoginRequest loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            logger.info("Login successful for user: {}", loginRequest.getUsername());
            return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        } catch (Exception e) {
            logger.error("Login failed for user: {}. Error: {}", loginRequest.getUsername(), e.getMessage());
            throw e;
        }
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}

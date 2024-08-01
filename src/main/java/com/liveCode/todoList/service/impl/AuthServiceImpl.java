package com.liveCode.todoList.service.impl;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.repository.UserRepository;
import com.liveCode.todoList.security.JwtUtil;
import com.liveCode.todoList.service.AuthService;
import com.liveCode.todoList.utils.dto.AuthDTO;
import com.liveCode.todoList.utils.dto.RegisterDTO;
import com.liveCode.todoList.utils.response.AuthResponse;
import com.liveCode.todoList.utils.response.RefreshResponse;
import com.liveCode.todoList.utils.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public AuthResponse login(AuthDTO req) {
        try {
            UserEntity user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), req.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during authentication");
        }
    }

    @Override
    public RegisterResponse registerUser(RegisterDTO req) {
        if (userRepository.existsByUsername(req.getUserName())) {
            throw new RuntimeException("Username is already taken");
        }

        List<String> passwordErrors = validatePassword(req.getPassword());
        if (!passwordErrors.isEmpty()) {
            throw new IllegalArgumentException("Password does not meet the requirements : " + String.join(", ", passwordErrors));
        }
        Role role = Role.USER;
        if (req.getRole() != null) {
            role = Role.valueOf(req.getRole().name().toUpperCase());
        }
        UserEntity user = UserEntity.builder()
                .username(req.getUserName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .build();
        userRepository.save(user);
        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(role.name())
                .build();

        jwtUtil.generateAccessToken(userDetails);
        jwtUtil.generateRefreshToken(userDetails);
        return RegisterResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    @Override
    public RefreshResponse refreshToken(String refreshToken) {
        if (jwtUtil.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userRepository.findByUsername(username)
                    .map(user -> User.withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(user.getRole().name())
                            .build())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            return RefreshResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    private List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            errors.add("Password must contain at least one special character");
        }

        return errors;
    }
}

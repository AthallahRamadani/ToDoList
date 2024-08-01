package com.liveCode.todoList.service.impl;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.ToDo;
import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.repository.ToDoRepository;
import com.liveCode.todoList.repository.UserRepository;
import com.liveCode.todoList.security.JwtUtil;
import com.liveCode.todoList.service.AdminService;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.RegisterDTO;
import com.liveCode.todoList.utils.dto.RoleDTO;
import com.liveCode.todoList.utils.response.UserGetAllAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final ToDoRepository toDoRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private final JwtUtil jwtUtil;

    @Override
    public CustomPage<UserEntity> getAllUsers(Pageable pageable, Authentication auth) {
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        return new CustomPage<>(userPage);
    }

    @Override
    public CustomPage<ToDo> getAllToDo(Pageable pageable, Authentication auth) {
        Page<ToDo> toDoPage = toDoRepository.findAll(pageable);
        return new CustomPage<>(toDoPage);
    }

    @Override
    public UserEntity getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public ToDo getOneToDo(Long id) {
        return toDoRepository.findById(id).orElseThrow();
    }

    @Override
    public UserEntity updateRole(Long id, RoleDTO req) {
        UserEntity foundUser = userRepository.findById(id).orElseThrow();
        foundUser.setRole(req.getRole());
        return userRepository.save(foundUser);
    }

    @Override
    public UserGetAllAdminResponse registerSuperAdmin(RegisterDTO req) {
        if (userRepository.existsByUsername(req.getUserName())) {
            throw new RuntimeException("Username is already taken");
        }

        List<String> passwordErrors = validatePassword(req.getPassword());
        if (!passwordErrors.isEmpty()) {
            throw new IllegalArgumentException("Password does not meet the requirements : " + String.join(", ", passwordErrors));
        }
        Role role = Role.SUPER_ADMIN;
        if (req.getRole() != null) {
            role = Role.valueOf(req.getRole().name().toUpperCase());
        }
        UserEntity user = UserEntity.builder()
                .username(req.getUserName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(role)
                .createdAt(LocalDate.now())
                .build();
        userRepository.save(user);
        UserDetails userDetails = User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(role.name())
                .build();

        jwtUtil.generateAccessToken(userDetails);
        jwtUtil.generateRefreshToken(userDetails);
        return UserGetAllAdminResponse.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
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

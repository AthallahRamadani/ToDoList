package com.liveCode.todoList.service.impl;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.repository.UserRepository;
import com.liveCode.todoList.service.UserService;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.UserDTO;
import com.liveCode.todoList.utils.dto.UserUpdateDTO;
import com.liveCode.todoList.utils.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity create(UserDTO request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.SUPER_ADMIN);
        return userRepository.save(user);
    }

    @Override
    public CustomPage<UserEntity> getAll(Pageable pageable, String name) {
        Specification<UserEntity> specification = UserSpecification.getSpecification(name);
        Page<UserEntity> userPage = userRepository.findAll(specification, pageable);
        return new CustomPage<>(userPage);
    }

    @Override
    public UserEntity getById(Long id) {
        return null;
    }

    @Override
    public UserEntity update(UserUpdateDTO request, Long id) {
        return null;
    }

    @Override
    public String deleteById(Long id) {
        return "";
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

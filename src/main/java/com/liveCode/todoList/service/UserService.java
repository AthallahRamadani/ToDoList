package com.liveCode.todoList.service;

import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.UserDTO;
import com.liveCode.todoList.utils.dto.UserUpdateDTO;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserEntity create(UserDTO request);
    CustomPage<UserEntity> getAll(Pageable pageable, String name);
    UserEntity getById(Long id);
    UserEntity update(UserUpdateDTO request, Long id);
    String deleteById(Long id);
    UserEntity getByUsername(String username);
}

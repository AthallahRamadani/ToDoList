package com.liveCode.todoList.service;

import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.utils.dto.AuthDTO;
import com.liveCode.todoList.utils.dto.RegisterDTO;
import com.liveCode.todoList.utils.response.AuthResponse;
import com.liveCode.todoList.utils.response.RefreshResponse;
import com.liveCode.todoList.utils.response.RegisterResponse;

public interface AuthService {
    AuthResponse login(AuthDTO req);
    RegisterResponse registerUser(RegisterDTO req);
    RefreshResponse refreshToken(String refreshToken);
}

package com.liveCode.todoList.service;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.ToDo;
import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.RegisterDTO;
import com.liveCode.todoList.utils.dto.RoleDTO;
import com.liveCode.todoList.utils.response.UserGetAllAdminResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface AdminService {
    CustomPage<UserEntity> getAllUsers(Pageable pageable, Authentication auth);
    CustomPage<ToDo> getAllToDo(Pageable pageable, Authentication auth);

    UserEntity getOneUser(Long id);

    ToDo getOneToDo(Long id);

    UserEntity updateRole(Long id, RoleDTO req);

    UserGetAllAdminResponse registerSuperAdmin(RegisterDTO req);

}

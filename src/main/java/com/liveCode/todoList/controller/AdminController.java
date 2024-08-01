package com.liveCode.todoList.controller;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.ToDo;
import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.service.AdminService;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.RegisterDTO;
import com.liveCode.todoList.utils.dto.RoleDTO;
import com.liveCode.todoList.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @Value("${superadmin.secret}")
    private String superAdminSecretKey;
    @Value("${admin.secret}")
    private String adminSecretKey;


    @GetMapping("/users")
    public ResponseEntity<CustomPage<UserEntity>> getAll(
            @PageableDefault Pageable pageable,
            Authentication auth
    ) {
        CustomPage<UserEntity> result = adminService.getAllUsers(pageable, auth);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/todos")
    public ResponseEntity<CustomPage<ToDo>> getAllToDo(
            @PageableDefault Pageable pageable,
            Authentication auth
    ) {
        CustomPage<ToDo> result = adminService.getAllToDo(pageable, auth);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOne(
            @PathVariable Long id
    ) {
        UserEntity result = adminService.getOneUser(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getOneTodos(
            @PathVariable Long id
    ) {
        ToDo result = adminService.getOneToDo(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO req, @RequestHeader(value = "X-Admin-Secret-Key") String key) {
        if (key == null || !key.equals(adminSecretKey)) {
            return Response.renderJSON(
                    null,
                    "Invalid Admin Secret Key",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return Response.renderJSON(
                adminService.updateRole(id, req)
        );
    }

    @PostMapping("/super-admin")
    public ResponseEntity<?> createSuperAdmin(@Valid @RequestBody RegisterDTO req, @RequestHeader(value = "X-Super-Admin-Secret-Key") String key) {
        if (key == null || !key.equals(superAdminSecretKey)) {
            return Response.renderJSON(
                    null,
                    "Invalid Super Admin Secret Key",
                    HttpStatus.UNAUTHORIZED
            );
        }
        return Response.renderJSON(
                adminService.registerSuperAdmin(req),
                "User created successfully",
                HttpStatus.CREATED
        );
    }


}

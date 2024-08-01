package com.liveCode.todoList.utils.response;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetAllAdminResponse {
    private Long id;
    private String userName;
    private String email;
    private Role role;
    private LocalDate createdAt;
}

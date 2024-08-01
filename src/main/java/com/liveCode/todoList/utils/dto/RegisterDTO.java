package com.liveCode.todoList.utils.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.liveCode.todoList.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {
    @NotBlank(message = "Username is required")
    private String userName;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // supaya tidak di-return di response API
    private Role role;
}

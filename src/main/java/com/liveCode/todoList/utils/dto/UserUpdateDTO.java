package com.liveCode.todoList.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @NotBlank
    private String userName;

    @NotBlank
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // supaya tidak di-return di response API
    private String password;
}

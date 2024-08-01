package com.liveCode.todoList.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank
    private String userName;

    @NotBlank
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // supaya tidak di-return di response API
    private String password;
}

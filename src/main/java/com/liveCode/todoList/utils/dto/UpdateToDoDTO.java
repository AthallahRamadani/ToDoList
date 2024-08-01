package com.liveCode.todoList.utils.dto;

import com.liveCode.todoList.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateToDoDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;

    private Status status;
}


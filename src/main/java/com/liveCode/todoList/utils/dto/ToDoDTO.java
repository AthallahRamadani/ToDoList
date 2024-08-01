package com.liveCode.todoList.utils.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDoDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull(message = "Due date cannot be null")
    private LocalDate dueDate;
}

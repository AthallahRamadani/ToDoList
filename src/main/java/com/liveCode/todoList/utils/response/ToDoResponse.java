package com.liveCode.todoList.utils.response;

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
public class ToDoResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Status status;
    private LocalDate createdAt;
}

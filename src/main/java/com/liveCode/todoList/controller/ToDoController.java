package com.liveCode.todoList.controller;

import com.liveCode.todoList.model.ToDo;
import com.liveCode.todoList.service.ToDoService;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.ToDoDTO;
import com.liveCode.todoList.utils.dto.UpdateToDoDTO;
import com.liveCode.todoList.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;

    @PostMapping
    @Validated
    public ResponseEntity<?> create(
            @Valid @RequestBody ToDoDTO newToDo,
            Authentication auth
    ) {
        return Response.renderJSON(
                toDoService.create(newToDo, auth),
                "New Task Record Created",
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<CustomPage<ToDo>> getAll(
            @PageableDefault Pageable pageable,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "order", required = false) String order,
            Authentication auth
    ) {
        CustomPage<ToDo> result = toDoService.getAll(pageable, status, sortBy, order, auth);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id,
            Authentication auth
    ) {
        return Response.renderJSON(toDoService.getById(id, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody UpdateToDoDTO request,
            @PathVariable Long id,
            Authentication auth
    ) {
        return Response.renderJSON(toDoService.update(request, id, auth));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOne(@PathVariable Long id, Authentication authentication) {
        toDoService.deleteOne(id, authentication);
        return ResponseEntity.noContent().build();
    }
}

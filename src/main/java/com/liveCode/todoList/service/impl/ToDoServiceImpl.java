package com.liveCode.todoList.service.impl;

import com.liveCode.todoList.model.Role;
import com.liveCode.todoList.model.Status;
import com.liveCode.todoList.model.ToDo;
import com.liveCode.todoList.model.UserEntity;
import com.liveCode.todoList.repository.ToDoRepository;
import com.liveCode.todoList.service.ToDoService;
import com.liveCode.todoList.service.UserService;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.ToDoDTO;
import com.liveCode.todoList.utils.dto.UpdateToDoDTO;
import com.liveCode.todoList.utils.response.ToDoResponse;
import com.liveCode.todoList.utils.specification.ToDoSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final UserService userService;

    @Override
    public ToDoResponse create(ToDoDTO req, Authentication auth) {
        String username = auth.getName();
        log.info("Creating ToDo for user: {}", username);

        UserEntity user = userService.getByUsername(auth.getName());


        ToDo toDo = ToDo.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .dueDate(req.getDueDate())
                .status(Status.PENDING)
                .createdAt(LocalDate.now())
                .user(user)
                .build();

        ToDo savedTodo = toDoRepository.save(toDo);

        return new ToDoResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getDescription(),
                savedTodo.getDueDate(),
                savedTodo.getStatus(),
                savedTodo.getCreatedAt()
        );
    }

    @Override
    public CustomPage<ToDo> getAll(Pageable pageable, String status, String sortBy, String order, Authentication auth) {
        UserEntity user = userService.getByUsername(auth.getName());
        Specification<ToDo> specification = ToDoSpecification.getSpecification(status, sortBy, order, user.getId());
        Page<ToDo> toDoPage = toDoRepository.findAll(specification, pageable);
        return new CustomPage<>(toDoPage);
    }

    @Override
    public ToDo getById(Long id, Authentication auth) {
        ToDo todo = toDoRepository.findById(id).orElseThrow(() -> new RuntimeException("Todo item not found"));
        if (auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_SUPER_ADMIN"))) {
            return todo;
        } else {
            UserEntity user = userService.getByUsername(auth.getName());
            if (todo.getUser().getId().equals(user.getId())) {
                return todo;
            } else {
                throw new RuntimeException("Todo item not found");
            }
        }
    }

    @Override
    public ToDo update(UpdateToDoDTO req, Long id, Authentication auth) {
        ToDo foundToDo = getById(id, auth);
        foundToDo.setTitle(req.getTitle());
        foundToDo.setDescription(req.getDescription());
        foundToDo.setDueDate(req.getDueDate());
        foundToDo.setStatus(req.getStatus());

        return toDoRepository.save(foundToDo);
    }

    @Override
    public void deleteOne(Long id, Authentication auth) {
        ToDo todo = this.getById(id, auth);
        System.out.println(auth.getName());
        toDoRepository.delete(todo);
    }

}

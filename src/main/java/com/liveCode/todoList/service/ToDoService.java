package com.liveCode.todoList.service;

import com.liveCode.todoList.model.ToDo;
import com.liveCode.todoList.utils.CustomPage;
import com.liveCode.todoList.utils.dto.ToDoDTO;
import com.liveCode.todoList.utils.dto.UpdateToDoDTO;
import com.liveCode.todoList.utils.response.ToDoResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ToDoService {
    ToDoResponse create(ToDoDTO req, Authentication auth);
    CustomPage<ToDo> getAll(Pageable pageable, String status, String sortBy, String order, Authentication auth);
    ToDo getById(Long id, Authentication auth);
    ToDo update(UpdateToDoDTO req, Long id, Authentication auth);
    void deleteOne(Long id, Authentication auth);
}

package com.liveCode.todoList.repository;

import com.liveCode.todoList.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long>, JpaSpecificationExecutor<ToDo> {
}

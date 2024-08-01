package com.liveCode.todoList.repository;

import com.liveCode.todoList.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<UserEntity, Long> {
}

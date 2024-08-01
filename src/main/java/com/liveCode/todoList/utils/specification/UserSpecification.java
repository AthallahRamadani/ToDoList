package com.liveCode.todoList.utils.specification;

import com.liveCode.todoList.model.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<UserEntity> getSpecification(String userName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userName != null && !userName.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("userName"), "%" + userName + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

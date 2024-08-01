package com.liveCode.todoList.utils.specification;

import com.liveCode.todoList.model.ToDo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ToDoSpecification {

    public static Specification<ToDo> getSpecification(String status, String sortBy, String order, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }

            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

            if (sortBy != null && !sortBy.isEmpty()) {
                if (order != null && order.equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(sortBy)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(sortBy)));
                }
            }

            return query.getRestriction();
        };
    }
}

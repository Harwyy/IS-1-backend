package com.is.lw.core.specification;

import com.is.lw.core.model.Discipline;
import org.springframework.data.jpa.domain.Specification;

public class DisciplineSpecification {

    public static Specification<Discipline> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}

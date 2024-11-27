package com.is.lw.core.specification;

import com.is.lw.core.model.Discipline;
import com.is.lw.core.model.LabWork;
import org.springframework.data.jpa.domain.Specification;

public class LabWorkSpecification {

    public static Specification<LabWork> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<LabWork> filterByDescriptionContains(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

}

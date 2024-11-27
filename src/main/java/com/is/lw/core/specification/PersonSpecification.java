package com.is.lw.core.specification;

import com.is.lw.core.model.Person;
import org.springframework.data.jpa.domain.Specification;


public class PersonSpecification {

    public static Specification<Person> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}

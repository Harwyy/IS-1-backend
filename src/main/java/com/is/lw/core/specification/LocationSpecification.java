package com.is.lw.core.specification;

import com.is.lw.core.model.Location;
import org.springframework.data.jpa.domain.Specification;

public class LocationSpecification {

    public static Specification<Location> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

}

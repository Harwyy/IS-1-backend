package com.is.lw.core.specification;

import com.is.lw.core.model.Location;
import org.springframework.data.jpa.domain.Specification;

public class LocationSpecification {

    public static Specification<Location> filterByIdEquals(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Location> filterByNameEquals(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Location> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Location> filterByXEquals(Long x) {
        return (root, query, criteriaBuilder) ->
                x == null ? null : criteriaBuilder.equal(root.get("x"), x);
    }

    public static Specification<Location> filterByXGreaterThan(Long x) {
        return (root, query, criteriaBuilder) ->
                x == null ? null : criteriaBuilder.greaterThan(root.get("x"), x);
    }

    public static Specification<Location> filterByXLessThan(Long x) {
        return (root, query, criteriaBuilder) ->
                x == null ? null : criteriaBuilder.lessThan(root.get("x"), x);
    }

    public static Specification<Location> filterByYEquals(Double y) {
        return (root, query, criteriaBuilder) ->
                y == null ? null : criteriaBuilder.equal(root.get("y"), y);
    }

    public static Specification<Location> filterByYGreaterThan(Double y) {
        return (root, query, criteriaBuilder) ->
                y == null ? null : criteriaBuilder.greaterThan(root.get("y"), y);
    }

    public static Specification<Location> filterByYLessThan(Double y) {
        return (root, query, criteriaBuilder) ->
                y == null ? null : criteriaBuilder.lessThan(root.get("y"), y);
    }

    public static Specification<Location> filterByZEquals(Integer z) {
        return (root, query, criteriaBuilder) ->
                z == null ? null : criteriaBuilder.equal(root.get("z"), z);
    }

    public static Specification<Location> filterByZGreaterThan(Integer z) {
        return (root, query, criteriaBuilder) ->
                z == null ? null : criteriaBuilder.greaterThan(root.get("z"), z);
    }

    public static Specification<Location> filterByZLessThan(Integer z) {
        return (root, query, criteriaBuilder) ->
                z == null ? null : criteriaBuilder.lessThan(root.get("z"), z);
    }

}

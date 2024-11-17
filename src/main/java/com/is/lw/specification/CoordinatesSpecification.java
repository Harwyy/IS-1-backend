package com.is.lw.specification;

import com.is.lw.model.Coordinates;
import org.springframework.data.jpa.domain.Specification;

public class CoordinatesSpecification {

    public static Specification<Coordinates> filterByXEquals(Float x) {
        return (root, query, criteriaBuilder) ->
                x == null ? null : criteriaBuilder.equal(root.get("x"), x);
    }

    public static Specification<Coordinates> filterByXGreaterThan(Float x) {
        return (root, query, criteriaBuilder) ->
                x == null ? null : criteriaBuilder.greaterThan(root.get("x"), x);
    }

    public static Specification<Coordinates> filterByXLessThan(Float x) {
        return (root, query, criteriaBuilder) ->
                x == null ? null : criteriaBuilder.lessThan(root.get("x"), x);
    }

    public static Specification<Coordinates> filterByYEquals(Long y) {
        return (root, query, criteriaBuilder) ->
                y == null ? null : criteriaBuilder.equal(root.get("y"), y);
    }

    public static Specification<Coordinates> filterByYGreaterThan(Long y) {
        return (root, query, criteriaBuilder) ->
                y == null ? null : criteriaBuilder.greaterThan(root.get("y"), y);
    }

    public static Specification<Coordinates> filterByYLessThan(Long y) {
        return (root, query, criteriaBuilder) ->
                y == null ? null : criteriaBuilder.lessThan(root.get("y"), y);
    }

    public static Specification<Coordinates> filterByZEquals(Integer z) {
        return (root, query, criteriaBuilder) ->
                z == null ? null : criteriaBuilder.equal(root.get("z"), z);
    }

    public static Specification<Coordinates> filterByZGreaterThan(Integer z) {
        return (root, query, criteriaBuilder) ->
                z == null ? null : criteriaBuilder.greaterThan(root.get("z"), z);
    }

    public static Specification<Coordinates> filterByZLessThan(Integer z) {
        return (root, query, criteriaBuilder) ->
                z == null ? null : criteriaBuilder.lessThan(root.get("z"), z);
    }

    public static Specification<Coordinates> filterByIdEquals(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

}

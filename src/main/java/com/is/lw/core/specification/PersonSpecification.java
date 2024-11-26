package com.is.lw.core.specification;

import com.is.lw.core.model.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecification {

    public static Specification<Person> filterByIdEquals(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Person> filterByNameEquals(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Person> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Person> filterByColorEquals(String color) {
        return (root, query, criteriaBuilder) ->
                color == null ? null : criteriaBuilder.equal(root.get("color"), color);
    }

    public static Specification<Person> filterByHairColorEquals(String hairColor) {
        return (root, query, criteriaBuilder) ->
                hairColor == null ? null : criteriaBuilder.equal(root.get("hairColor"), hairColor);
    }

    public static Specification<Person> filterByWeightEquals(Long weight) {
        return (root, query, criteriaBuilder) ->
                weight == null ? null : criteriaBuilder.equal(root.get("weight"), weight);
    }

    public static Specification<Person> filterByWeightGreaterThan(Long weight) {
        return (root, query, criteriaBuilder) ->
                weight == null ? null : criteriaBuilder.greaterThan(root.get("weight"), weight);
    }

    public static Specification<Person> filterByWeightLessThan(Long weight) {
        return (root, query, criteriaBuilder) ->
                weight == null ? null : criteriaBuilder.lessThan(root.get("weight"), weight);
    }

    public static Specification<Person> filterByNationalityEquals(String nationality) {
        return (root, query, criteriaBuilder) ->
                nationality == null ? null : criteriaBuilder.equal(root.get("nationality"), nationality);
    }

    public static Specification<Person> filterByLocationIdEquals(Long locationId) {
        return (root, query, criteriaBuilder) ->
                locationId == null ? null : criteriaBuilder.equal(root.get("location").get("id"), locationId);
    }

    public static Specification<Person> filterByCreatedByEquals(Long createdById) {
        return (root, query, criteriaBuilder) ->
                createdById == null ? null : criteriaBuilder.equal(root.get("createdBy").get("id"), createdById);
    }

}

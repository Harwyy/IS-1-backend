package com.is.lw.specification;

import com.is.lw.model.LabWork;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LabWorkSpecification {

    public static Specification<LabWork> filterByIdEquals(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<LabWork> filterByNameEquals(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<LabWork> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<LabWork> filterByCoordinatesIdEquals(Long coordinatesId) {
        return (root, query, criteriaBuilder) ->
                coordinatesId == null ? null : criteriaBuilder.equal(root.get("coordinates").get("id"), coordinatesId);
    }

    public static Specification<LabWork> filterByDescriptionContains(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<LabWork> filterByDifficultyEquals(String difficulty) {
        return (root, query, criteriaBuilder) ->
                difficulty == null ? null : criteriaBuilder.equal(root.get("difficulty"), difficulty);
    }

    public static Specification<LabWork> filterByDisciplineIdEquals(Long disciplineId) {
        return (root, query, criteriaBuilder) ->
                disciplineId == null ? null : criteriaBuilder.equal(root.get("discipline").get("id"), disciplineId);
    }

    public static Specification<LabWork> filterByMinimalPointEquals(Float minimalPoint) {
        return (root, query, criteriaBuilder) ->
                minimalPoint == null ? null : criteriaBuilder.equal(root.get("minimalPoint"), minimalPoint);
    }

    public static Specification<LabWork> filterByMinimalPointGreaterThan(Float minimalPoint) {
        return (root, query, criteriaBuilder) ->
                minimalPoint == null ? null : criteriaBuilder.greaterThan(root.get("minimalPoint"), minimalPoint);
    }

    public static Specification<LabWork> filterByMinimalPointLessThan(Float minimalPoint) {
        return (root, query, criteriaBuilder) ->
                minimalPoint == null ? null : criteriaBuilder.lessThan(root.get("minimalPoint"), minimalPoint);
    }

    public static Specification<LabWork> filterByAuthorIdEquals(Long authorId) {
        return (root, query, criteriaBuilder) ->
                authorId == null ? null : criteriaBuilder.equal(root.get("author").get("id"), authorId);
    }

    public static Specification<LabWork> filterByCreatedByEquals(Long createdById) {
        return (root, query, criteriaBuilder) ->
                createdById == null ? null : criteriaBuilder.equal(root.get("createdBy").get("id"), createdById);
    }

    public static Specification<LabWork> filterByUpdatedByEquals(Long updatedById) {
        return (root, query, criteriaBuilder) ->
                updatedById == null ? null : criteriaBuilder.equal(root.get("updatedBy").get("id"), updatedById);
    }

    public static Specification<LabWork> filterByCreatedAtAfter(LocalDateTime createdAt) {
        return (root, query, criteriaBuilder) ->
                createdAt == null ? null : criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), createdAt);
    }

    public static Specification<LabWork> filterByCreatedAtBefore(LocalDateTime createdAt) {
        return (root, query, criteriaBuilder) ->
                createdAt == null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), createdAt);
    }

}

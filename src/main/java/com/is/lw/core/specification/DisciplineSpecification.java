package com.is.lw.core.specification;

import com.is.lw.core.model.Discipline;
import org.springframework.data.jpa.domain.Specification;

public class DisciplineSpecification {

    public static Specification<Discipline> filterByIdEquals(Long id) {
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("id"), id);
    }

    public static Specification<Discipline> filterByNameEquals(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Discipline> filterByNameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Discipline> filterByLectureHoursEquals(Long lectureHours) {
        return (root, query, criteriaBuilder) ->
                lectureHours == null ? null : criteriaBuilder.equal(root.get("lectureHours"), lectureHours);
    }

    public static Specification<Discipline> filterByLectureHoursGreaterThan(Long lectureHours) {
        return (root, query, criteriaBuilder) ->
                lectureHours == null ? null : criteriaBuilder.greaterThan(root.get("lectureHours"), lectureHours);
    }

    public static Specification<Discipline> filterByLectureHoursLessThan(Long lectureHours) {
        return (root, query, criteriaBuilder) ->
                lectureHours == null ? null : criteriaBuilder.lessThan(root.get("lectureHours"), lectureHours);
    }

    public static Specification<Discipline> filterByPracticeHoursEquals(Integer practiceHours) {
        return (root, query, criteriaBuilder) ->
                practiceHours == null ? null : criteriaBuilder.equal(root.get("practiceHours"), practiceHours);
    }

    public static Specification<Discipline> filterByPracticeHoursGreaterThan(Integer practiceHours) {
        return (root, query, criteriaBuilder) ->
                practiceHours == null ? null : criteriaBuilder.greaterThan(root.get("practiceHours"), practiceHours);
    }

    public static Specification<Discipline> filterByPracticeHoursLessThan(Integer practiceHours) {
        return (root, query, criteriaBuilder) ->
                practiceHours == null ? null : criteriaBuilder.lessThan(root.get("practiceHours"), practiceHours);
    }

    public static Specification<Discipline> filterBySelfStudyHoursEquals(Integer selfStudyHours) {
        return (root, query, criteriaBuilder) ->
                selfStudyHours == null ? null : criteriaBuilder.equal(root.get("selfStudyHours"), selfStudyHours);
    }

    public static Specification<Discipline> filterBySelfStudyHoursGreaterThan(Integer selfStudyHours) {
        return (root, query, criteriaBuilder) ->
                selfStudyHours == null ? null : criteriaBuilder.greaterThan(root.get("selfStudyHours"), selfStudyHours);
    }

    public static Specification<Discipline> filterBySelfStudyHoursLessThan(Integer selfStudyHours) {
        return (root, query, criteriaBuilder) ->
                selfStudyHours == null ? null : criteriaBuilder.lessThan(root.get("selfStudyHours"), selfStudyHours);
    }

    public static Specification<Discipline> filterByLabsCountEquals(Long labsCount) {
        return (root, query, criteriaBuilder) ->
                labsCount == null ? null : criteriaBuilder.equal(root.get("labsCount"), labsCount);
    }

    public static Specification<Discipline> filterByLabsCountGreaterThan(Long labsCount) {
        return (root, query, criteriaBuilder) ->
                labsCount == null ? null : criteriaBuilder.greaterThan(root.get("labsCount"), labsCount);
    }

    public static Specification<Discipline> filterByLabsCountLessThan(Long labsCount) {
        return (root, query, criteriaBuilder) ->
                labsCount == null ? null : criteriaBuilder.lessThan(root.get("labsCount"), labsCount);
    }
}

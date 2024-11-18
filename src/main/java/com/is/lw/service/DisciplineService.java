package com.is.lw.service;

import com.is.lw.auth.model.User;
import com.is.lw.controller.Request.DisciplineAddRequest;
import com.is.lw.controller.Request.DisciplineUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.Discipline;
import com.is.lw.model.enums.Status;
import com.is.lw.repository.DisciplineRepository;
import com.is.lw.specification.DisciplineSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DisciplineService {

    private final DisciplineRepository repository;

    public MyResponse addDiscipline(DisciplineAddRequest request) {

        repository.save(Discipline.builder()
                .name(request.getName())
                .lectureHours(request.getLectureHours())
                .practiceHours(request.getPracticeHours())
                .selfStudyHours(request.getSelfStudyHours())
                .labsCount(request.getLabsCount())
                .build());
        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();

    }

    public MyResponse updateDiscipline(DisciplineUpdateRequest request) {

        if (!repository.existsById(request.getId())) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Discipline with id " + request.getId() + " not found.")
                    .build();
        }

        Discipline discipline = repository.findById(request.getId()).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != discipline.getCreatedBy().getId()) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " can`t update discipline.")
                    .build();
        }

        discipline.setName(request.getName());
        discipline.setLectureHours(request.getLectureHours());
        discipline.setPracticeHours(request.getPracticeHours());
        discipline.setSelfStudyHours(request.getSelfStudyHours());
        discipline.setLabsCount(request.getLabsCount());
        repository.save(discipline);

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Discipline with id " + request.getId() + " was updated.")
                .build();

    }

    public MyResponse deleteDiscipline(Long id) {

        if (!repository.existsById(id)) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Discipline with id " + id + " not found.")
                    .build();
        }

        Discipline discipline = repository.findById(id).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != discipline.getCreatedBy().getId()) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " can`t delete discipline with id " + id + ".")
                    .build();
        }

        repository.deleteById(id);
        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Discipline with id " + id + " was deleted.")
                .build();

    }

    public List<Discipline> filterAndSortDisciplines(String nameEquals,
                                                     String nameContains,
                                                     Long lectureHoursEquals,
                                                     Long lectureHoursGreaterThan,
                                                     Long lectureHoursLessThan,
                                                     Integer practiceHoursEquals,
                                                     Integer practiceHoursGreaterThan,
                                                     Integer practiceHoursLessThan,
                                                     Integer selfStudyHoursEquals,
                                                     Integer selfStudyHoursGreaterThan,
                                                     Integer selfStudyHoursLessThan,
                                                     Long labsCountEquals,
                                                     Long labsCountGreaterThan,
                                                     Long labsCountLessThan,
                                                     Long idEquals,
                                                     String sortBy,
                                                     String direction,
                                                     int page,
                                                     int size) {

        Specification<Discipline> specification = Specification
                .where(DisciplineSpecification.filterByNameEquals(nameEquals))
                .and(DisciplineSpecification.filterByNameContains(nameContains))
                .and(DisciplineSpecification.filterByLectureHoursEquals(lectureHoursEquals))
                .and(DisciplineSpecification.filterByLectureHoursGreaterThan(lectureHoursGreaterThan))
                .and(DisciplineSpecification.filterByLectureHoursLessThan(lectureHoursLessThan))
                .and(DisciplineSpecification.filterByPracticeHoursEquals(practiceHoursEquals))
                .and(DisciplineSpecification.filterByPracticeHoursGreaterThan(practiceHoursGreaterThan))
                .and(DisciplineSpecification.filterByPracticeHoursLessThan(practiceHoursLessThan))
                .and(DisciplineSpecification.filterBySelfStudyHoursEquals(selfStudyHoursEquals))
                .and(DisciplineSpecification.filterBySelfStudyHoursGreaterThan(selfStudyHoursGreaterThan))
                .and(DisciplineSpecification.filterBySelfStudyHoursLessThan(selfStudyHoursLessThan))
                .and(DisciplineSpecification.filterByLabsCountEquals(labsCountEquals))
                .and(DisciplineSpecification.filterByLabsCountGreaterThan(labsCountGreaterThan))
                .and(DisciplineSpecification.filterByLabsCountLessThan(labsCountLessThan))
                .and(DisciplineSpecification.filterByIdEquals(idEquals));

        Sort sort = getSort(sortBy, direction);

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(specification, pageable);
    }

    private Sort getSort(String sortBy, String direction) {

        if (sortBy == null || direction == null) {
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);

    }
}

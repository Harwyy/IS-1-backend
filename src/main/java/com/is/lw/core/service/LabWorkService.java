package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.core.controller.Request.LabWorkAddRequest;
import com.is.lw.core.controller.Request.LabWorkUpdateRequest;
import com.is.lw.core.controller.Response.MyResponse;
import com.is.lw.core.model.*;
import com.is.lw.core.model.enums.Status;
import com.is.lw.core.repository.*;
import com.is.lw.core.specification.LabWorkSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LabWorkService {

    private final LabWorkRepository repository;
    private final PersonRepository personRepository;
    private final DisciplineRepository disciplineRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final LocationRepository locationRepository;

    public MyResponse addLabWork(LabWorkAddRequest request) {

        Coordinates coordinates = null;
        Discipline discipline = null;
        Person person = null;
        Location location = null;

        if (request.getCoordinates() != null){
            if (request.getCoordinates().getId() == null){
                coordinates = coordinatesRepository.save(
                        Coordinates.builder()
                                .x(request.getCoordinates().getX())
                                .y(request.getCoordinates().getY())
                                .z(request.getCoordinates().getZ())
                                .build()
                );
            } else {
                Long coordinatesId = request.getCoordinates().getId();
                coordinates = coordinatesRepository.findById(coordinatesId)
                        .orElseThrow(() -> new RuntimeException("Coordinates with id " + coordinatesId + " not found."));
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!user.getId().equals(coordinates.getCreatedBy().getId())) {
                    return MyResponse.builder()
                            .status(Status.FAIL)
                            .message("User with id " + user.getId() + " is not authorized to use coordinates with id " + coordinates + ".")
                            .build();
                }
            }
        }

        if (request.getDiscipline() != null){
            if (request.getDiscipline().getId() == null){
                discipline = disciplineRepository.save(
                        Discipline.builder()
                                .name(request.getDiscipline().getName())
                                .lectureHours(request.getDiscipline().getLectureHours())
                                .practiceHours(request.getDiscipline().getPracticeHours())
                                .labsCount(request.getDiscipline().getLabsCount())
                                .selfStudyHours(request.getDiscipline().getSelfStudyHours())
                                .build()
                );
            } else {
                Long disciplineId = request.getDiscipline().getId();
                discipline = disciplineRepository.findById(disciplineId)
                        .orElseThrow(() -> new RuntimeException("Discipline with id " + disciplineId + " not found."));
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!user.getId().equals(discipline.getCreatedBy().getId())) {
                    return MyResponse.builder()
                            .status(Status.FAIL)
                            .message("User with id " + user.getId() + " is not authorized to use discipline with id " + discipline + ".")
                            .build();
                }
            }
        }

        if (request.getAuthor() != null){
            if (request.getAuthor().getId() == null){
                if (request.getAuthor().getLocation() != null){
                    if (request.getAuthor().getLocation().getId() == null){
                        location = locationRepository.save(
                                Location.builder()
                                        .name(request.getAuthor().getLocation().getName())
                                        .x(request.getAuthor().getLocation().getX())
                                        .y(request.getAuthor().getLocation().getY())
                                        .z(request.getAuthor().getLocation().getZ())
                                        .build()
                        );
                    } else {
                        Long locationId = request.getAuthor().getLocation().getId();
                        location = locationRepository.findById(locationId)
                                .orElseThrow(() -> new IllegalArgumentException("Location with id " + locationId + " not found."));
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        if (!user.getId().equals(location.getCreatedBy().getId())) {
                            return MyResponse.builder()
                                    .status(Status.FAIL)
                                    .message("User with id " + user.getId() + " is not authorized to use location with id " + locationId + ".")
                                    .build();
                        }
                    }
                }
                person = personRepository.save(
                        Person.builder()
                                .name(request.getAuthor().getName())
                                .color(request.getAuthor().getColor())
                                .hairColor(request.getAuthor().getHairColor())
                                .location(location)
                                .nationality(request.getAuthor().getNationality())
                                .weight(request.getAuthor().getWeight())
                                .build()
                );
            } else {
                Long personId = request.getAuthor().getId();

                person = personRepository.findById(personId)
                        .orElseThrow(() -> new RuntimeException("Person with id " + personId + " not found."));
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!user.getId().equals(person.getCreatedBy().getId())) {
                    return MyResponse.builder()
                            .status(Status.FAIL)
                            .message("User with id " + user.getId() + " is not authorized to use person with id " + personId + ".")
                            .build();
                }
            }
        }

        repository.save(
                LabWork.builder()
                        .name(request.getName())
                        .coordinates(coordinates)
                        .description(request.getDescription())
                        .difficulty(request.getDifficulty())
                        .discipline(discipline)
                        .minimalPoint(request.getMinimalPoint())
                        .author(person)
                        .build()
        );

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    public MyResponse updateLabWork(LabWorkUpdateRequest request){

        if (!repository.existsById(request.getId())){
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Laboratory work with id " + request.getId() + " not found.")
                    .build();
        }

        LabWork labWork = repository.findById(request.getId()).get();
        Person person = null;
        Coordinates coordinates = null;
        Discipline discipline = null;

        if (request.getAuthor() != null){
            Long personId = request.getAuthor().getId();
            person = personRepository.findById(personId)
                    .orElseThrow(() -> new RuntimeException("Person with id " + personId + " not found."));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!user.getId().equals(person.getCreatedBy().getId())) {
                return MyResponse.builder()
                        .status(Status.FAIL)
                        .message("User with id " + user.getId() + " is not authorized to use person with id " + personId + ".")
                        .build();
            }
        }

        if (request.getDescription() != null){
            Long disciplineId = request.getDiscipline().getId();
            discipline = disciplineRepository.findById(disciplineId)
                    .orElseThrow(() -> new RuntimeException("Discipline with id " + disciplineId + " not found."));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!user.getId().equals(discipline.getCreatedBy().getId())) {
                return MyResponse.builder()
                        .status(Status.FAIL)
                        .message("User with id " + user.getId() + " is not authorized to use discipline with id " + disciplineId + ".")
                        .build();
            }
        }

        if (request.getCoordinates() != null){
            Long coordinatesId = request.getCoordinates().getId();
            coordinates = coordinatesRepository.findById(coordinatesId)
                    .orElseThrow(() -> new RuntimeException("Coordinates with id " + coordinatesId + " not found."));
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!user.getId().equals(coordinates.getCreatedBy().getId())) {
                return MyResponse.builder()
                        .status(Status.FAIL)
                        .message("User with id " + user.getId() + " is not authorized to use coordinates with id " + coordinatesId + ".")
                        .build();
            }
        }

        labWork.setName(request.getName());
        labWork.setDescription(request.getDescription());
        labWork.setDifficulty(request.getDifficulty());
        labWork.setMinimalPoint(request.getMinimalPoint());
        labWork.setAuthor(person);
        labWork.setCoordinates(coordinates);
        labWork.setDiscipline(discipline);

        repository.save(labWork);

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    @Transactional
    public MyResponse deleteLabWork(Long id){

        if (!repository.existsById(id)) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Labwork with id " + id + " not found.")
                    .build();
        }

        LabWork labWork = repository.findById(id).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getId().equals(labWork.getCreatedBy().getId())) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " is not authorized to delete labwork with id " + id + ".")
                    .build();
        }

        Long personId = labWork.getAuthor() != null ? labWork.getAuthor().getId() : null;
        Long disciplineId = labWork.getDiscipline().getId() != null ? labWork.getDiscipline().getId() : null;
        Long coordinatesId = labWork.getCoordinates() != null ? labWork.getCoordinates().getId() : null;

        repository.delete(labWork);

        if (personId != null){
            unlinkLabworkForPerson(personId);
            personRepository.deleteById(personId);
        }

        if (disciplineId != null){
            unlinkLabworkForDiscipline(disciplineId);
            disciplineRepository.deleteById(disciplineId);
        }

        if (coordinatesId != null){
            unlinkLabworkForCoordinates(coordinatesId);
            coordinatesRepository.deleteById(coordinatesId);
        }

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Person with id " + id + " was deleted.")
                .build();
    }

    @Transactional
    public void unlinkLabworkForPerson(Long id) {
        repository.updateLabWorksPersonToNull(id);
    }

    @Transactional
    public void unlinkLabworkForDiscipline(Long id) {
        repository.updateLabWorksDisciplineToNull(id);
    }

    @Transactional
    public void unlinkLabworkForCoordinates(Long id) {
        repository.updateLabWorksCoordinatesToNull(id);
    }

    public List<LabWork> filterAndSortLabWorks(
            String nameEquals,
            String nameContains,
            Long coordinatesIdEquals,
            String descriptionContains,
            String difficultyEquals,
            Long disciplineIdEquals,
            Float minimalPointEquals,
            Float minimalPointGreaterThan,
            Float minimalPointLessThan,
            Long authorIdEquals,
            Long createdByIdEquals,
            Long updatedByIdEquals,
            LocalDateTime createdAtAfter,
            LocalDateTime createdAtBefore,
            String sortBy,
            String direction,
            int page,
            int size) {

        Specification<LabWork> specification = Specification
                .where(LabWorkSpecification.filterByNameEquals(nameEquals))
                .and(LabWorkSpecification.filterByNameContains(nameContains))
                .and(LabWorkSpecification.filterByCoordinatesIdEquals(coordinatesIdEquals))
                .and(LabWorkSpecification.filterByDescriptionContains(descriptionContains))
                .and(LabWorkSpecification.filterByDifficultyEquals(difficultyEquals))
                .and(LabWorkSpecification.filterByDisciplineIdEquals(disciplineIdEquals))
                .and(LabWorkSpecification.filterByMinimalPointEquals(minimalPointEquals))
                .and(LabWorkSpecification.filterByMinimalPointGreaterThan(minimalPointGreaterThan))
                .and(LabWorkSpecification.filterByMinimalPointLessThan(minimalPointLessThan))
                .and(LabWorkSpecification.filterByAuthorIdEquals(authorIdEquals))
                .and(LabWorkSpecification.filterByCreatedByEquals(createdByIdEquals))
                .and(LabWorkSpecification.filterByUpdatedByEquals(updatedByIdEquals))
                .and(LabWorkSpecification.filterByCreatedAtAfter(createdAtAfter))
                .and(LabWorkSpecification.filterByCreatedAtBefore(createdAtBefore));

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

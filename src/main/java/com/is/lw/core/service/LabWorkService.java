package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.core.model.*;
import com.is.lw.core.repository.*;
import com.is.lw.core.specification.LabWorkSpecification;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LabWorkService {

    private final LabWorkRepository repository;
    private final DisciplineRepository disciplineRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final PersonRepository personRepository;
    private final AuditLogService auditService;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<LabWork> createLabWork(LabWork labWork) {
        if (labWork == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (labWork.getDiscipline() != null) {
            Discipline discipline = disciplineRepository.findById(labWork.getDiscipline().getId())
                    .orElse(null);
            if (discipline != null) {
                labWork.setDiscipline(discipline);
            }
        }

        Coordinates coordinates = coordinatesRepository.findById(labWork.getCoordinates().getId())
                .orElse(null);
        if (coordinates == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        labWork.setCoordinates(coordinates);

        if (labWork.getAuthor() != null){
            Person person = personRepository.findById(labWork.getAuthor().getId())
                    .orElse(null);
            if (person != null) {
                labWork.setAuthor(person);
            }
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        labWork.setCreatedBy(user);

        LabWork savedLabWork = repository.save(labWork);
        entityManager.flush();
        entityManager.refresh(savedLabWork);
        auditService.logOperation("CREATE", user.getId(), "lab_work", savedLabWork.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLabWork);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<LabWork> getLabWorkById(Long id) {
        Optional<LabWork> labWork = repository.findById(id);
        if (labWork.isPresent()) {
            return ResponseEntity.ok(labWork.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Transactional
    public ResponseEntity<LabWork> updateLabWork(LabWork updatedLabWork) {
        boolean existingLabWork = repository.existsById(updatedLabWork.getId());
        if (!existingLabWork) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        LabWork labWork = repository.findById(updatedLabWork.getId()).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ((user.getRole().equals(Role.USER) && !labWork.getCreatedBy().equals(user)) ||
                (user.getRole().equals(Role.ADMIN) && !labWork.getUpdateable())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (updatedLabWork.getDiscipline() != null) {
            Discipline discipline = disciplineRepository.findById(updatedLabWork.getDiscipline().getId())
                    .orElse(null);
            if (discipline != null) {
                labWork.setDiscipline(discipline);
            } else {
                labWork.setDiscipline(null);
            }
        } else {
            labWork.setDiscipline(null);
        }

        Coordinates coordinates = coordinatesRepository.findById(labWork.getCoordinates().getId())
                .orElse(null);
        if (coordinates == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (updatedLabWork.getAuthor() != null){
            Person person = personRepository.findById(updatedLabWork.getAuthor().getId())
                    .orElse(null);
            if (person != null) {
                labWork.setAuthor(person);
            } else {
                labWork.setAuthor(null);
            }
        } else {
            labWork.setAuthor(null);
        }

        labWork.setName(updatedLabWork.getName());
        labWork.setCoordinates(coordinates);
        labWork.setDescription(updatedLabWork.getDescription());
        labWork.setMinimalPoint(updatedLabWork.getMinimalPoint());
        labWork.setDifficulty(updatedLabWork.getDifficulty());
        if (user.getRole().equals(Role.USER)) {
            labWork.setUpdateable(updatedLabWork.getUpdateable());
        }

        repository.save(labWork);
        auditService.logOperation("UPDATE", user.getId(), "lab_work", labWork.getId());
        return ResponseEntity.ok(labWork);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<LabWork>> getMyLabWorks() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(repository.findAllByCreatedBy(user));
    }

    @Transactional
    public ResponseEntity<String> deleteLabWork(Long id) {
        boolean existingLabWork = repository.existsById(id);
        if (!existingLabWork) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Laboratory work not found.");
        }

        LabWork labWork = repository.findById(id).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!labWork.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete this person.");
        }

        Long personId = labWork.getAuthor() != null ? labWork.getAuthor().getId() : null;
        Long coordinatesId = labWork.getCoordinates() != null ? labWork.getCoordinates().getId() : null;
        Long disciplineId = labWork.getDiscipline() != null ? labWork.getDiscipline().getId() : null;

        repository.delete(labWork);
        auditService.logOperation("DELETE", user.getId(), "lab_work", labWork.getId());

        if (personId != null) {
            unlinkLabWorksFromPerson(personId);
            personRepository.deleteById(personId);
        }

        if (coordinatesId != null) {
            unlinkLabWorksFromCoordinates(coordinatesId);
            coordinatesRepository.deleteById(coordinatesId);
        }

        if (disciplineId != null) {
            unlinkLabWorksFromDiscipline(disciplineId);
            disciplineRepository.deleteById(disciplineId);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Laboratory work deleted successfully.");
    }

    @Transactional
    public void unlinkLabWorksFromPerson(Long personId) {
        repository.updateLabWorksPersonToNull(personId);
    }

    @Transactional
    public void unlinkLabWorksFromCoordinates(Long coordinatesId) {
        repository.updateLabWorksCoordinatesToNull(coordinatesId);
    }

    @Transactional
    public void unlinkLabWorksFromDiscipline(Long disciplineID) {
        repository.updateLabWorksDisciplineToNull(disciplineID);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<LabWork>> getAllLabWorks(String nameContains, String descriptionContains, String sortBy, String direction, int page, int size) {
        if (page < 0 || size > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        if (direction == null || (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))) {
            direction = "asc";
        }

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        } else if (!isSortableField(sortBy)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        Specification<LabWork> specification = Specification.where(LabWorkSpecification.filterByNameContains(nameContains))
                .and(LabWorkSpecification.filterByDescriptionContains(descriptionContains));
        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(repository.findAll(specification, pageable));
    }

    private boolean isSortableField(String sortBy) {
        return List.of("id", "name", "description", "difficulty", "minimalPoint", "discipline", "createdAt", "author", "coordinates", "updateable").contains(sortBy);
    }

    private Sort getSort(String sortBy, String direction) {
        if (sortBy == null || direction == null) {
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}

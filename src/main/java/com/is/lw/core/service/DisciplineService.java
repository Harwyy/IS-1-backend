package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.core.model.Discipline;
import com.is.lw.core.repository.DisciplineRepository;
import com.is.lw.core.specification.DisciplineSpecification;
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
public class DisciplineService {

    private final DisciplineRepository repository;
    private final AuditLogService auditService;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<Discipline> createDiscipline(Discipline discipline) {
        if (discipline == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        discipline.setCreatedBy(user);
        Discipline savedDiscipline = repository.save(discipline);
        entityManager.flush();
        entityManager.refresh(savedDiscipline);
        auditService.logOperation("CREATE", user.getId(), "discipline", savedDiscipline.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDiscipline);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Discipline> getDisciplineById(Long id) {
        Optional<Discipline> discipline = repository.findById(id);
        if (discipline.isPresent()) {
            return ResponseEntity.ok(discipline.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Transactional
    public ResponseEntity<Discipline> updateDiscipline(Discipline updatedDiscipline) {
        boolean existingDiscipline = repository.existsById(updatedDiscipline.getId());
        if (!existingDiscipline) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Discipline discipline = repository.findById(updatedDiscipline.getId()).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ((user.getRole().equals(Role.USER) && !discipline.getCreatedBy().equals(user)) ||
                (user.getRole().equals(Role.ADMIN) && !discipline.getUpdateable())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        discipline.setName(updatedDiscipline.getName());
        discipline.setLectureHours(updatedDiscipline.getLectureHours());
        discipline.setPracticeHours(updatedDiscipline.getPracticeHours());
        discipline.setSelfStudyHours(updatedDiscipline.getSelfStudyHours());
        discipline.setLabsCount(updatedDiscipline.getLabsCount());

        if (user.getRole().equals(Role.USER)) {
            discipline.setUpdateable(updatedDiscipline.getUpdateable());
        }

        repository.save(discipline);
        auditService.logOperation("UPDATE", user.getId(), "discipline", discipline.getId());
        return ResponseEntity.ok(discipline);
    }

    @Transactional
    public ResponseEntity<String> deleteDiscipline(Long id) {
        boolean existingDiscipline = repository.existsById(id);
        if (!existingDiscipline) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Discipline not found.");
        }

        Discipline discipline = repository.findById(id).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!discipline.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete this discipline.");
        }

        repository.delete(discipline);
        auditService.logOperation("DELETE", user.getId(), "discipline", discipline.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Discipline deleted successfully.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Discipline>> getMyDisciplines() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(repository.findAllByCreatedBy(user));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Discipline>> getAllDisciplines(String nameContains, String sortBy, String direction, int page, int size) {
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

        Specification<Discipline> specification = Specification.where(DisciplineSpecification.filterByNameContains(nameContains));
        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(repository.findAll(specification, pageable));
    }

    private boolean isSortableField(String sortBy) {
        return List.of("id", "name", "lectureHours", "practiceHours", "selfStudyHours", "labsCount", "updateable").contains(sortBy);
    }

    private Sort getSort(String sortBy, String direction) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}

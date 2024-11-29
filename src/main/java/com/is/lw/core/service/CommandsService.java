package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.service.AuthService;
import com.is.lw.core.repository.DisciplineRepository;
import com.is.lw.core.repository.LabWorkRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandsService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final LabWorkRepository labWorkRepository;
    private final DisciplineRepository disciplineRepository;
    private final AuditLogService auditLogService;

    @Transactional()
    public ResponseEntity<String> deleteOneLabWorkByMinimalPoint(Integer minimalPoint) {
        if (minimalPoint == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Minimal point must not be null.");
        }
        long count = labWorkRepository.countByMinimalPoint(minimalPoint);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No laboratory work found with the given minimal point.");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            labWorkRepository.deleteOneByMinimalPoint(minimalPoint, user.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("One laboratory work deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting laboratory work.");
        }
    }

    public ResponseEntity<String> getCountByMinimalPoint(Integer minPoint) {
        if (minPoint == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MinimalPoint must not be null.");
        }
        try {
            Query query = entityManager.createNativeQuery("SELECT get_count_by_minimal_point(" + minPoint + ")");
            Object result = query.getSingleResult();
            if (result != null) {
                Integer count = (Integer) result;
                return ResponseEntity.status(HttpStatus.OK).body(count.toString());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No records found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while executing the query.");
        }
    }

    public ResponseEntity<String> getLabWorksByDescriptionSubstring(String substring) {
        if (substring == null || substring.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Substring must not be null or empty.");
        }
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT get_laboratory_works_by_description_substring_json('" + substring + "')");
            List<?> results = query.getResultList();

            if (results.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No lab works found with the given substring.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(results.get(0).toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving lab works.");
        }
    }

    @Transactional()
    public ResponseEntity<String> addLabWorkToDiscipline(Long labWorkId, Long disciplineId) {
        if (labWorkId == null || disciplineId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("LabWorkId and DisciplineId must not be null.");
        }
        boolean existsLabWork = labWorkRepository.existsById(labWorkId);
        boolean existsDiscipline = disciplineRepository.existsById(disciplineId);
        if (!existsLabWork) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("LabWork with ID " + labWorkId + " does not exist.");
        }
        if (!existsDiscipline) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Discipline with ID " + disciplineId + " does not exist.");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            labWorkRepository.addLabWorkToDiscipline(labWorkId, disciplineId, user.getId());
            auditLogService.logOperation("UPDATE", user.getId(), "lab_works", labWorkId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("LabWork with ID " + labWorkId + " successfully added to Discipline with ID " + disciplineId + ".");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the LabWork to the Discipline: " + e.getMessage());
        }
    }

    @Transactional()
    public ResponseEntity<String> deleteLabWorkToDiscipline(Long labWorkId) {
        if (labWorkId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("LabWorkId and DisciplineId must not be null.");
        }
        boolean existsLabWork = labWorkRepository.existsById(labWorkId);
        if (!existsLabWork) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("LabWork with ID " + labWorkId + " does not exist.");
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            labWorkRepository.deleteLabWorkToDiscipline(labWorkId, user.getId());
            auditLogService.logOperation("UPDATE", user.getId(), "lab_works", labWorkId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("LabWork with ID " + labWorkId + " successfully deleted from Discipline.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the LabWork to the Discipline: " + e.getMessage());
        }
    }

}

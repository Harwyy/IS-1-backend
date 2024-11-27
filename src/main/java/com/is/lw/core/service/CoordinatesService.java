package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.core.model.Coordinates;
import com.is.lw.core.repository.CoordinatesRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class CoordinatesService {

    private final CoordinatesRepository repository;
    private final AuditLogService auditService;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<Coordinates> createCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        coordinates.setCreatedBy(user);
        Coordinates savedCoordinates = repository.save(coordinates);
        entityManager.flush();
        entityManager.refresh(savedCoordinates);
        auditService.logOperation("CREATE", user.getId(), "coordinates", savedCoordinates.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCoordinates);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Coordinates> getCoordinatesById(Long id) {
        Optional<Coordinates> coordinates = repository.findById(id);
        if (coordinates.isPresent()) {
            return ResponseEntity.ok(coordinates.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @Transactional
    public ResponseEntity<Coordinates> updateCoordinates(Coordinates updatedCoordinates) {
        boolean existingCoordinates = repository.existsById(updatedCoordinates.getId());
        if (!existingCoordinates) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        Coordinates coordinates = repository.findById(updatedCoordinates.getId()).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ((user.getRole().equals(Role.USER) && !coordinates.getCreatedBy().equals(user)) ||
                (user.getRole().equals(Role.ADMIN) && !coordinates.getUpdateable())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        coordinates.setX(updatedCoordinates.getX());
        coordinates.setY(updatedCoordinates.getY());
        if (user.getRole().equals(Role.USER)) {
            coordinates.setUpdateable(updatedCoordinates.getUpdateable());
        }

        repository.save(coordinates);
        auditService.logOperation("UPDATE", user.getId(), "coordinates", coordinates.getId());
        return ResponseEntity.ok(coordinates);
    }

    @Transactional
    public ResponseEntity<String> deleteCoordinates(Long id) {
        boolean existingCoordinates = repository.existsById(id);
        if (!existingCoordinates) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Coordinates not found.");
        }

        Coordinates coordinates = repository.findById(id).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!coordinates.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete these coordinates.");
        }

        repository.delete(coordinates);
        auditService.logOperation("DELETE", user.getId(), "coordinates", coordinates.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Coordinates deleted successfully.");
    }
    
    @Transactional(readOnly = true)
    public ResponseEntity<List<Coordinates>> getMyCoordinates() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(repository.findAllByCreatedBy(user));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Coordinates>> getAllCoordinates(String sortBy, String direction, int page, int size) {
        if (page < 0 || size > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }

        if (direction == null || (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc"))) {
            direction = "asc";
        }

        System.out.println(sortBy);
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        } else if (!isSortableField(sortBy)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }

        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(repository.findAll(pageable).getContent());
    }

    private boolean isSortableField(String sortBy) {
        return List.of("id", "X", "Y", "updateable").contains(sortBy);
    }

    private Sort getSort(String sortBy, String direction) {
        if (sortBy == null || direction == null) {
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}

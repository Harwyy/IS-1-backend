package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.core.model.Location;
import com.is.lw.core.repository.LocationRepository;
import com.is.lw.core.repository.PersonRepository;
import com.is.lw.core.specification.LocationSpecification;
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

import java.util.List;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationRepository repository;
    private final PersonRepository personRepository;
    private final AuditLogService auditService;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<Location> createLocation(Location location) {
        if (location == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        location.setCreatedBy(user);
        Location savedLocation = repository.save(location);
        entityManager.flush();
        entityManager.refresh(savedLocation);
        auditService.logOperation("CREATE", user.getId(), "location", savedLocation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLocation);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Location> getLocationById(Long id) {
        Optional<Location> location = repository.findById(id);
        if (location.isPresent()) {
            return ResponseEntity.ok(location.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Transactional()
    public ResponseEntity<Location> updateLocation(Location updatedLocation) {
        boolean existingLocation = repository.existsById(updatedLocation.getId());
        if (!existingLocation) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Location location = repository.findById(updatedLocation.getId()).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ((user.getRole().equals(Role.USER) && !location.getCreatedBy().equals(user)) ||
                (user.getRole().equals(Role.ADMIN) && !location.getUpdateable())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        location.setName(updatedLocation.getName());
        location.setCoordinateX(updatedLocation.getCoordinateX());
        location.setCoordinateY(updatedLocation.getCoordinateY());
        location.setCoordinateZ(updatedLocation.getCoordinateZ());
        if (user.getRole().equals(Role.USER)) {
            location.setUpdateable(updatedLocation.getUpdateable());
        }
        repository.save(location);
        auditService.logOperation("UPDATE", user.getId(), "location", location.getId());
        return ResponseEntity.ok(location);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Location>> getMyLocations() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(repository.findAllByCreatedBy(user));
    }

    @Transactional()
    public ResponseEntity<String> deleteLocation(Long id) {
        boolean existingLocation = repository.existsById(id);
        if (!existingLocation) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found.");
        }

        Location location = repository.findById(id).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!location.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete this location.");
        }
        personRepository.updatePersonsLocationToNull(id);
        repository.delete(location);
        auditService.logOperation("DELETE", user.getId(), "location", location.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Location deleted successfully.");
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Location>> getAllLocations(String nameContains, String sortBy, String direction, int page, int size) {
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

        Specification<Location> specification = Specification.where(LocationSpecification.filterByNameContains(nameContains));
        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(repository.findAll(specification, pageable));
    }

    private boolean isSortableField(String sortBy) {
        return List.of("id", "name", "coordinateX", "coordinateY", "coordinateZ", "updateable").contains(sortBy);
    }

    private Sort getSort(String sortBy, String direction) {
        if (sortBy == null || direction == null) {
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}

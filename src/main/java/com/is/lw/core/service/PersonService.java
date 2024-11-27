package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.auth.model.enums.Role;
import com.is.lw.core.model.Location;
import com.is.lw.core.model.Person;
import com.is.lw.core.repository.LocationRepository;
import com.is.lw.core.repository.PersonRepository;
import com.is.lw.core.specification.PersonSpecification;
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
public class PersonService {

    private final PersonRepository repository;
    private final LocationRepository locationRepository;
    private final AuditLogService auditService;
    private final EntityManager entityManager;

    @Transactional
    public ResponseEntity<Person> createPerson(Person person) {
        if (person == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Location location = locationRepository.findById(person.getLocation().getId())
                .orElse(null);
        if (location == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        person.setLocation(location);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        person.setCreatedBy(user);

        Person savedPerson = repository.save(person);
        entityManager.flush();
        entityManager.refresh(savedPerson);
        auditService.logOperation("CREATE", user.getId(), "person", savedPerson.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Person> getPersonById(Long id) {
        Optional<Person> person = repository.findById(id);
        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Transactional
    public ResponseEntity<Person> updatePerson(Person updatedPerson) {
        boolean existingPerson = repository.existsById(updatedPerson.getId());
        if (!existingPerson) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Person person = repository.findById(updatedPerson.getId()).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ((user.getRole().equals(Role.USER) && !person.getCreatedBy().equals(user)) ||
                (user.getRole().equals(Role.ADMIN) && !person.getUpdateable())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Location location = locationRepository.findById(updatedPerson.getLocation().getId())
                .orElse(null);
        if (location == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        person.setName(updatedPerson.getName());
        person.setColor(updatedPerson.getColor());
        person.setHairColor(updatedPerson.getHairColor());
        person.setLocation(location);
        person.setWeight(updatedPerson.getWeight());
        person.setNationality(updatedPerson.getNationality());
        if (user.getRole().equals(Role.USER)) {
            person.setUpdateable(updatedPerson.getUpdateable());
        }

        repository.save(person);
        auditService.logOperation("UPDATE", user.getId(), "person", person.getId());
        return ResponseEntity.ok(person);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Person>> getMyPersons() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(repository.findAllByCreatedBy(user));
    }

    @Transactional
    public ResponseEntity<String> deletePerson(Long id) {
        boolean existingPerson = repository.existsById(id);
        if (!existingPerson) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }

        Person person = repository.findById(id).get();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!person.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete this person.");
        }

        Long locationId = person.getLocation() != null ? person.getLocation().getId() : null;

        repository.delete(person);
        auditService.logOperation("DELETE", user.getId(), "person", person.getId());

        if (locationId != null) {
            unlinkPersonsFromLocation(locationId);
            locationRepository.deleteById(locationId);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body("Person and associated location deleted successfully.");
    }

    @Transactional
    public void unlinkPersonsFromLocation(Long locationId) {
        repository.updatePersonsLocationToNull(locationId);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<Person>> getAllPersons(String nameContains, String sortBy, String direction, int page, int size) {
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

        Specification<Person> specification = Specification.where(PersonSpecification.filterByNameContains(nameContains));
        Sort sort = getSort(sortBy, direction);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(repository.findAll(specification, pageable));
    }

    private boolean isSortableField(String sortBy) {
        return List.of("id", "name", "color", "hairColor", "weight", "nationality", "updateable").contains(sortBy);
    }

    private Sort getSort(String sortBy, String direction) {
        if (sortBy == null || direction == null) {
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}

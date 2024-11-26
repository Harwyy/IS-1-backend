package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.core.controller.Request.PersonAddRequest;
import com.is.lw.core.controller.Request.PersonUpdateRequest;
import com.is.lw.core.controller.Response.MyResponse;
import com.is.lw.core.model.Location;
import com.is.lw.core.model.Person;
import com.is.lw.core.model.enums.Status;
import com.is.lw.core.repository.LocationRepository;
import com.is.lw.core.repository.PersonRepository;
import com.is.lw.core.specification.PersonSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository repository;
    private final LocationRepository locationRepository;

    public MyResponse addPerson(PersonAddRequest request) {
        Location location = null;

        if (request.getLocation() != null) {
            if (request.getLocation().getId() == null) {

                location = locationRepository.save(
                        Location.builder()
                                .name(request.getLocation().getName())
                                .x(request.getLocation().getX())
                                .y(request.getLocation().getY())
                                .z(request.getLocation().getZ())
                                .build()
                );
            } else {
                Long locationId = request.getLocation().getId();

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

        repository.save(
                Person.builder()
                        .name(request.getName())
                        .color(request.getColor())
                        .hairColor(request.getHairColor())
                        .location(location)
                        .nationality(request.getNationality())
                        .weight(request.getWeight())
                        .build()
        );

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    public MyResponse updatePerson(PersonUpdateRequest request) {

        if (!repository.existsById(request.getId())) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Person with id " + request.getId() + " not found.")
                    .build();
        }

        Person person = repository.findById(request.getId()).get();
        Location location = null;

        if (request.getLocation() != null) {
            Long locationId = request.getLocation().getId();
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

        person.setName(request.getName());
        person.setColor(request.getColor());
        person.setHairColor(request.getHairColor());
        person.setNationality(request.getNationality());
        person.setWeight(request.getWeight());
        person.setLocation(location);

        repository.save(person);

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    @Transactional
    public MyResponse deletePerson(Long id) {
        if (!repository.existsById(id)) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Person with id " + id + " not found.")
                    .build();
        }

        Person person = repository.findById(id).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getId().equals(person.getCreatedBy().getId())) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " is not authorized to delete person with id " + id + ".")
                    .build();
        }

        Long locationId = person.getLocation() != null ? person.getLocation().getId() : null;

        repository.delete(person);

        if (locationId != null) {
            unlinkPersonsFromLocation(locationId);
            locationRepository.deleteById(locationId);
        }

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Person with id " + id + " was deleted.")
                .build();
    }

    @Transactional
    public void unlinkPersonsFromLocation(Long locationId) {
        repository.updatePersonsLocationToNull(locationId);
    }

    public List<Person> filterAndSortPersons(
            String nameEquals,
            String nameContains,
            String colorEquals,
            String hairColorEquals,
            Long weightEquals,
            Long weightGreaterThan,
            Long weightLessThan,
            String nationalityEquals,
            Long locationIdEquals,
            Long idEquals,
            String sortBy,
            String direction,
            int page,
            int size) {

        Specification<Person> specification = Specification
                .where(PersonSpecification.filterByNameEquals(nameEquals))
                .and(PersonSpecification.filterByNameContains(nameContains))
                .and(PersonSpecification.filterByColorEquals(colorEquals))
                .and(PersonSpecification.filterByHairColorEquals(hairColorEquals))
                .and(PersonSpecification.filterByWeightEquals(weightEquals))
                .and(PersonSpecification.filterByWeightGreaterThan(weightGreaterThan))
                .and(PersonSpecification.filterByWeightLessThan(weightLessThan))
                .and(PersonSpecification.filterByNationalityEquals(nationalityEquals))
                .and(PersonSpecification.filterByLocationIdEquals(locationIdEquals))
                .and(PersonSpecification.filterByIdEquals(idEquals));

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

package com.is.lw.core.service;

import com.is.lw.auth.model.User;
import com.is.lw.core.controller.Request.LocationAddRequest;
import com.is.lw.core.controller.Request.LocationUpdateRequest;
import com.is.lw.core.controller.Response.MyResponse;
import com.is.lw.core.model.Location;
import com.is.lw.core.model.enums.Status;
import com.is.lw.core.repository.LocationRepository;
import com.is.lw.core.specification.LocationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationService {

    private final LocationRepository repository;

    public MyResponse addLocation(LocationAddRequest request) {

        repository.save(Location.builder()
                .name(request.getName())
                .x(request.getX())
                .y(request.getY())
                .z(request.getZ())
                .build());
        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();

    }

    public MyResponse updateLocation(LocationUpdateRequest request) {

        if (!repository.existsById(request.getId())) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Location with id " + request.getId() + " not found.")
                    .build();
        }

        Location location = repository.findById(request.getId()).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != location.getCreatedBy().getId()) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " can`t update location.")
                    .build();
        }

        location.setName(request.getName());
        location.setX(request.getX());
        location.setY(request.getY());
        location.setZ(request.getZ());
        repository.save(location);

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Location with id " + request.getId() + " was updated.")
                .build();

    }

    public MyResponse deleteLocation(Long id) {

        if (!repository.existsById(id)) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Location with id " + id + " not found.")
                    .build();
        }

        Location location = repository.findById(id).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != location.getCreatedBy().getId()) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " can`t delete location with id " + id + ".")
                    .build();
        }

        repository.deleteById(id);
        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Location with id " + id + " was deleted.")
                .build();

    }

    public List<Location> filterAndSortLocations(
            String nameEquals,
            String nameContains,
            Long xEquals,
            Long xGreaterThan,
            Long xLessThan,
            Double yEquals,
            Double yGreaterThan,
            Double yLessThan,
            Integer zEquals,
            Integer zGreaterThan,
            Integer zLessThan,
            String sortBy,
            String direction,
            int page,
            int size) {

        Specification<Location> specification = Specification
                .where(LocationSpecification.filterByNameEquals(nameEquals))
                .and(LocationSpecification.filterByNameContains(nameContains))
                .and(LocationSpecification.filterByXEquals(xEquals))
                .and(LocationSpecification.filterByXGreaterThan(xGreaterThan))
                .and(LocationSpecification.filterByXLessThan(xLessThan))
                .and(LocationSpecification.filterByYEquals(yEquals))
                .and(LocationSpecification.filterByYGreaterThan(yGreaterThan))
                .and(LocationSpecification.filterByYLessThan(yLessThan))
                .and(LocationSpecification.filterByZEquals(zEquals))
                .and(LocationSpecification.filterByZGreaterThan(zGreaterThan))
                .and(LocationSpecification.filterByZLessThan(zLessThan));

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

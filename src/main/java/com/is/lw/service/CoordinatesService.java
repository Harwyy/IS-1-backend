package com.is.lw.service;

import com.is.lw.auth.model.User;
import com.is.lw.controller.Request.CoordinatesAddRequest;
import com.is.lw.controller.Request.CoordinatesUpdateRequest;
import com.is.lw.controller.Response.MyResponse;
import com.is.lw.model.Coordinates;
import com.is.lw.model.enums.Status;
import com.is.lw.repository.CoordinatesRepository;
import com.is.lw.specification.CoordinatesSpecification;
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
public class CoordinatesService {

    private final CoordinatesRepository repository;

    public MyResponse addCoordinates(CoordinatesAddRequest request) {

        repository.save(Coordinates.builder()
                .x(request.getX())
                .y(request.getY())
                .z(request.getZ())
                .build());
        return MyResponse.builder()
                .status(Status.SUCCESS)
                .build();

    }

    public MyResponse updateCoordinates(CoordinatesUpdateRequest request) {

        if (!repository.existsById(request.getId())) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Coordinates with id " + request.getId() + " not found.")
                    .build();
        }

        Coordinates coordinates = repository.findById(request.getId()).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != coordinates.getCreatedBy().getId()) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " can`t update coordinates.")
                    .build();
        }

        coordinates.setX(request.getX());
        coordinates.setY(request.getY());
        coordinates.setZ(request.getZ());
        repository.save(coordinates);

        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Coordinates with id " + request.getId() + " was updated.")
                .build();

    }

    public MyResponse deleteCoordinates(Long id) {

        if (!repository.existsById(id)) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("Coordinates with id " + id + " not found.")
                    .build();
        }

        Coordinates coordinates = repository.findById(id).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId() != coordinates.getCreatedBy().getId()) {
            return MyResponse.builder()
                    .status(Status.FAIL)
                    .message("User with id " + user.getId() + " can`t delete coordinates with id " + id + ".")
                    .build();
        }

        repository.deleteById(id);
        return MyResponse.builder()
                .status(Status.SUCCESS)
                .message("Coordinates with id " + id + " was deleted.")
                .build();

    }

    public List<Coordinates> filterAndSortCoordinates(Float xEquals,
                                                      Float xGreaterThan,
                                                      Float xLessThan,
                                                      Long yEquals,
                                                      Long yGreaterThan,
                                                      Long yLessThan,
                                                      Integer zEquals,
                                                      Integer zGreaterThan,
                                                      Integer zLessThan,
                                                      Long idEquals,
                                                      String sortBy,
                                                      String direction,
                                                      int page,
                                                      int size)
    {
        Specification<Coordinates> specification = Specification
                .where(CoordinatesSpecification.filterByXEquals(xEquals))
                .and(CoordinatesSpecification.filterByXGreaterThan(xGreaterThan))
                .and(CoordinatesSpecification.filterByXLessThan(xLessThan))
                .and(CoordinatesSpecification.filterByYEquals(yEquals))
                .and(CoordinatesSpecification.filterByYGreaterThan(yGreaterThan))
                .and(CoordinatesSpecification.filterByYLessThan(yLessThan))
                .and(CoordinatesSpecification.filterByZEquals(zEquals))
                .and(CoordinatesSpecification.filterByZGreaterThan(zGreaterThan))
                .and(CoordinatesSpecification.filterByZLessThan(zLessThan))
                .and(CoordinatesSpecification.filterByIdEquals(idEquals));

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

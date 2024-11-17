package com.is.lw.service;

import com.is.lw.controller.Request.CoordinatesAddRequest;
import com.is.lw.controller.Request.CoordinatesUpdateRequest;
import com.is.lw.controller.Response.CoordinatesAddOrUpdateResponse;
import com.is.lw.model.Coordinates;
import com.is.lw.model.enums.Status;
import com.is.lw.repository.CoordinatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CoordinatesService {

    private final CoordinatesRepository repository;

    public CoordinatesAddOrUpdateResponse addCoordinates(CoordinatesAddRequest request) {
        repository.save(Coordinates.builder()
                .x(request.getX())
                .y(request.getY())
                .z(request.getZ())
                .build());
        return CoordinatesAddOrUpdateResponse.builder()
                .status(Status.SUCCESS)
                .build();
    }

    public CoordinatesAddOrUpdateResponse updateCoordinates(CoordinatesUpdateRequest request) {
        Coordinates coordinates = repository.findById(request.getId()).orElseThrow(
                () -> new RuntimeException("Coordinates not found with id: " + request.getId())
        );
        coordinates.setX(request.getX());
        coordinates.setY(request.getY());
        coordinates.setZ(request.getZ());
        repository.save(coordinates);
        return CoordinatesAddOrUpdateResponse.builder()
                .status(Status.SUCCESS)
                .message("Coordinates with id " + request.getId() + " was updated.")
                .build();
    }
}

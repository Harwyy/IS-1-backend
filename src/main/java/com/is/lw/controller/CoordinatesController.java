package com.is.lw.controller;

import com.is.lw.controller.Request.CoordinatesAddRequest;
import com.is.lw.controller.Request.CoordinatesUpdateRequest;
import com.is.lw.controller.Response.CoordinatesAddOrUpdateResponse;
import com.is.lw.service.CoordinatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class CoordinatesController {

    private final CoordinatesService service;

    @PostMapping("/add-coordinates")
    public ResponseEntity<CoordinatesAddOrUpdateResponse> addCoordinates(
            @RequestBody CoordinatesAddRequest request
    ) {
        return ResponseEntity.ok(service.addCoordinates(request));
    }

    @PostMapping("/update-coordinates")
     public ResponseEntity<CoordinatesAddOrUpdateResponse> updateCoordinates(
             @RequestBody CoordinatesUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateCoordinates(request));
    }

}

package com.is.lw.controller;

import com.is.lw.model.Coordinates;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class test {

    @PostMapping("/test")
    public String validateDifficulty(@Valid @RequestBody Coordinates Coordinates){
        return "OK";
    }

    @GetMapping("/aboba")
    public String aboba(){
        return "aboba";
    }

}

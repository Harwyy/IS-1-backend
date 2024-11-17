package com.is.lw.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineUpdateRequest {

    @JsonProperty("id")
    Long id;
    @JsonProperty("name")
    String name;
    @JsonProperty("lecturehours")
    Long lectureHours;
    @JsonProperty("practicehours")
    int practiceHours;
    @JsonProperty("selfstudyhours")
    Integer selfStudyHours;
    @JsonProperty("labscount")
    long labsCount;

}

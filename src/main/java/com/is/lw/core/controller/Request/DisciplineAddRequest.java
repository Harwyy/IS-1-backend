package com.is.lw.core.controller.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineAddRequest {

    @JsonProperty("name")
    String name;
    @JsonProperty("lectureHours")
    Long lectureHours;
    @JsonProperty("practiceHours")
    int practiceHours;
    @JsonProperty("selfStudyHours")
    Integer selfStudyHours;
    @JsonProperty("labsCount")
    long labsCount;

}

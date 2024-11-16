package com.is.lw.auth.controller.response;

import com.is.lw.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WaitingForApproveResponse {

    private Status status;
    public List<String> list;
    public Integer length;
    public String message;

}

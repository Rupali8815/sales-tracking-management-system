package com.company.salestracker.dto.request;

import com.company.salestracker.enums.LeadStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeadRequest {

    @NotBlank
    private String name;

    private String email;
    private String phone;
    private String source;
    private LeadStatus status;
    private Integer assignedTo;
}

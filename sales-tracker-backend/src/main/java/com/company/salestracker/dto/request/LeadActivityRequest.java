package com.company.salestracker.dto.request;

import com.company.salestracker.enums.ActivityType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LeadActivityRequest {

    @NotNull(message = "activityType is required (CALL, EMAIL, MEETING, NOTE, STATUS_CHANGE)")
    private ActivityType activityType;

    @Size(max = 1000)
    private String notes;
}

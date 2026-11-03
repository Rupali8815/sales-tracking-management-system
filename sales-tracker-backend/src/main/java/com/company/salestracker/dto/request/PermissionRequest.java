package com.company.salestracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PermissionRequest {

    @NotBlank(message = "Permission code is required")
    @Size(max = 100)
    private String permissionCode;

    @Size(max = 255)
    private String description;
}

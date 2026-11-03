package com.company.salestracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PermissionResponse {
    private Integer id;
    private String permissionCode;
    private String description;
}

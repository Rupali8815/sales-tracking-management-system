package com.company.salestracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class RoleResponse {
    private Integer id;
    private String roleName;
    private String description;
    private Set<String> permissions;
    private LocalDateTime createdAt;
}

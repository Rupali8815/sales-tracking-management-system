package com.company.salestracker.dto.response;

import com.company.salestracker.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private UserStatus status;
    private Set<String> roles;
    private LocalDateTime createdAt;
}

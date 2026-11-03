package com.company.salestracker.mapper;

import com.company.salestracker.dto.request.UserRequest;
import com.company.salestracker.dto.response.UserResponse;
import com.company.salestracker.entity.Role;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.UserStatus;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /** DTO -> Entity, for creating a brand-new User. */
    public User toEntity(UserRequest request, String encodedPassword, Set<Role> roles) {
        if (request == null) return null;

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phone(request.getPhone())
                .status(request.getStatus() != null ? request.getStatus() : UserStatus.ACTIVE)
                .roles(roles)
                .build();
    }

    /**
     * DTO -> Entity, applied in-place onto an existing User for updates.
     * Pass {@code null} for encodedPassword/roles to leave those fields untouched.
     */
    public void updateEntity(User user, UserRequest request, String encodedPassword, Set<Role> roles) {
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
        if (encodedPassword != null) {
            user.setPassword(encodedPassword);
        }
        if (roles != null) {
            user.setRoles(roles);
        }
    }

    /** Entity -> DTO, for API responses. */
    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .build();
    }
}

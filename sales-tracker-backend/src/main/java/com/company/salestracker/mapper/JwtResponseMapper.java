package com.company.salestracker.mapper;

import com.company.salestracker.dto.response.JwtResponse;
import com.company.salestracker.entity.Role;
import com.company.salestracker.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtResponseMapper {

    public JwtResponse toResponse(User user, String accessToken, String refreshToken) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(roleNames)
                .build();
    }
}

package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.security.TokenRevocationService;
import com.company.salestracker.dto.request.LoginRequest;
import com.company.salestracker.dto.response.JwtResponse;
import com.company.salestracker.entity.User;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.exception.UnauthorizedException;
import com.company.salestracker.mapper.JwtResponseMapper;
import com.company.salestracker.repository.UserRepository;
import com.company.salestracker.security.JwtTokenProvider;
import com.company.salestracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JwtResponseMapper jwtResponseMapper;
    private final TokenRevocationService tokenRevocationService;
    private final AuditService auditService;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        auditService.logAs(user, AuditAction.LOGIN, "User", user.getId());

        return jwtResponseMapper.toResponse(user, accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        if (tokenRevocationService.isRevoked(email, jwtTokenProvider.getIssuedAt(refreshToken))) {
            throw new UnauthorizedException("Session ended. Please log in again");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), null,
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName()))
                        .collect(Collectors.toList()));

        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        return jwtResponseMapper.toResponse(user, newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String email) {
           tokenRevocationService.revokeAllFor(email);
        userRepository.findByEmail(email)
                .ifPresent(u -> auditService.logAs(u, AuditAction.LOGOUT, "User", u.getId()));
    }
}

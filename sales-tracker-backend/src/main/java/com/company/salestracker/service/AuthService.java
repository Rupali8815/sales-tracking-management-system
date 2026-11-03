package com.company.salestracker.service;

import com.company.salestracker.dto.request.LoginRequest;
import com.company.salestracker.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    JwtResponse refreshToken(String refreshToken);
    void logout(String email);
}

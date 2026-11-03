package com.company.salestracker.service;

import com.company.salestracker.dto.request.UserRequest;
import com.company.salestracker.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Integer id, UserRequest request);
    void deactivateUser(Integer id);
    UserResponse getUser(Integer id);
    Page<UserResponse> listUsers(Pageable pageable);
}

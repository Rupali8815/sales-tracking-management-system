package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.dto.request.UserRequest;
import com.company.salestracker.dto.response.UserResponse;
import com.company.salestracker.entity.Role;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.UserStatus;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.mapper.UserMapper;
import com.company.salestracker.repository.RoleRepository;
import com.company.salestracker.repository.UserRepository;
import com.company.salestracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuditService auditService;

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("A user with this email already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Set<Role> roles = resolveRoles(request.getRoleIds());

        User user = userMapper.toEntity(request, encodedPassword, roles);

        User saved = userRepository.save(user);
        auditService.log(AuditAction.CREATE, "User", saved.getId());
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));

        String encodedPassword = (request.getPassword() != null && !request.getPassword().isBlank())
                ? passwordEncoder.encode(request.getPassword())
                : null;
        Set<Role> roles = request.getRoleIds() != null ? resolveRoles(request.getRoleIds()) : null;

        userMapper.updateEntity(user, request, encodedPassword, roles);

        User saved = userRepository.save(user);
        auditService.log(AuditAction.UPDATE, "User", saved.getId());
        return userMapper.toResponse(saved);
    }

    @Override
    public void deactivateUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
        auditService.log(AuditAction.DELETE, "User", user.getId());
    }

    @Override
    public UserResponse getUser(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Override
    public Page<UserResponse> listUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    private Set<Role> resolveRoles(Set<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
}

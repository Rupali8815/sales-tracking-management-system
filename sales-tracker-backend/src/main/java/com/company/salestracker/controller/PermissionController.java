package com.company.salestracker.controller;

import com.company.salestracker.dto.request.PermissionRequest;
import com.company.salestracker.dto.response.PermissionResponse;
import jakarta.validation.Valid;
import com.company.salestracker.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_MANAGE')")
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> create(@Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.createPermission(request.getPermissionCode().trim().toUpperCase(), request.getDescription()));
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> list() {
        return ResponseEntity.ok(permissionService.listPermissions());
    }
}

package com.company.salestracker.controller;

import com.company.salestracker.dto.request.RolePermissionsRequest;
import com.company.salestracker.dto.request.RoleRequest;
import com.company.salestracker.dto.response.RoleResponse;
import jakarta.validation.Valid;
import com.company.salestracker.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_MANAGE')")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request.getRoleName().trim(), request.getDescription()));
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<RoleResponse> assignPermissions(@PathVariable Integer id, @Valid @RequestBody RolePermissionsRequest request) {
        return ResponseEntity.ok(roleService.assignPermissions(id, request.getPermissionIds()));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> list() {
        return ResponseEntity.ok(roleService.listRoles());
    }
}

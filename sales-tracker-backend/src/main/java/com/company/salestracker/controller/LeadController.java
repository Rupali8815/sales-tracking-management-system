package com.company.salestracker.controller;

import com.company.salestracker.dto.request.LeadActivityRequest;
import com.company.salestracker.dto.request.LeadRequest;
import com.company.salestracker.dto.response.LeadResponse;
import com.company.salestracker.service.LeadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @PostMapping
    @PreAuthorize("hasAuthority('LEAD_CREATE')")
    public ResponseEntity<LeadResponse> create(@Valid @RequestBody LeadRequest request) {
        return ResponseEntity.ok(leadService.createLead(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAD_UPDATE')")
    public ResponseEntity<LeadResponse> update(@PathVariable Integer id, @Valid @RequestBody LeadRequest request) {
        return ResponseEntity.ok(leadService.updateLead(id, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LEAD_READ')")
    public ResponseEntity<LeadResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(leadService.getLead(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('LEAD_READ')")
    public ResponseEntity<Page<LeadResponse>> list(@RequestParam(required = false) String status, Pageable pageable) {
        return ResponseEntity.ok(leadService.listLeads(status, pageable));
    }

    @GetMapping("/{id}/activities")
    @PreAuthorize("hasAuthority('LEAD_READ')")
    public ResponseEntity<java.util.List<com.company.salestracker.dto.response.LeadActivityResponse>> activities(@PathVariable Integer id) {
        return ResponseEntity.ok(leadService.getActivities(id));
    }

    @PostMapping("/{id}/activities")
    @PreAuthorize("hasAuthority('LEAD_UPDATE')")
    public ResponseEntity<Void> addActivity(@PathVariable Integer id,
                                             @Valid @RequestBody LeadActivityRequest request,
                                             Authentication authentication) {
        // NOTE: createdByUserId resolution from Authentication principal should be wired
        // to the authenticated user's numeric ID via a lookup in a real implementation.
        leadService.addActivity(id, request.getActivityType().name(), request.getNotes(), null);
        return ResponseEntity.noContent().build();
    }
}

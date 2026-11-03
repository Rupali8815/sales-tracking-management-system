package com.company.salestracker.controller;

import com.company.salestracker.dto.request.DealRequest;
import com.company.salestracker.dto.response.DealResponse;
import com.company.salestracker.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping
    @PreAuthorize("hasAuthority('DEAL_CREATE')")
    public ResponseEntity<DealResponse> convertLeadToDeal(@RequestBody DealRequest request) {
        return ResponseEntity.ok(dealService.convertLeadToDeal(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DEAL_UPDATE')")
    public ResponseEntity<DealResponse> update(@PathVariable Integer id, @RequestBody DealRequest request) {
        return ResponseEntity.ok(dealService.updateDeal(id, request));
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasAuthority('DEAL_UPDATE')")
    public ResponseEntity<DealResponse> close(@PathVariable Integer id, @RequestParam String outcome) {
        return ResponseEntity.ok(dealService.closeDeal(id, outcome));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('DEAL_READ')")
    public ResponseEntity<Page<DealResponse>> list(@RequestParam(required = false) String stage, Pageable pageable) {
        return ResponseEntity.ok(dealService.listDeals(stage, pageable));
    }
}

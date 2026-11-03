package com.company.salestracker.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.salestracker.dto.request.TargetRequest;
import com.company.salestracker.dto.response.TargetResponse;
import com.company.salestracker.service.TargetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/targets")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('TARGET_MANAGE')")
public class TargetController {

    private final TargetService targetService;

    @PostMapping
    public ResponseEntity<TargetResponse> assign(@Valid @RequestBody TargetRequest request) {
        return ResponseEntity.ok(targetService.assignTarget(
                request.getUserId(), request.getMonth(), request.getYear(), request.getAmount()));
    }

    @GetMapping
    public ResponseEntity<List<TargetResponse>> list(@RequestParam Integer userId, @RequestParam int year) {
        return ResponseEntity.ok(targetService.getUserTargets(userId, year));
    }

    @GetMapping("/achievement")
    public ResponseEntity<BigDecimal> achievement(@RequestParam Integer userId,
                                                   @RequestParam int month,
                                                   @RequestParam int year) {
        return ResponseEntity.ok(targetService.calculateAchievementPercentage(userId, month, year));
    }
}

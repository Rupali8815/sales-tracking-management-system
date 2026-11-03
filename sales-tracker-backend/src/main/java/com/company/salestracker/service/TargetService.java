package com.company.salestracker.service;

import com.company.salestracker.dto.response.TargetResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TargetService {
    TargetResponse assignTarget(Integer userId, int month, int year, BigDecimal amount);
    List<TargetResponse> getUserTargets(Integer userId, int year);
    BigDecimal calculateAchievementPercentage(Integer userId, int month, int year);
}

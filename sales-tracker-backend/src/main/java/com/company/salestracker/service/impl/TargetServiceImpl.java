package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.dto.response.TargetResponse;
import com.company.salestracker.entity.Target;
import com.company.salestracker.entity.User;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.mapper.TargetMapper;
import com.company.salestracker.repository.SalesRepository;
import com.company.salestracker.repository.TargetRepository;
import com.company.salestracker.repository.UserRepository;
import com.company.salestracker.service.TargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TargetServiceImpl implements TargetService {

    private final TargetRepository targetRepository;
    private final UserRepository userRepository;
    private final SalesRepository salesRepository;
    private final TargetMapper targetMapper;
    private final AuditService auditService;

    @Override
    public TargetResponse assignTarget(Integer userId, int month, int year, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        Target target = targetRepository.findByUserIdAndTargetMonthAndTargetYear(userId, month, year)
                .orElseGet(() -> targetMapper.toEntity(user, month, year, amount));

        target.setTargetAmount(amount);

        Target saved = targetRepository.save(target);
        auditService.log(AuditAction.UPDATE, "Target", saved.getId());
        return targetMapper.toResponse(saved);
    }

    @Override
    public List<TargetResponse> getUserTargets(Integer userId, int year) {
        return targetRepository.findByUserIdAndTargetYear(userId, year).stream()
                .map(targetMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateAchievementPercentage(Integer userId, int month, int year) {
        Target target = targetRepository.findByUserIdAndTargetMonthAndTargetYear(userId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("No target set for this period"));

        BigDecimal achieved = salesRepository.findByAssignedUser(userId).stream()
                .filter(sale -> sale.getSaleDate().getMonthValue() == month && sale.getSaleDate().getYear() == year)
                .map(sale -> sale.getSaleAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (target.getTargetAmount().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return achieved.divide(target.getTargetAmount(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

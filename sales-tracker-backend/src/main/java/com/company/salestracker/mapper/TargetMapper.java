package com.company.salestracker.mapper;

import com.company.salestracker.dto.response.TargetResponse;
import com.company.salestracker.entity.Target;
import com.company.salestracker.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TargetMapper {

    /** Fields -> Entity, for assigning a brand-new Target. */
    public Target toEntity(User user, int month, int year, BigDecimal amount) {
        return Target.builder()
                .user(user)
                .targetMonth(month)
                .targetYear(year)
                .targetAmount(amount)
                .build();
    }

    /** Entity -> DTO, for API responses. */
    public TargetResponse toResponse(Target target) {
        if (target == null) return null;

        return TargetResponse.builder()
                .id(target.getId())
                .userId(target.getUser().getId())
                .userName(target.getUser().getName())
                .targetMonth(target.getTargetMonth())
                .targetYear(target.getTargetYear())
                .targetAmount(target.getTargetAmount())
                .build();
    }
}

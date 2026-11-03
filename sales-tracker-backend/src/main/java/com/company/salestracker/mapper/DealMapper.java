package com.company.salestracker.mapper;

import com.company.salestracker.dto.request.DealRequest;
import com.company.salestracker.dto.response.DealResponse;
import com.company.salestracker.entity.Deal;
import com.company.salestracker.entity.Lead;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.DealStage;
import org.springframework.stereotype.Component;

@Component
public class DealMapper {

    /** DTO -> Entity, for creating a brand-new Deal (converting a Lead). */
    public Deal toEntity(DealRequest request, Lead lead, User assignedTo) {
        if (request == null) return null;

        return Deal.builder()
                .lead(lead)
                .dealStage(request.getDealStage() != null ? request.getDealStage() : DealStage.NEGOTIATION)
                .expectedAmount(request.getExpectedAmount())
                .closingDate(request.getClosingDate())
                .assignedTo(assignedTo)
                .build();
    }

    /** DTO -> Entity, applied in-place onto an existing Deal for updates. */
    public void updateEntity(Deal deal, DealRequest request, User assignedTo) {
        if (request.getDealStage() != null) deal.setDealStage(request.getDealStage());
        if (request.getExpectedAmount() != null) deal.setExpectedAmount(request.getExpectedAmount());
        if (request.getClosingDate() != null) deal.setClosingDate(request.getClosingDate());
        if (assignedTo != null) deal.setAssignedTo(assignedTo);
    }

    /** Entity -> DTO, for API responses. */
    public DealResponse toResponse(Deal deal) {
        if (deal == null) return null;

        return DealResponse.builder()
                .id(deal.getId())
                .leadId(deal.getLead().getId())
                .leadName(deal.getLead().getName())
                .dealStage(deal.getDealStage())
                .expectedAmount(deal.getExpectedAmount())
                .closingDate(deal.getClosingDate())
                .assignedTo(deal.getAssignedTo() != null ? deal.getAssignedTo().getId() : null)
                .assignedToName(deal.getAssignedTo() != null ? deal.getAssignedTo().getName() : null)
                .createdAt(deal.getCreatedAt())
                .build();
    }
}

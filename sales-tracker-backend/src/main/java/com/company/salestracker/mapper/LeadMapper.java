package com.company.salestracker.mapper;

import com.company.salestracker.dto.request.LeadRequest;
import com.company.salestracker.dto.response.LeadResponse;
import com.company.salestracker.entity.Lead;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.LeadStatus;
import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

    /** DTO -> Entity, for creating a brand-new Lead. */
    public Lead toEntity(LeadRequest request, User assignedTo) {
        if (request == null) return null;

        return Lead.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .source(request.getSource())
                .status(request.getStatus() != null ? request.getStatus() : LeadStatus.NEW)
                .assignedTo(assignedTo)
                .build();
    }

    /** DTO -> Entity, applied in-place onto an existing Lead for updates. */
    public void updateEntity(Lead lead, LeadRequest request, User assignedTo) {
        lead.setName(request.getName());
        lead.setEmail(request.getEmail());
        lead.setPhone(request.getPhone());
        lead.setSource(request.getSource());
        if (request.getStatus() != null) {
            lead.setStatus(request.getStatus());
        }
        if (assignedTo != null) {
            lead.setAssignedTo(assignedTo);
        }
    }

    /** Entity -> DTO, for API responses. */
    public LeadResponse toResponse(Lead lead) {
        if (lead == null) return null;

        return LeadResponse.builder()
                .id(lead.getId())
                .name(lead.getName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .source(lead.getSource())
                .status(lead.getStatus())
                .assignedTo(lead.getAssignedTo() != null ? lead.getAssignedTo().getId() : null)
                .assignedToName(lead.getAssignedTo() != null ? lead.getAssignedTo().getName() : null)
                .createdAt(lead.getCreatedAt())
                .updatedAt(lead.getUpdatedAt())
                .build();
    }
}

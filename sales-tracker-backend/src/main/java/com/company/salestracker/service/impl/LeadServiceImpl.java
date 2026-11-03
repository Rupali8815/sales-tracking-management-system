package com.company.salestracker.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.salestracker.dto.request.LeadRequest;
import com.company.salestracker.dto.response.LeadActivityResponse;
import com.company.salestracker.dto.response.LeadResponse;
import com.company.salestracker.entity.Lead;
import com.company.salestracker.entity.LeadActivity;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.ActivityType;
import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.enums.LeadStatus;
import com.company.salestracker.exception.ForbiddenOperationException;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.mapper.LeadMapper;
import com.company.salestracker.repository.LeadActivityRepository;
import com.company.salestracker.repository.LeadRepository;
import com.company.salestracker.repository.UserRepository;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.service.LeadService;
import com.company.salestracker.util.CurrentUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final LeadActivityRepository leadActivityRepository;
    private final UserRepository userRepository;
    private final LeadMapper leadMapper;
    private final CurrentUser currentUser;
    private final AuditService auditService;

    @Override
    public LeadResponse createLead(LeadRequest request) {
    	Integer ownerId = currentUser.isOwnerScoped() ? currentUser.getId() : request.getAssignedTo();
        User assignedTo = resolveUser(ownerId);
        Lead lead = leadMapper.toEntity(request, assignedTo);

        Lead saved = leadRepository.save(lead);
        auditService.log(AuditAction.CREATE, "Lead", saved.getId());
        return toResponse(saved);
    }

    @Override
    public LeadResponse updateLead(Integer id, LeadRequest request) {
        Lead lead =  leadRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + id));
        
        if (!canModify(lead)) {
            throw new ForbiddenOperationException("You can only change leads assigned to you");
        }

        User assignedTo = null;
        if (request.getAssignedTo() != null) {
        	Integer currentOwner = lead.getAssignedTo() != null ? lead.getAssignedTo().getId() : null;
            boolean changing = !request.getAssignedTo().equals(currentOwner);
            if (changing && !currentUser.isAdminOrManager()) {
                throw new ForbiddenOperationException("Only a Manager or Admin can re-assign a lead");
            }
            if (changing) assignedTo = resolveUser(request.getAssignedTo());
        }
        leadMapper.updateEntity(lead, request, assignedTo);

        Lead saved = leadRepository.save(lead);
        auditService.log(AuditAction.UPDATE, "Lead", saved.getId());
        return toResponse(saved);
    }

    @Override
    public LeadResponse getLead(Integer id) {

    	 Lead lead =  leadRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + id));
    
        assertCanView(lead);
        return toResponse(lead);
    }

    @Override
    public Page<LeadResponse> listLeads(String status, Pageable pageable) {
        boolean hasStatus = status != null && !status.isBlank();
        Page<Lead> page;
        if (currentUser.isOwnerScoped()) {
        	Integer me = currentUser.getId();
            page = hasStatus
                    ? leadRepository.findByStatusAndAssignedToId(LeadStatus.valueOf(status), me, pageable)
                    : leadRepository.findByAssignedToId(me, pageable);
        } else {
            page = hasStatus
                    ? leadRepository.findByStatus(LeadStatus.valueOf(status), pageable)
                    : leadRepository.findAll(pageable);
        }
        return page.map(this::toResponse);
    }

    @Override
    public void addActivity(Integer leadId, String activityType, String notes, Integer createdByUserId) {
    
    	 Lead lead =  leadRepository.findById(leadId)
                 .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + leadId));
    
    	  if (!canModify(lead)) {
              throw new ForbiddenOperationException("You can only change leads assigned to you");
          }

        Integer creatorId = createdByUserId != null ? createdByUserId : currentUser.getId();
        User createdBy = resolveUser(creatorId);

        LeadActivity activity = LeadActivity.builder()
                .lead(lead)
                .activityType(ActivityType.valueOf(activityType))
                .notes(notes)
                .createdBy(createdBy)
                .build();

        leadActivityRepository.save(activity);
        auditService.log(AuditAction.UPDATE, "Lead", leadId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadActivityResponse> getActivities(Integer leadId) {

    	 Lead lead =  leadRepository.findById(leadId)
                 .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + leadId));
    
        assertCanView(lead);

        return leadActivityRepository.findByLeadIdOrderByCreatedAtDesc(leadId).stream()
                .map(a -> LeadActivityResponse.builder()
                        .id(a.getId())
                        .activityType(a.getActivityType())
                        .notes(a.getNotes())
                        .createdByName(a.getCreatedBy() != null ? a.getCreatedBy().getName() : null)
                        .createdAt(a.getCreatedAt())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }

   

   private boolean isOwner(Lead lead) {
        return lead.getAssignedTo() != null && lead.getAssignedTo().getId().equals(currentUser.getId());
    }

    private boolean canModify(Lead lead) {
        if (currentUser.isAdminOrManager()) return true;
        return currentUser.isOwnerScoped() && isOwner(lead);
    }

    private void assertCanView(Lead lead) {
        if (currentUser.isOwnerScoped() && !isOwner(lead)) {
            throw new ForbiddenOperationException("This lead is assigned to someone else");
        }
    }

    private LeadResponse toResponse(Lead lead) {
        LeadResponse r = leadMapper.toResponse(lead);
        r.setEditable(canModify(lead));
        return r;
    }

    private User resolveUser(Integer userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }
}

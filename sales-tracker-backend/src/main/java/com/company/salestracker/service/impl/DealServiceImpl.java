package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.dto.request.DealRequest;
import com.company.salestracker.dto.response.DealResponse;
import com.company.salestracker.entity.Deal;
import com.company.salestracker.entity.Lead;
import com.company.salestracker.entity.User;
import com.company.salestracker.enums.DealStage;
import com.company.salestracker.enums.LeadStatus;
import com.company.salestracker.exception.ConflictException;
import com.company.salestracker.exception.ForbiddenOperationException;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.mapper.DealMapper;
import com.company.salestracker.repository.DealRepository;
import com.company.salestracker.repository.LeadRepository;
import com.company.salestracker.repository.UserRepository;
import com.company.salestracker.service.DealService;
import com.company.salestracker.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final DealMapper dealMapper;
    private final CurrentUser currentUser;
    private final AuditService auditService;

    @Override
    @Transactional
    public DealResponse convertLeadToDeal(DealRequest request) {
        Lead lead = leadRepository.findById(request.getLeadId())
                .orElseThrow(() -> new ResourceNotFoundException("Lead not found: " + request.getLeadId()));

        boolean ownsLead = lead.getAssignedTo() != null
                && lead.getAssignedTo().getId().equals(currentUser.getId());
        if (!currentUser.isAdminOrManager() && !ownsLead) {
            throw new ForbiddenOperationException("You can only convert leads assigned to you");
        }

        lead.setStatus(LeadStatus.QUALIFIED);
        leadRepository.save(lead);

         User assignedTo;
        if (currentUser.isOwnerScoped()) {
            assignedTo = resolveUser(currentUser.getId());
        } else if (request.getAssignedTo() != null) {
            assignedTo = resolveUser(request.getAssignedTo());
        } else {
            assignedTo = lead.getAssignedTo();
        }
        Deal deal = dealMapper.toEntity(request, lead, assignedTo);

        Deal saved = dealRepository.save(deal);
        auditService.log(AuditAction.CREATE, "Deal", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public DealResponse updateDeal(Integer id, DealRequest request) {
        Deal deal = findDeal(id);
        assertOwnerOrManager(deal);
        assertNotLocked(deal);

        if (request.getDealStage() != null
                && isClosed(request.getDealStage())
                && request.getDealStage() != deal.getDealStage()) {
            throw new IllegalArgumentException("Use the Mark Won / Mark Lost action to close a deal");
        }

        User assignedTo = null;
        if (request.getAssignedTo() != null) {
        	Integer currentOwner = deal.getAssignedTo() != null ? deal.getAssignedTo().getId() : null;
            boolean changing = !request.getAssignedTo().equals(currentOwner);
            if (changing && !currentUser.isAdminOrManager()) {
                throw new ForbiddenOperationException("Only a Manager or Admin can re-assign a deal");
            }
            if (changing) assignedTo = resolveUser(request.getAssignedTo());
        }
        dealMapper.updateEntity(deal, request, assignedTo);

        Deal saved = dealRepository.save(deal);
        auditService.log(AuditAction.UPDATE, "Deal", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public DealResponse closeDeal(Integer id, String outcome) {
        Deal deal = findDeal(id);
        assertOwnerOrManager(deal);

        if (isClosed(deal.getDealStage())) {
            throw new ConflictException("Deal is already " + deal.getDealStage() + " and cannot be closed again");
        }

        DealStage outcomeStage;
        try {
            outcomeStage = DealStage.valueOf(outcome);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Outcome must be CLOSED_WON or CLOSED_LOST");
        }

        if (!isClosed(outcomeStage)) {
            throw new IllegalArgumentException("Outcome must be CLOSED_WON or CLOSED_LOST");
        }

        deal.setDealStage(outcomeStage);

        if (outcomeStage == DealStage.CLOSED_LOST) {
            deal.getLead().setStatus(LeadStatus.LOST);
            leadRepository.save(deal.getLead());
        }

        Deal saved = dealRepository.save(deal);
        auditService.log(AuditAction.UPDATE, "Deal", saved.getId());
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealResponse> listDeals(String stage, Pageable pageable) {
        boolean hasStage = stage != null && !stage.isBlank();
        Page<Deal> page;
        if (currentUser.isOwnerScoped()) {
        	Integer me = currentUser.getId();
            page = hasStage
                    ? dealRepository.findByDealStageAndAssignedToId(DealStage.valueOf(stage), me, pageable)
                    : dealRepository.findByAssignedToId(me, pageable);
        } else {
            page = hasStage
                    ? dealRepository.findByDealStage(DealStage.valueOf(stage), pageable)
                    : dealRepository.findAll(pageable);
        }
        return page.map(this::toResponse);
    }

   
    private Deal findDeal(Integer id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found: " + id));
    }

    private boolean isClosed(DealStage stage) {
        return stage == DealStage.CLOSED_WON || stage == DealStage.CLOSED_LOST;
    }

    private boolean isOwner(Deal deal) {
        return deal.getAssignedTo() != null && deal.getAssignedTo().getId().equals(currentUser.getId());
    }

    private boolean ownerOrManager(Deal deal) {
        if (currentUser.isAdminOrManager()) return true;
        return currentUser.isOwnerScoped() && isOwner(deal);
    }

     private boolean locked(Deal deal) {
        return isClosed(deal.getDealStage()) && !currentUser.isAdmin();
    }

    private void assertOwnerOrManager(Deal deal) {
        if (!ownerOrManager(deal)) {
            throw new ForbiddenOperationException("You can only change deals assigned to you");
        }
    }

    private void assertNotLocked(Deal deal) {
        if (locked(deal)) {
            throw new ConflictException("Deal is " + deal.getDealStage() + " and locked. Only an Admin can change it");
        }
    }

    private DealResponse toResponse(Deal deal) {
        DealResponse r = dealMapper.toResponse(deal);
        r.setEditable(ownerOrManager(deal) && !locked(deal));
        return r;
    }

    private User resolveUser(Integer userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }
}

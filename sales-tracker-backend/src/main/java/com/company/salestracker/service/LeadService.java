package com.company.salestracker.service;

import com.company.salestracker.dto.request.LeadRequest;
import com.company.salestracker.dto.response.LeadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeadService {
    LeadResponse createLead(LeadRequest request);
    LeadResponse updateLead(Integer id, LeadRequest request);
    LeadResponse getLead(Integer id);
    Page<LeadResponse> listLeads(String status, Pageable pageable);
    java.util.List<com.company.salestracker.dto.response.LeadActivityResponse> getActivities(Integer leadId);
    void addActivity(Integer leadId, String activityType, String notes, Integer createdByUserId);
}

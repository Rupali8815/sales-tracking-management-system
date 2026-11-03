package com.company.salestracker.repository;

import com.company.salestracker.entity.Lead;
import com.company.salestracker.enums.LeadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Integer> {
    Page<Lead> findByStatus(LeadStatus status, Pageable pageable);
    Page<Lead> findByStatusAndAssignedToId(LeadStatus status, Integer userId, Pageable pageable);
    Page<Lead> findByAssignedToId(Integer userId, Pageable pageable);
}

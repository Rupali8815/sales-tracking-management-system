package com.company.salestracker.repository;

import com.company.salestracker.entity.LeadActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeadActivityRepository extends JpaRepository<LeadActivity, Integer> {
	List<LeadActivity> findByLeadIdOrderByCreatedAtDesc(Integer leadId);
}

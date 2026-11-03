package com.company.salestracker.repository;

import com.company.salestracker.entity.Deal;
import com.company.salestracker.enums.DealStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Integer> {
    Page<Deal> findByDealStage(DealStage dealStage, Pageable pageable);
    Page<Deal> findByDealStageAndAssignedToId(DealStage dealStage, Integer userId, Pageable pageable);
    Page<Deal> findByAssignedToId(Integer userId, Pageable pageable);
}

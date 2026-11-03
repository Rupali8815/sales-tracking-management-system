package com.company.salestracker.repository;

import com.company.salestracker.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SalesRepository extends JpaRepository<Sale, Integer> {

    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :start AND :end")
    List<Sale> findBetweenDates(LocalDate start, LocalDate end);

    boolean existsByDealId(Integer dealId);

    Page<Sale> findByDealAssignedToId(Integer userId, Pageable pageable);

    @Query("SELECT s FROM Sale s WHERE s.deal.assignedTo.id = :userId")
    List<Sale> findByAssignedUser(Integer userId);
}

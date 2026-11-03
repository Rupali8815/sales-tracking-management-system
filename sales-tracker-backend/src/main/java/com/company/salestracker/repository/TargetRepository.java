package com.company.salestracker.repository;

import com.company.salestracker.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Integer> {
    List<Target> findByUserIdAndTargetYear(Integer userId, Integer targetYear);
    Optional<Target> findByUserIdAndTargetMonthAndTargetYear(Integer userId, Integer targetMonth, Integer targetYear);
}

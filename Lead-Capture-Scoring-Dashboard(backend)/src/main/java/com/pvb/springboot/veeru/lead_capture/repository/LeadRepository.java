package com.pvb.springboot.veeru.lead_capture.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pvb.springboot.veeru.lead_capture.entity.Lead;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {

    @Query("SELECT l FROM Lead l WHERE " +
           "(:budgetMin IS NULL OR l.budget >= :budgetMin) AND " +
           "(:budgetMax IS NULL OR l.budget <= :budgetMax) AND " +
           "(:location IS NULL OR l.preferredLocation LIKE %:location%)")
    List<Lead> findByFilters(
            @Param("budgetMin") Double budgetMin,
            @Param("budgetMax") Double budgetMax,
            @Param("location") String location);

    List<Lead> findAllByOrderByScoreDesc();
}

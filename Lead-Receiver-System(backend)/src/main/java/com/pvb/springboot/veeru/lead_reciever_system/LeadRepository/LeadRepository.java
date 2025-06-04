package com.pvb.springboot.veeru.lead_reciever_system.LeadRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pvb.springboot.veeru.lead_reciever_system.entity.Lead;

@Repository 
public interface LeadRepository extends JpaRepository<Lead, Long> {
	
}

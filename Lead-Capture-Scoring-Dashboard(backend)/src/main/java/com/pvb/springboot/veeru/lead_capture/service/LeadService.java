package com.pvb.springboot.veeru.lead_capture.service;

import com.pvb.springboot.veeru.lead_capture.controller.MockAIController;
import com.pvb.springboot.veeru.lead_capture.dto.LeadRequest;
import com.pvb.springboot.veeru.lead_capture.entity.Lead;
import com.pvb.springboot.veeru.lead_capture.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LeadService {

    private final LeadRepository leadRepository;
    private final MockAIController mockAIController;
    private final RestTemplate restTemplate;

    @Autowired
    public LeadService(LeadRepository leadRepository, MockAIController mockAIController, RestTemplate restTemplate) {
        this.leadRepository = leadRepository;
        this.mockAIController = mockAIController;
        this.restTemplate = restTemplate;
    }

    public Lead createLead(Lead lead) {
        lead.setCreatedAt(LocalDateTime.now());
        try {
            System.out.println("--- LeadService: Calling AI for score/category for lead: " + lead.getName() + " ---");
            LeadRequest aiRequest = new LeadRequest();
            aiRequest.setBudget(lead.getBudget());
            aiRequest.setLocation(lead.getPreferredLocation());

            ResponseEntity<Map<String, Object>> aiResponse = mockAIController.getScore(aiRequest);

            if (aiResponse.getStatusCode().is2xxSuccessful() && aiResponse.getBody() != null) {
                Map<String, Object> aiResult = aiResponse.getBody();
                Integer score = ((Number) aiResult.getOrDefault("score", 0)).intValue();
                String category = (String) aiResult.getOrDefault("category", "unknown");

                lead.setScore(score);
                lead.setCategory(category);
                System.out.println("--- LeadService: AI returned Score: " + score + ", Category: " + category + " for lead: " + lead.getName() + " ---");
            } else {
                System.err.println("--- LeadService: AI call failed or returned empty body for lead: " + lead.getName() + ". Status: " + aiResponse.getStatusCode() + " ---");
                lead.setScore(0); 
                lead.setCategory("failed_ai_call"); 
            }
        } catch (Exception e) {
            System.err.println("--- LeadService: Error calling AI service for lead: " + lead.getName() + ". Error: " + e.getMessage() + " ---");
            e.printStackTrace(); 
            lead.setScore(0); 
            lead.setCategory("error_ai_processing"); 
        }
                  Lead savedLead = leadRepository.save(lead);
                  System.out.println("--- LeadService: Lead saved locally: " + savedLead.getName() + " ---");
                  try {
                      String receiverUrl = "http://localhost:8081/receive-api/leads";

                      HttpHeaders headers = new HttpHeaders();
                      headers.setContentType(MediaType.APPLICATION_JSON);

                      Lead leadToSend = new Lead();
                      leadToSend.setName(savedLead.getName());
                      leadToSend.setEmail(savedLead.getEmail());
                      leadToSend.setPhone(savedLead.getPhone());
                      leadToSend.setBudget(savedLead.getBudget());
                      leadToSend.setPreferredLocation(savedLead.getPreferredLocation());
                      leadToSend.setScore(savedLead.getScore());
                      leadToSend.setCategory(savedLead.getCategory());
                      leadToSend.setCreatedAt(savedLead.getCreatedAt());
                      leadToSend.setId(null); 

                      HttpEntity<Lead> requestEntity = new HttpEntity<>(leadToSend, headers);
                      System.out.println("--- LeadService: Attempting to send lead to external receiver at: " + receiverUrl + " ---");
                      ResponseEntity<String> receiverResponse = restTemplate.postForEntity(
                          receiverUrl,
                          requestEntity,
                          String.class
                      );

                      if (receiverResponse.getStatusCode().is2xxSuccessful()) {
                          System.out.println("--- LeadService: Successfully sent lead to receiver system ---");
                          System.out.println("Receiver Response Status: " + receiverResponse.getStatusCode());
                          System.out.println("Receiver Response Body: " + receiverResponse.getBody());
                      } else {
                          System.err.println("--- LeadService: Failed to send lead to receiver system. Status: " + receiverResponse.getStatusCode());
                          System.err.println("Receiver Error Body: " + receiverResponse.getBody());
                      }
                  } catch (Exception e) {
                      System.err.println("--- LeadService: Error sending lead to receiver system: " + e.getClass().getName() + " - " + e.getMessage());
                      e.printStackTrace();
                  }

                  return savedLead;
    }    
    public List<Lead> getAllLeads(Double budgetMin, Double budgetMax, String location, String sortBy) {
        List<Lead> leads = leadRepository.findAll(); 
        if (budgetMin != null) {
            leads.removeIf(lead -> lead.getBudget() == null || lead.getBudget() < budgetMin);
        }
        if (budgetMax != null) {
            leads.removeIf(lead -> lead.getBudget() == null || lead.getBudget() > budgetMax);
        }
        if (location != null && !location.trim().isEmpty()) {
            leads.removeIf(lead -> lead.getPreferredLocation() == null || !lead.getPreferredLocation().equalsIgnoreCase(location.trim()));
        }
        if ("scoreDesc".equalsIgnoreCase(sortBy)) {
            leads.sort(Comparator.comparing(Lead::getScore, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        } else if ("scoreAsc".equalsIgnoreCase(sortBy)) {
            leads.sort(Comparator.comparing(Lead::getScore, Comparator.nullsFirst(Comparator.naturalOrder())));
        } else if ("createdAtDesc".equalsIgnoreCase(sortBy)) {
            leads.sort(Comparator.comparing(Lead::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        } else if ("createdAtAsc".equalsIgnoreCase(sortBy)) {
            leads.sort(Comparator.comparing(Lead::getCreatedAt, Comparator.nullsFirst(Comparator.naturalOrder())));
        }
        return leads;
    }
    
    public Optional<Lead> getLeadById(Long id) {
        return leadRepository.findById(id);
    }
    
    public Lead updateLead(Long id, Lead leadDetails) {
        Lead lead = leadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lead not found with id: " + id));

        lead.setName(leadDetails.getName());
        lead.setEmail(leadDetails.getEmail());
        lead.setPhone(leadDetails.getPhone());
        lead.setBudget(leadDetails.getBudget());
        lead.setPreferredLocation(leadDetails.getPreferredLocation());
        lead.setScore(leadDetails.getScore());
        lead.setCategory(leadDetails.getCategory());
        return leadRepository.save(lead);
    }
    
    public void deleteLead(Long id) {
        if (!leadRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Lead not found with id: " + id);
        }
        leadRepository.deleteById(id);
        System.out.println("--- LeadService: Successfully deleted lead with ID: " + id + " ---");
    }
}
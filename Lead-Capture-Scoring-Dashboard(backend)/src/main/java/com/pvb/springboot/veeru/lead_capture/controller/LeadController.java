package com.pvb.springboot.veeru.lead_capture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pvb.springboot.veeru.lead_capture.entity.Lead;
import com.pvb.springboot.veeru.lead_capture.service.LeadService;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) {
        Lead createdLead = leadService.createLead(lead);
        return new ResponseEntity<>(createdLead, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Lead>> getAllLeads(
            @RequestParam(required = false) Double budgetMin,
            @RequestParam(required = false) Double budgetMax,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String sortBy) {
        List<Lead> leads = leadService.getAllLeads(budgetMin, budgetMax, location, sortBy);
        return new ResponseEntity<>(leads, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLeadById(@PathVariable Long id) {
        return leadService.getLeadById(id)
                .map(lead -> new ResponseEntity<>(lead, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable Long id, @RequestBody Lead leadDetails) {
        Lead updatedLead = leadService.updateLead(id, leadDetails);
        return new ResponseEntity<>(updatedLead, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        leadService.deleteLead(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
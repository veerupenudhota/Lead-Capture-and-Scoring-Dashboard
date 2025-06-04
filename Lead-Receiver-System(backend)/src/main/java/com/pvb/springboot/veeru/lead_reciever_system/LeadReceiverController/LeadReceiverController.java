package com.pvb.springboot.veeru.lead_reciever_system.LeadReceiverController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pvb.springboot.veeru.lead_reciever_system.LeadRepository.LeadRepository;
import com.pvb.springboot.veeru.lead_reciever_system.entity.Lead;

import java.util.List;

@RestController 
@RequestMapping("/receive-api/leads") 
public class LeadReceiverController {

 @Autowired 
 private LeadRepository leadRepository;

 @PostMapping
 public ResponseEntity<String> receiveLead(@RequestBody Lead lead) {
     System.out.println("--- RECEIVED LEAD IN RECEIVER SYSTEM ---");
     System.out.println("Name: " + lead.getName());
     System.out.println("Score: " + lead.getScore());
     System.out.println("Category: " + lead.getCategory());
     System.out.println("Original Created At: " + lead.getCreatedAt()); 
     System.out.println("---------------------------------------");

     leadRepository.save(lead);
     return new ResponseEntity<>("Lead received and saved successfully by receiver!", HttpStatus.OK);
 }
 
 @GetMapping
 public ResponseEntity<List<Lead>> getAllReceivedLeads() {
     List<Lead> leads = leadRepository.findAll(); 
     return new ResponseEntity<>(leads, HttpStatus.OK);
 }
}
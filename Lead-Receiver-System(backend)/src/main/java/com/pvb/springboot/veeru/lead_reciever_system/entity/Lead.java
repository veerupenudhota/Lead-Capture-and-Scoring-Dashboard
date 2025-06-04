package com.pvb.springboot.veeru.lead_reciever_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity 
public class Lead {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) 
    private String name;

    @Column(nullable = false, unique = true) 
    private String email;

    private String phone;
    private Double budget;
    private String preferredLocation;
    private Integer score;
    private String category;

    @Column(name = "created_at", nullable = false) 
    private LocalDateTime createdAt; 

 public Lead() {
 }

 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public String getName() {
     return name;
 }

 public void setName(String name) {
     this.name = name;
 }

 public String getEmail() {
     return email;
 }

 public void setEmail(String email) {
     this.email = email;
 }

 public String getPhone() {
     return phone;
 }

 public void setPhone(String phone) {
     this.phone = phone;
 }

 public Double getBudget() {
     return budget;
 }

 public void setBudget(Double budget) {
     this.budget = budget;
 }

 public String getPreferredLocation() {
     return preferredLocation;
 }

 public void setPreferredLocation(String preferredLocation) {
     this.preferredLocation = preferredLocation;
 }

 public Integer getScore() {
     return score;
 }

 public void setScore(Integer score) {
     this.score = score;
 }

 public String getCategory() {
     return category;
 }

 public void setCategory(String category) {
     this.category = category;
 }

 public LocalDateTime getCreatedAt() {
     return createdAt;
 }

 public void setCreatedAt(LocalDateTime createdAt) {
     this.createdAt = createdAt;
 }

 @Override
 public String toString() {
     return "Lead{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", budget=" + budget +
            ", preferredLocation='" + preferredLocation + '\'' +
            ", score=" + score +
            ", category='" + category + '\'' +
            ", createdAt=" + createdAt +
            '}';
 }
}

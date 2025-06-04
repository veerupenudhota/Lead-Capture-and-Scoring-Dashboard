package com.pvb.springboot.veeru.lead_capture.dto;

public class LeadRequest {
    private String location;
    private double budget;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
    
}

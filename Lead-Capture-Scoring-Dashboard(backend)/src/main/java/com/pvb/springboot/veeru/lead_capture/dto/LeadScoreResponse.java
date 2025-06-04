package com.pvb.springboot.veeru.lead_capture.dto;

public class LeadScoreResponse {
    private Integer score;
    private String category;
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
	public LeadScoreResponse(Integer score, String category) {
		super();
		this.score = score;
		this.category = category;
	}
	@Override
	public String toString() {
		return "LeadScoreResponse [score=" + score + ", category=" + category + ", getScore()=" + getScore()
				+ ", getCategory()=" + getCategory() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
    
}

package it.polito.tdp.PremierLeague.model;

public class Arco {
	
	Integer teamIDup;
	Integer teamIDdown;
	double weight;
	public Arco(Integer teamIDup, Integer teamIDdown, double weight) {
		super();
		this.teamIDup = teamIDup;
		this.teamIDdown = teamIDdown;
		this.weight = weight;
	}
	public Integer getTeamIDup() {
		return teamIDup;
	}
	public Integer getTeamIDdown() {
		return teamIDdown;
	}
	public double getWeight() {
		return weight;
	}
	
	

}

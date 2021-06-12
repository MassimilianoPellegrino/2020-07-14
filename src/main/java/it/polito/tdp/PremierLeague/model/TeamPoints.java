package it.polito.tdp.PremierLeague.model;

public class TeamPoints {
	Team team;
	Double points;
	public TeamPoints(Team team, Double points) {
		super();
		this.team = team;
		this.points = points;
	}
	public Team getTeam() {
		return team;
	}
	public Double getPoints() {
		return points;
	}
	@Override
	public String toString() {
		return team+" "+points;
	}
	
	
	
}

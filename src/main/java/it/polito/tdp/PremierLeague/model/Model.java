package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	Graph<Team, DefaultWeightedEdge> grafo;
	PremierLeagueDAO dao;
	Map<Integer, Team> idMap;
	List<TeamPoints> classifica;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		// TODO Auto-generated method stub
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listAllTeams(idMap));
		calcolaClassifica();
		for(TeamPoints t1: classifica) {
			for(TeamPoints t2: classifica) {
				if(t1.getPoints()>t2.getPoints())
					Graphs.addEdgeWithVertices(grafo, t1.getTeam(), t2.getTeam(), t1.getPoints()-t2.getPoints());
				else if(t2.getPoints()>t1.getPoints())
					Graphs.addEdgeWithVertices(grafo, t2.getTeam(), t1.getTeam(), t2.getPoints()-t1.getPoints());

			}
		}
	}
	
	
	public void calcolaClassifica(){
		classifica = new ArrayList<>();
		double punti = 0;
		for(Team t: idMap.values()) {
			punti = 0;
			for(Match m: dao.listAllMatches()) {
				if(m.getTeamHomeID().equals(t.getTeamID())) {
					if(m.getResultOfTeamHome().equals(1)) {
						punti+=3;
					}else if(m.getResultOfTeamHome().equals(0)) {
						punti+=1;
					}
				}else if(m.getTeamAwayID().equals(t.getTeamID())) {
					if(m.getResultOfTeamHome().equals(-1)) {
						punti+=3;
					}else if(m.getResultOfTeamHome().equals(0)) {
						punti+=1;
					}
				}
			}
			
			classifica.add(new TeamPoints(t, punti));
		}
		
		classifica.sort(new Comparator<TeamPoints>() {

			@Override
			public int compare(TeamPoints o1, TeamPoints o2) {
				// TODO Auto-generated method stub
				return (int) (o2.getPoints()-o1.getPoints());
			}
		});
		
	}

	public Graph<Team, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public Map<Integer, Team> getIdMap() {
		return idMap;
	}

	public List<TeamPoints> getClassifica() {
		return classifica;
	}
	
	
}

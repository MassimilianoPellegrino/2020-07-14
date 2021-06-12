package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

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
	PriorityQueue<Match> queue;
	int totReporter;
	int mediaReporter;
	int numPartiteSottoSoglia;
	List<Match> matches;
	List<Team> teams;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
		matches = dao.listAllMatches();
		teams = dao.listAllTeams(idMap);
	}
	
	public void creaGrafo() {
		// TODO Auto-generated method stub
		grafo = new SimpleDirectedWeightedGraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, teams);
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
			for(Match m: matches) {
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

	public Team randomSuccessor(Team t) {
		List<Team> successori = Graphs.successorListOf(grafo, t);
		Random r = new Random();
		
		
		if(successori.size()>0) {
			int i = r.nextInt(successori.size());
			return successori.get(i);
		}else
			return null;
	}
	
	public Team randomPredecessor(Team t) {
		List<Team> predecessori = Graphs.predecessorListOf(grafo, t);
		Random r = new Random();
		
		
		if(predecessori.size()>0) {
			int i = r.nextInt(predecessori.size());
			return predecessori.get(i);
		}else
			return null;
	}
	
	public void simula(int n, int x) {
		mediaReporter = 0;
		numPartiteSottoSoglia = 0;
		totReporter = 0;
		
		queue = new PriorityQueue<>();
		
		Map<Team, Integer> map = new HashMap<>();
		
		for(Team t: idMap.values())
			map.put(t, n);
		
		for(Match m: matches)
			queue.add(m);
		
		Match m;
		
		while((m = queue.poll())!=null){
			
			if(m.getResultOfTeamHome().equals(0))
				continue;
			
			double r1 = Math.random();
			double r2 = Math.random();
			
			if(r1<0.5) {
				Team winner;
				
				if(m.getResultOfTeamHome().equals(1))
					winner = idMap.get(m.getTeamHomeID());
				else
					winner = idMap.get(m.getTeamAwayID());
				
				Team rp;
				
				if(map.get(winner)>0) {
					rp = randomPredecessor(winner);
					if(rp!=null) {
						map.put(winner, map.get(winner)-1);
						map.put(rp, map.get(rp)+1);
					}
				}
				
			}
			
			if(r2<0.2) {
				Team loser;
				
				if(m.getResultOfTeamHome().equals(-1))
					loser = idMap.get(m.getTeamHomeID());
				else
					loser = idMap.get(m.getTeamAwayID());
				
				
				
				if(map.get(loser)>0) {
					Random r = new Random();
					int ranNum = r.nextInt(map.get(loser))+1;
					Team rs;
					rs = randomSuccessor(loser);
					if(rs!=null) {
						if(map.get(loser)>=ranNum) {
							map.put(loser, map.get(loser)-ranNum);
							map.put(rs, map.get(rs)+ranNum);
						}else {
							map.put(rs, map.get(rs)+map.get(loser));
							map.put(loser, 0);
						}
					}
				}
			}
				
			if(map.get(idMap.get(m.getTeamHomeID()))+map.get(idMap.get(m.getTeamAwayID()))<x)
				numPartiteSottoSoglia++;
				
			totReporter+=map.get(idMap.get(m.getTeamHomeID()))+map.get(idMap.get(m.getTeamAwayID()));	
		}
		
		
		
		
		
	}

	public int getTotReporter() {
		return totReporter;
	}

	public int getNumPartiteSottoSoglia() {
		return numPartiteSottoSoglia;
	}

	public List<Match> getMatches() {
		return matches;
	}
	
	
}

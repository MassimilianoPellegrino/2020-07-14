/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Team;
import it.polito.tdp.PremierLeague.model.TeamPoints;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnClassifica"
    private Button btnClassifica; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="cmbSquadra"
    private ComboBox<Team> cmbSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doClassifica(ActionEvent event) {
    	this.txtResult.clear();
    	Team t;
    	try {
    		t = cmbSquadra.getValue();
    		this.txtResult.appendText("Squadre battute da "+t+":\n\n");
    		for(Team team: Graphs.successorListOf(model.getGrafo(), t)) {
    			this.txtResult.appendText(team+" "+model.getGrafo().getEdgeWeight(model.getGrafo().getEdge(t, team))+"\n");
    		}
    		this.txtResult.appendText("\nSquadre che hanno battuto "+t+":\n\n");
    		List<TeamPoints> list = new ArrayList<>(); 
    		for(Team team: Graphs.predecessorListOf(model.getGrafo(), t)) {
    			list.add(0, new TeamPoints(team, model.getGrafo().getEdgeWeight(model.getGrafo().getEdge(team, t))));
    		}
    		for(TeamPoints tp: list)
    			this.txtResult.appendText(tp+"\n");
    			
    	}catch(NullPointerException e) {
    		this.txtResult.setText("Selezionare una squadra");
    	}

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	model.creaGrafo();
    	this.cmbSquadra.getItems().addAll(model.getIdMap().values());
    	
    	this.txtResult.setText("Grafo creato con "+model.getGrafo().vertexSet().size()+" vertici e "+model.getGrafo().edgeSet().size()+" archi");
    	
    	btnClassifica.setDisable(false);
    	btnSimula.setDisable(false);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	int N;
    	int X;
    	try {
    		N = Integer.parseInt(txtN.getText());
    		X = Integer.parseInt(txtX.getText());
    		model.simula(N, X);
    		this.txtResult.appendText("Numero medio di reporter per partita: "+model.getTotReporter()/model.getMatches().size()+"\n");
    		this.txtResult.appendText("Numero di partite con meno reporter della soglia inserita: "+model.getNumPartiteSottoSoglia()+"\n");
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire numeri interi");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnClassifica != null : "fx:id=\"btnClassifica\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbSquadra != null : "fx:id=\"cmbSquadra\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        this.btnClassifica.setDisable(true);
        this.btnSimula.setDisable(true);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}

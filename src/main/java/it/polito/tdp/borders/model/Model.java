package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private BordersDAO dao;
	private Map<Integer, Country> idMap;
	private SimpleGraph<Country, DefaultEdge> grafo;
	
	public Model() {
		dao = new BordersDAO();
		idMap = new HashMap<Integer, Country>();
		dao.loadAllCountries(idMap);
	}

	public List<Border> getConfini(int anno) {
		return dao.getCountryPairs(anno, this.idMap);
	}
	
	public void creaGrafo(int anno) {
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		// aggiungo vertici
		Graphs.addAllVertices(grafo, dao.getVertici(anno, idMap));
		
		// aggiungo archi
		for(Adiacenza a: dao.getAdiacenze(anno, idMap)) {
			if(this.grafo.getEdge(a.getC1(), a.getC2()) == null) {
				Graphs.addEdgeWithVertices(grafo, a.getC1(), a.getC2());
			}
		}
		
		System.out.println("# Vertici: " + this.grafo.vertexSet().size());
		System.out.println("# Archi: " + this.grafo.edgeSet().size());
	}
	
	
	public Set<Country> getStati(){
		return this.grafo.vertexSet();
	}

	public Map<Country, Integer> numeroStatiConfinanti(){
		Map<Country, Integer> result = new HashMap<>();
		for(Country c: this.grafo.vertexSet()) {
			result.put(c, this.grafo.degreeOf(c));
		}
		return result;
	}
}

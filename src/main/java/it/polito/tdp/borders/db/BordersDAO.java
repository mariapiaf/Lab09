package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.util.Map;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.borders.model.Adiacenza;
import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country> idMap) {

		String sql = "SELECT * FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getInt("CCode"))) {
					Country c = new Country(rs.getInt("CCode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					idMap.put(c.getCcode(), c);
				}
			}
			
			conn.close();


		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country> idMap) {

		String sql = "SELECT DISTINCT * "
				+ "FROM contiguity "
				+ "WHERE conttype =1 AND YEAR <= ? AND state1no > state2no";
		
		List<Border> result = new ArrayList<Border>();
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c1 = idMap.get(rs.getInt("state1no"));
				Country c2 = idMap.get(rs.getInt("state2no"));
				result.add(new Border(c1, c2, rs.getInt("year"), rs.getInt("conttype")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	
		
	}
	
	
	public List<Country> getVertici(int anno, Map<Integer, Country> idMap){
		String sql = "SELECT DISTINCT c.CCode, c.StateAbb, c.StateNme "
				+ "FROM country c, contiguity con "
				+ "WHERE con.year<=? AND (c.CCode = con.state1no OR c.CCode = con.state2no)";
		
		List<Country> result = new LinkedList<>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Country(rs.getInt("CCode"), rs.getString("StateAbb"), rs.getString("StateNme")));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
	public List<Adiacenza> getAdiacenze(int anno, Map<Integer, Country> idMap){
		String sql = "SELECT DISTINCT c.CCode, c.StateAbb, c.StateNme,con.state1no, con.state2no "
				+ "FROM country c, contiguity con "
				+ "WHERE con.year<=? AND (c.CCode = con.state1no OR c.CCode = con.state2no) "
				+ "	AND conttype = 1";
		
		List<Adiacenza> result = new LinkedList<>();
	
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Country c1 = idMap.get(rs.getInt("state1no"));
				Country c2 = idMap.get(rs.getInt("state2no"));
				result.add(new Adiacenza(c1, c2));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	
	}
}

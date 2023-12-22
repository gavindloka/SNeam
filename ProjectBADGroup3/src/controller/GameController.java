package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Game;
import util.Connect;

public class GameController {
	private Connect connect = Connect.getInstance();
	ArrayList<Game> games = new ArrayList<>();	
	ObservableList<Game> gamesTableList = FXCollections.observableArrayList();
	public void insertGame(String gameID, String gameName, String gameDescription, Integer gamePrice) {
		String query = "INSERT INTO game(`GameID`,`GameName`,`GameDescription`,`Price`) VALUES(?,?,?,?)";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1, gameID);
			prst.setString(2, gameName);
			prst.setString(3, gameDescription);
			prst.setInt(4, gamePrice);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<Game> getGameData() {
	    String query = "SELECT * FROM game";
	    connect.rs = connect.execQuery(query); 
	    try {
	        while (connect.rs.next()) {
	            String gameID = connect.rs.getString("GameID");
	            String gameName = connect.rs.getString("GameName");
	            String gameDesc = connect.rs.getString("GameDescription");
	            int gamePrice = connect.rs.getInt("Price");
	            Game game = new Game(gameID, gameName, gameDesc, gamePrice);
	            games.add(game);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return games;
	}
	
	public Game getGameDataByID(String inputGameID){
		Game game = null;
		String query = "SELECT * FROM game WHERE gameID = ?";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1, inputGameID);
			connect.rs = prst.executeQuery();
			while(connect.rs.next()) {
				String gameID = connect.rs.getString("GameID");
	            String gameName = connect.rs.getString("GameName");
	            String gameDesc = connect.rs.getString("GameDescription");
	            int gamePrice = connect.rs.getInt("Price");
	            game = new Game(gameID, gameName, gameDesc, gamePrice);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return game;
	}
	public String generateGameID() {
		String query = "SELECT GameID FROM game";
		connect.rs = connect.execQuery(query);

		String maxGameID = "GA000";
		try {
			while(connect.rs.next()) {
				String gameID = connect.rs.getString("GameID");
				String digit = gameID.substring(2);
				if(digit.matches("\\d+")&&gameID.compareTo(maxGameID)>0) {
					maxGameID = gameID;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = Integer.parseInt(maxGameID.substring(2))+1;
		String newID = String.format("GA%03d", num);
		return newID;
	}
	
	public boolean isGameNameUnique(String name) {
		boolean unique = true;
		String query = "SELECT GameName FROM game";
		connect.rs = connect.execQuery(query);
		try {
			while(connect.rs.next()) {
				String gameName = connect.rs.getString("GameName");
				if(gameName.equals(name)) {
					unique = false;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return unique;
	}
	public void deleteGame(String gameID) {
		String query  = "DELETE FROM game WHERE GameID LIKE ?";
		PreparedStatement prst = connect.prepareStatement(query);

		try {
			prst.setString(1, gameID);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateGame(String gameName, String gameDesc, int gamePrice,String gameID) {
		String query = "UPDATE game SET GameName = ?, GameDescription = ?, Price = ? WHERE GameID LIKE ?";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1, gameName);
			prst.setString(2, gameDesc);
			prst.setInt(3, gamePrice);
			prst.setString(4, gameID);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}

package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Cart;
import util.Connect;

public class CartController {
	private Connect connect = Connect.getInstance();
	ArrayList<Cart>carts = new ArrayList<>();
	public ArrayList<Cart> getCartData() {
		String query = "SELECT * FROM cart";
		connect.rs = connect.execQuery(query);
		try {
			while(connect.rs.next()) {
				String userID = connect.rs.getString("UserID");
				String gameID = connect.rs.getString("GameID");
				int quantity = connect.rs.getInt("Quantity");
				Cart cart = new Cart(userID,gameID,quantity);
				carts.add(cart);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carts;
	}
	public ArrayList<Cart> getCartDataByUserID(String lUserID){
		String query = "SELECT * FROM cart WHERE UserID = ?";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1, lUserID);
			connect.rs = prst.executeQuery();
			while(connect.rs.next()) {
				String userID = connect.rs.getString("UserID");
				String gameID = connect.rs.getString("GameID");
				int quantity = connect.rs.getInt("Quantity");
				Cart cart = new Cart(userID,gameID,quantity);
				carts.add(cart);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return carts;
	}
	
	public void insertCart(String userID, String gameID, int quantity) {
		String query = "INSERT INTO cart(`UserID`,`GameID`,`Quantity`) VALUES (?,?,?)";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1, userID);
			prst.setString(2,gameID);
			prst.setInt(3, quantity);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteCart(String userID, String gameID) {
		String query = "DELETE FROM cart WHERE UserID LIKE ? AND GameID LIKE ?";
		PreparedStatement prst = connect.prepareStatement(query);
		
		try {
			prst.setString(1, userID);
			prst.setString(2, gameID);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateCart(String userID, String gameID, int quantity) {
		String query = "UPDATE cart SET Quantity = ? WHERE userID LIKE ? AND GameID LIKE ?";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setInt(1, quantity);
			prst.setString(2, userID);
			prst.setString(3, gameID);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}



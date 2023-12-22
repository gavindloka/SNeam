package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import util.Connect;

public class TransactionController {
	private Connect connect = Connect.getInstance();
	
	public String generateTransactionID() {
		String query = "SELECT TransactionID FROM TransactionHeader";
		connect.rs = connect.execQuery(query);
		
		String maxTransactionID = "TR000";
		try {
			while(connect.rs.next()) {
				String transactionID = connect.rs.getString("TransactionID");
				String digit = transactionID.substring(2);
				if(digit.matches("\\d+")&&transactionID.compareTo(maxTransactionID)>0) {
					maxTransactionID = transactionID;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = Integer.parseInt(maxTransactionID.substring(2))+1;
		String newID = String.format("TR%03d", num);
		return newID;
	}
	
	public void insertTransactionHeader(String transactionID, String userID) {
		String query = "INSERT INTO transactionHeader(TransactionID,UserID) VALUES(?,?)";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1,transactionID);
			prst.setString(2, userID);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertTransactionDetail(String transactionID, String gameID, int quantity) {
		String query = "INSERT INTO transactionDetail(TransactionID,GameID,Quantity) VALUES (?,?,?)";
		PreparedStatement prst = connect.prepareStatement(query);
//		System.out.println("Insert transaction detail: " + transactionID + ", " + gameID + ", " + quantity);
		try {
			prst.setString(1,transactionID);
			prst.setString(2,gameID);
			prst.setInt(3,quantity);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void clearCart(String userID) {
		String query = "DELETE FROM cart WHERE UserID LIKE ?";
		PreparedStatement prst = connect.prepareStatement(query);
		
		try {
			prst.setString(1, userID);
			prst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

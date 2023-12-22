package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import model.User;
import util.Connect;

public class AuthController {
	private Connect connect = Connect.getInstance();
	public static User authUser;

	public void insertUser(String userID,String username,String password, String phoneNumber, String email, String role) {
		String query = "INSERT INTO user(`UserID`,`Username`,`Password`,`PhoneNumber`,`Email`,`Role`) VALUES (?,?,?,?,?,?)";
		PreparedStatement prst = connect.prepareStatement(query);
		try {
			prst.setString(1,userID);
			prst.setString(2, username);
			prst.setString(3, password);
			prst.setString(4, phoneNumber);
			prst.setString(5, email);
			prst.setString(6, role);
			prst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean isEmailUnique(String rEmail) {
		Boolean unique = true;
		String query = "SELECT Email FROM user";
		connect.rs = connect.execQuery(query);
		try {
			while(connect.rs.next()) {
				String email = connect.rs.getString("Email");
				if(email.equals(rEmail)) {
					unique = false;
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unique;
	}

	public String generateUserID() {
		String query = "SELECT UserID FROM user";
		connect.rs = connect.execQuery(query);

		String maxUserID = "AC000";
		try {
			while(connect.rs.next()) {
				String userID = connect.rs.getString("UserID");
				String digit = userID.substring(2);
				if(digit.matches("\\d+")&&userID.compareTo(maxUserID)>0) {
					maxUserID = userID;
				}			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = Integer.parseInt(maxUserID.substring(2))+1;
		String newID = String.format("AC%03d", num);
		return newID;
	}
	public String loggedInUser(String lEmail) {
	    String loggedInUname = "";
	    String query = "SELECT Username FROM user WHERE Email = ?";
	    PreparedStatement prst = connect.prepareStatement(query);

	    try {
	        prst.setString(1, lEmail);
	        connect.rs = prst.executeQuery();

	        while (connect.rs.next()) {
	            loggedInUname = connect.rs.getString("Username");
	            break; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return loggedInUname;	
	}
	
	public String loggedInUserID(String lEmail) {
	    String loggedInUserID = "";
	    String query = "SELECT UserID FROM user WHERE Email = ?";
	    PreparedStatement preparedStatement = connect.prepareStatement(query);

	    try {
	        preparedStatement.setString(1, lEmail);
	        connect.rs = preparedStatement.executeQuery();

	        while (connect.rs.next()) {
	            loggedInUserID = connect.rs.getString("UserID");
	            break; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return loggedInUserID;	
	}
	public String loggedInUserRole(String lEmail) {
	    String loggedInUserRole = "";
	    String query = "SELECT Role FROM user WHERE Email = ?";
	    PreparedStatement preparedStatement = connect.prepareStatement(query);

	    try {
	        preparedStatement.setString(1, lEmail);
	        connect.rs = preparedStatement.executeQuery();

	        while (connect.rs.next()) {
	            loggedInUserRole = connect.rs.getString("Role");
	            break; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return loggedInUserRole;	
	}
	public boolean isCredentialValid(String lEmail, String lPass) {
		Boolean valid = false;
		String query = "SELECT Email,Password FROM user";
		connect.rs = connect.execQuery(query);
		try {
			while(connect.rs.next()) {
				String email = connect.rs.getString("Email");
				String pass = connect.rs.getString("Password");
				if(email.equals(lEmail)&&pass.equals(lPass)) {
					valid = true;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return valid;
	}
}

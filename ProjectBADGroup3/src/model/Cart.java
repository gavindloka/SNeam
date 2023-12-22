package model;

public class Cart {
	private String userID, gameID;
	private int quantity;
	public Cart(String userID, String gameID, int quantity) {
		this.userID = userID;
		this.gameID = gameID;
		this.quantity = quantity;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}

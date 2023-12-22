package view;

import java.util.ArrayList;
import java.util.Optional;

import controller.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Game;

public class AdminHomePage extends BorderPane{
	ScrollPane adminSP;
	VBox adminVB,adminGameDescVB,adminGameMenuVB,adminButtonVB,adminRootVB;
	GridPane adminGP1,adminGP2;
	HBox adminHB;
	TextField gameTitleTF, gamePriceTF;
	TextArea gameDescTA;
	Label adminTitleLbl,adminMenuLbl,adminGameTitleLbl,adminGameDescLbl,adminGamePriceLbl,adminMenuGameTitleLbl,adminMenuGameDescLbl,adminMenuGamePriceLbl;
	Button addGameBtn, updateGameBtn, deleteGameBtn;
	ListView<String>listView;
	MenuBar adminMenuBar;
	Menu adminMenu;
	MenuItem adminMenuItem;
	ArrayList<Game>games;
	GameController gameController;
	Alert alert;

	public void init() {
		gameController = new GameController();
		adminVB = new VBox();
		adminGameDescVB = new VBox();
		adminGameMenuVB = new VBox();
		adminButtonVB = new VBox();
		adminGP1 = new GridPane();
		adminGP2 = new GridPane();
		adminHB = new HBox();

		gameTitleTF = new TextField();
		gameDescTA = new TextArea();
		gamePriceTF = new TextField();

		adminTitleLbl = new Label("Hello, admin");
		adminMenuLbl = new Label("Admin Menu");
		adminGameTitleLbl = new Label("");
		adminGameDescLbl = new Label("");
		adminGamePriceLbl = new Label("");

		adminMenuGameTitleLbl = new Label("Game Title");
		adminMenuGameDescLbl = new Label("Game Description");
		adminMenuGamePriceLbl = new Label("Price");

		addGameBtn = new Button("Add Game");
		updateGameBtn = new Button("Update Game");
		deleteGameBtn = new Button("Delete Game");

		listView = new ListView<>();
		adminMenuBar = new MenuBar();
		adminMenu = new Menu("Log Out");
		adminMenuItem = new MenuItem("Log Out");

		adminRootVB = new VBox();
		adminSP = new ScrollPane();

		games = new ArrayList<>();
	}

	public void initComponent() {
		adminGP2.add(adminGameDescLbl, 0, 0);
		adminGameDescVB.getChildren().addAll(adminGameTitleLbl,adminGP2,adminGamePriceLbl);
		adminGP1.add(listView, 0, 0);
		adminGP1.add(adminGameDescVB, 1, 0);

		adminButtonVB.getChildren().addAll(addGameBtn,updateGameBtn,deleteGameBtn);
		adminGameMenuVB.getChildren().addAll(adminMenuGameTitleLbl,gameTitleTF,adminMenuGameDescLbl,gameDescTA,adminMenuGamePriceLbl,gamePriceTF);
		adminHB.getChildren().addAll(adminGameMenuVB,adminButtonVB);

		adminVB.getChildren().addAll(adminTitleLbl,adminGP1,adminMenuLbl,adminHB);

		adminMenu.getItems().addAll(adminMenuItem);
		adminMenuBar.getMenus().add(adminMenu);

		adminRootVB.getChildren().addAll(adminMenuBar, adminVB);
		adminSP.setContent(adminRootVB);

		refreshListView();
	}


	private void refreshListView() {
		games.clear();
		games = gameController.getGameData();
		ObservableList<String> adminGames = FXCollections.observableArrayList();
		for (Game game : games) {
			adminGames.add(game.getName());
		}
		listView.setItems(adminGames);
	}
	private Game getSelectedGame(String gameName) {
		for (Game game : games) {
			if(game.getName().equals(gameName)) {
				return game;
			}
		}
		return null;
	}
	public void setPosition() {
		this.setCenter(adminSP);
	}

	public void setArrangement() {
		BorderPane.setMargin(adminVB, new Insets(15,5,5,5));
		adminGP1.setHgap(20);
		adminHB.setSpacing(20);
		adminVB.setSpacing(20);
		adminVB.setAlignment(Pos.CENTER);
		adminGameDescVB.setSpacing(20);
		adminGameDescVB.setAlignment(Pos.CENTER);

		adminGP1.setAlignment(Pos.CENTER);
		adminButtonVB.setSpacing(20);
		adminButtonVB.setAlignment(Pos.CENTER_LEFT);
		adminHB.setAlignment(Pos.CENTER);
		adminSP.setFitToWidth(true);
	}
	public void setSize() {
		adminTitleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		adminGameTitleLbl.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		adminMenuLbl.setFont(Font.font("Arial", FontWeight.BOLD, 25));
		adminGameDescLbl.setWrapText(true);
		adminGameDescLbl.setPrefWidth(300);
		gameDescTA.setWrapText(true);

		addGameBtn.setPrefSize(140, 40);
		updateGameBtn.setPrefSize(140,40);
		deleteGameBtn.setPrefSize(140,40);
		addGameBtn.setFont(new Font("Arial",15));
		updateGameBtn.setFont(new Font("Arial",15));
		deleteGameBtn.setFont(new Font("Arial",15));
	}

	public void setMouseHandler() {
		listView.setOnMouseClicked(e->{
			String selectedItem = listView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				Game selectedGame = getSelectedGame(selectedItem);
				if (selectedGame != null) {
					String priceString = String.valueOf(selectedGame.getPrice());
					adminGameTitleLbl.setText(selectedGame.getName());
					adminGameDescLbl.setText(selectedGame.getDescription());
					adminGamePriceLbl.setText("Rp" + priceString);
					gameTitleTF.setText(selectedGame.getName());
					gameDescTA.setText(selectedGame.getDescription());
					gamePriceTF.setText(priceString);
				} else {
					adminGameTitleLbl.setText("");
					adminGameDescLbl.setText("");
					adminGamePriceLbl.setText("");
					gameTitleTF.setText("");
					gameDescTA.setText("");
					gamePriceTF.setText("");
				}
			}
		});
	}

	public void setActionHandler() {
		addGameBtn.setOnAction(e->{
			String gameID = gameController.generateGameID();
			String gameName = gameTitleTF.getText();
			String gameDesc = gameDescTA.getText();
			String gamePrice = gamePriceTF.getText();
			boolean isGameNameUnique = gameController.isGameNameUnique(gameName);
			if(gameName.equals("")) {
				showAlert("No Game Name Warning","You haven't inputted a game name","Please fill the game title form",AlertType.WARNING);
			}else if(gameDesc.equals("")) {
				showAlert("No Game Description Warning", "You haven't inputted a game description","Please fill the game description form",AlertType.WARNING);			
			}else if(gamePrice.equals("")) {
				showAlert("No Game Price Warning","You haven't inputted a game price","Please fill the game price form",AlertType.WARNING);
			}else if(gameDesc.length()>250) {
				showAlert("Invalid Input","Game description is invalid","Game description cannot exceed 250 characters",AlertType.WARNING);
			}else if(gameName.length()>50) {
				showAlert("Invalid Input","Game name is invalid","Game name cannot exceed 50 characters",AlertType.WARNING);
			}else if(gamePrice.length()>10) {
				showAlert("Invalid Input","Price cannot exceed 9999999999","Price is too high",AlertType.WARNING);
			}else if(!gamePrice.matches("^[0-9]+$")) {
				showAlert("Invalid Input","Game price is invalid","Game price must be numeric",AlertType.WARNING);
			}else if(isGameNameUnique==false){
				showAlert("Invalid Input","Game name is invalid","Game name must be unique",AlertType.WARNING);
			}else {
				int gamePriceInt = Integer.parseInt(gamePrice);
				gameController.insertGame(gameID,gameName,gameDesc,gamePriceInt);
				showAlert("Succesful","Successfully registered a new game","",AlertType.INFORMATION);
				gameTitleTF.clear();
				gameDescTA.clear();
				gamePriceTF.clear();
				refreshListView();
			}
		});
		deleteGameBtn.setOnAction(e->{
			String selectedGameName = listView.getSelectionModel().getSelectedItem();
			if(selectedGameName!=null) {
				Game selectedGame = getSelectedGame(selectedGameName);
				if(selectedGame!=null) {
					String gameID = selectedGame.getGameID();
					Alert conf = new Alert(AlertType.CONFIRMATION);
					conf.setTitle("Delete Game");
					conf.setHeaderText("Are you sure you want to delete this game?");
					Optional<ButtonType>result = conf.showAndWait();
					if(result.get()==ButtonType.OK) {
						gameController.deleteGame(gameID);
						refreshListView();
						gameTitleTF.clear();
						gameDescTA.clear();
						gamePriceTF.clear();
						adminGameTitleLbl.setText("");
						adminGameDescLbl.setText("");
						adminGamePriceLbl.setText("");
					}
				}
			}else {
				showAlert("No Games Selected","Please select one of the games","",AlertType.WARNING);
			}
		});
		updateGameBtn.setOnAction(e->{
			String selectedGameName = listView.getSelectionModel().getSelectedItem();
			if(selectedGameName!=null) {
				Game selectedGame = getSelectedGame(selectedGameName);
				if(selectedGame!=null) {
					String gameID = selectedGame.getGameID();
					String gameName = gameTitleTF.getText();
					String gameDesc = gameDescTA.getText();
					String gamePrice = gamePriceTF.getText();

					if(gameName.equals("")&&gameDesc.equals("")&&gamePrice.equals("")) {
						showAlert("Invalid Input","All forms are empty","Please fill all the forms",AlertType.WARNING);
					}else if(gameDesc.length()>250) {
						showAlert("Invalid Input","Invalid Game Description","Game description cannot exceed 250 characters",AlertType.WARNING);
					}else if(gameName.length()>50) {
						showAlert("Invalid Input","Invalid Game Name","Game name cannot exceed 50 characters",AlertType.WARNING);
					}else if(!gamePrice.isEmpty()&&!gamePrice.matches("^[0-9]+$")) {
						showAlert("Invalid Input","Invalid Game Price","Game price must be numeric",AlertType.WARNING);
					}else if(gamePrice.length()>10) {
						showAlert("Invalid Input","Game price is invalid","Game price cannot exceed 10 characters",AlertType.WARNING);
					}else {
						if(gameName.equals("")) {
							gameName = selectedGame.getName();
						}
						if(gameDesc.equals("")) {
							gameDesc = selectedGame.getDescription();
						}
						if(gamePrice.equals("")) {
							gamePrice= String.valueOf(selectedGame.getPrice());
						}
						int gamePriceInt = Integer.parseInt(gamePrice);
						gameController.updateGame(gameName,gameDesc,gamePriceInt,gameID);							
						showAlert("Succesful","Successfully updated a game","",AlertType.INFORMATION);
						refreshListView();
						gameTitleTF.clear();
						gameDescTA.clear();
						gamePriceTF.clear();
						adminGameTitleLbl.setText("");
						adminGameDescLbl.setText("");
						adminGamePriceLbl.setText("");
					}
				}
			}else {
				showAlert("No Games Selected","Please select one of the games","",AlertType.WARNING);
			}
		});
		adminMenuItem.setOnAction(e->{
			gameTitleTF.clear();
			gameDescTA.clear();
			gamePriceTF.clear();
			adminGameTitleLbl.setText("");
			adminGameDescLbl.setText("");
			adminGamePriceLbl.setText("");
			Main.root.getChildren().clear();
			Main.root.getChildren().add(Main.loginBP);
		});
	}
	private void showAlert(String title, String header, String message, AlertType alertType) {
		alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public AdminHomePage() {
		init();
		initComponent();
		setPosition();
		setArrangement();
		setSize();
		setMouseHandler();
		setActionHandler();
	}
}

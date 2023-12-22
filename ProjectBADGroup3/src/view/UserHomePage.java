package view;

import java.util.ArrayList;

import controller.AuthController;
import controller.CartController;
import controller.GameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Cart;
import model.Game;

public class UserHomePage extends BorderPane{
	Scene cartPopUpScene;
	VBox homeVB, hGameInfoVB, insidePopUpVB;
	GridPane cartGP;
	BorderPane cartPopUpBP;
	Stage cartStage;
	GridPane homeGP, hGameInfoGP;
	Label cHelloLbl,hGameTitleLbl,hGameDescLbl,hGamePriceLbl, puGameTitleLbl, puGameDescLbl,puGamePriceLbl;
	Button addCartBtn, addCartWindowBtn;
	MenuBar hMenuBar;
	Menu hMenu1, hMenu2;
	MenuItem hMenu1Item1, hMenu1Item2, hMenu2Item1;
	ListView<String>listView;
	ArrayList<Game>games;
	GameController gameController;
	Window cartWindow;
	Spinner<Integer> qtySpinner;
	SpinnerValueFactory<Integer> qtyFactory;
	CartController cartController;
	public void init() {
		gameController = new GameController();
		homeVB = new VBox();
		homeGP = new GridPane();
		hGameInfoVB = new VBox();
		hGameInfoGP = new GridPane();

		cHelloLbl = new Label("Hello, "+ AuthController.authUser.getUsername());
		hGameTitleLbl = new Label("");
		hGameDescLbl= new Label("");
		hGamePriceLbl = new Label("");

		addCartBtn = new Button("Add To Cart");

		hMenuBar = new MenuBar();
		hMenu1 = new Menu("Dashboard");
		hMenu1Item1 = new MenuItem("Home");
		hMenu1Item2 = new MenuItem("Cart");
		hMenu2 = new Menu("Log Out");
		hMenu2Item1 = new MenuItem("Log Out");
		listView = new ListView<>();
		games = new ArrayList<>();
	}
	public void initComponent() {
		addCartBtn.setVisible(false);
		hGameInfoGP.add(hGameTitleLbl, 0, 0);
		hGameInfoGP.add(hGameDescLbl, 0, 1);
		hGameInfoVB.getChildren().addAll(hGameInfoGP,hGamePriceLbl,addCartBtn);

		homeGP.add(listView, 0, 0);
		homeGP.add(hGameInfoVB, 1, 0);
		homeVB.getChildren().addAll(cHelloLbl,homeGP);

		hMenu1.getItems().addAll(hMenu1Item1,hMenu1Item2);
		hMenu2.getItems().add(hMenu2Item1);
		hMenuBar.getMenus().addAll(hMenu1,hMenu2);	
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
	public void setPosition() {
		this.setTop(hMenuBar);
		this.setCenter(homeVB);
	}
	public void setArrangement() {
		BorderPane.setMargin(homeVB, new Insets(15,0,0,0));
		homeGP.setAlignment(Pos.CENTER);
		homeGP.setHgap(10);
		homeVB.setSpacing(20);
		homeVB.setAlignment(Pos.TOP_CENTER);

		hGameInfoGP.setVgap(10);
		hGameInfoVB.setSpacing(10);
		hGameInfoVB.setAlignment(Pos.CENTER);
	}
	public void setSize() {
		cHelloLbl.setFont(Font.font("Arial",FontWeight.BOLD,50));
		hGameTitleLbl.setFont(Font.font("Arial",FontWeight.BOLD,30));
		hGameTitleLbl.setWrapText(true);
		hGameDescLbl.setWrapText(true);
		hGameDescLbl.setPrefWidth(350);
		hGameTitleLbl.setPrefWidth(350);
		addCartBtn.setPrefSize(300,40);
	}

	public void cartWindow() {
		cartController = new CartController();
		cartStage = new Stage();
		cartPopUpBP = new BorderPane();
		cartGP = new GridPane();
		cartWindow = new Window("Add To Cart");
		cartPopUpBP.setCenter(cartWindow);
		cartPopUpScene = new Scene(cartPopUpBP,300,400);
		qtySpinner = new Spinner<>();
		qtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
		addCartWindowBtn = new Button("Add To Cart");

		insidePopUpVB = new VBox();
		puGameTitleLbl = new Label("");
		puGameDescLbl = new Label("");
		puGamePriceLbl = new Label("");

		cartGP.add(puGameDescLbl, 0, 0);
		insidePopUpVB.getChildren().addAll(puGameTitleLbl,cartGP,puGamePriceLbl,qtySpinner,addCartWindowBtn);
		cartWindow.getContentPane().getChildren().add(insidePopUpVB);

		insidePopUpVB.setPadding(new Insets(10));
		insidePopUpVB.setSpacing(15);
		puGameTitleLbl.setFont(Font.font("Arial",FontWeight.BOLD,23));
		puGameDescLbl.setFont(Font.font("Arial",16));
		puGamePriceLbl.setFont(Font.font("Arial",16));
		puGameDescLbl.setWrapText(true);
		puGameTitleLbl.setWrapText(true);


		cartWindow.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		insidePopUpVB.setAlignment(Pos.CENTER);

		String selectedItem = listView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			Game selectedGame = getSelectedGame(selectedItem);
			if (selectedGame != null) {
				puGameTitleLbl.setText(selectedGame.getName());
				puGameDescLbl.setText(selectedGame.getDescription());
				puGamePriceLbl.setText("Rp"+selectedGame.getPrice());

				ArrayList<Cart> cartData = cartController.getCartDataByUserID(AuthController.authUser.getUserID());
				for (Cart cart : cartData) {
					if(cart.getGameID().equals(selectedGame.getGameID())) {
						qtySpinner.getValueFactory().setValue(cart.getQuantity());
						break;
					}
				}
			}
		}

		cartStage.setResizable(false);
		cartStage.setScene(cartPopUpScene);
		cartStage.show();
		
		addCartWindowBtn.setOnAction(e->{
			int quantity = qtySpinner.getValue();
			String selectedGameName = listView.getSelectionModel().getSelectedItem();
			Game selectedGame = getSelectedGame(selectedGameName);
			ArrayList<Cart> cartData = cartController.getCartDataByUserID(AuthController.authUser.getUserID());
			if(quantity==0 && selectedGameName!=null) {
				for (Cart cart : cartData) {
					if(cart.getGameID().equals(selectedGame.getGameID())) {
						cartController.deleteCart(AuthController.authUser.getUserID(), selectedGame.getGameID());
						break;
					}
				}
			}else if(quantity!=0 && selectedGame !=null) {
				boolean isGameInCart = false;
				for (Cart cart : cartData) {
					if(cart.getGameID().equals(selectedGame.getGameID())) {
						cartController.updateCart(AuthController.authUser.getUserID(), selectedGame.getGameID(), quantity);
						isGameInCart = true;
						break;
					}
				}
				if(!isGameInCart) {
					cartController.insertCart(AuthController.authUser.getUserID(), selectedGame.getGameID(), quantity);
				}
			}
			cartStage.close();
		});

	}
	

	private Game getSelectedGame(String gameName) {
		for (Game game : games) {
			if(game.getName().equals(gameName)) {
				return game;
			}
		}
		return null;
	}
	public void setMouseHandler() {
		listView.setOnMouseClicked(e->{
			String selectedItem = listView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				Game selectedGame = getSelectedGame(selectedItem);
				if (selectedGame != null) {
					String priceString = String.valueOf(selectedGame.getPrice());
					hGameTitleLbl.setText(selectedGame.getName());
					hGameDescLbl.setText(selectedGame.getDescription());
					hGamePriceLbl.setText("Rp" + priceString);
					addCartBtn.setVisible(true);
				} else {
					hGameTitleLbl.setText("");
					hGameDescLbl.setText("");
					hGamePriceLbl.setText("");
				}
			}
		});
	}

	public void setActionHandler() {

		hMenu1Item1.setOnAction(e->{
			Main.root.getChildren().clear();
			Main.root.getChildren().add(this);
		});
		hMenu1Item2.setOnAction(e->{
			Main.root.getChildren().clear();
			Main.root.getChildren().add(new CartPage());
		});
		hMenu2Item1.setOnAction(e->{
			Main.root.getChildren().clear();
			Main.root.getChildren().add(Main.loginBP);
		});
		addCartBtn.setOnAction(e->{
			cartWindow();
		});
	}
	public UserHomePage(){
		init();
		initComponent();
		setPosition();
		setArrangement();
		setSize();
		setActionHandler();
		setMouseHandler();
	}
}

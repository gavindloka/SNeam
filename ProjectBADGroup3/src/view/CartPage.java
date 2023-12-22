package view;

import java.util.ArrayList;

import controller.AuthController;
import controller.CartController;
import controller.GameController;
import controller.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Cart;
import model.Game;

public class CartPage extends BorderPane {
	VBox cartVB;
	MenuBar menuBar;
	Menu menu1, menu2;
	MenuItem menu1Item1, menu1Item2, menu2Item1;
	Label yourCartLbl, grossPriceLbl;
	Button checkOutBtn;
	TableView<Game> gameTable;
	CartController cartController;
	GameController gameController;
	TransactionController transactionController;
	int totalPrice = 0;
	Alert alert;
	ArrayList<Cart> cartList;
	ObservableList<Game> gameData;
	

	public void init() {
		cartController = new CartController();
		gameController = new GameController();
		transactionController = new TransactionController();
		cartVB = new VBox();
		yourCartLbl = new Label("Your Cart");
		grossPriceLbl = new Label("");
		checkOutBtn = new Button("Check Out");

		menuBar = new MenuBar();
		menu1 = new Menu("Dashboard");
		menu1Item1 = new MenuItem("Home");
		menu1Item2 = new MenuItem("Cart");
		menu2 = new Menu("Log Out");
		menu2Item1 = new MenuItem("Log Out");

		gameTable = new TableView<Game>();

	}

	public void initComponent() {
		cartVB.getChildren().addAll(yourCartLbl,gameTable,grossPriceLbl,checkOutBtn);

		menu1.getItems().addAll(menu1Item1, menu1Item2);
		menu2.getItems().add(menu2Item1);
		menuBar.getMenus().addAll(menu1, menu2);
	}

	public void setPosition() {
		this.setTop(menuBar);
		this.setCenter(cartVB);
	}

	public void setArrangement() {
		cartVB.setAlignment(Pos.CENTER);
		cartVB.setSpacing(10);
	}
	public void setSize() {
		yourCartLbl.setFont(Font.font("Arial",FontWeight.BOLD,30));
		grossPriceLbl.setFont(Font.font("Arial",33));
		checkOutBtn.setPrefSize(180, 50);
		checkOutBtn.setFont(Font.font("Arial",20));
	}
	private void setTable() {
		gameTable.setMaxWidth(500);

		TableColumn<Game, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setMinWidth(0.55 * gameTable.getMaxWidth() - 1);

		TableColumn<Game, Integer> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceColumn.setMinWidth(0.15 * gameTable.getMaxWidth() - 1);

		TableColumn<Game, Integer> quantityColumn = new TableColumn<>("Quantity");
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		quantityColumn.setMinWidth(0.15 * gameTable.getMaxWidth() -1);

		TableColumn<Game, Integer> totalColumn = new TableColumn<>("Total");
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
		totalColumn.setMinWidth(0.15 * gameTable.getMaxWidth() -1);

		gameTable.getColumns().addAll(nameColumn,priceColumn,quantityColumn,totalColumn);
		cartList = cartController.getCartDataByUserID(AuthController.authUser.getUserID());
		gameData = FXCollections.observableArrayList();
		for (Cart cart : cartList) {
			String gameID = cart.getGameID();
			Game game = gameController.getGameDataByID(gameID);
			game.setQuantity(cart.getQuantity());
			game.setTotal(cart.getQuantity()*game.getPrice());
			totalPrice += game.getTotal();
			grossPriceLbl.setText("Gross Price: Rp "+totalPrice);
			gameData.add(game);
		}
		gameTable.setItems(gameData);

	}

	private void clearTable() {
		cartList = cartController.getCartDataByUserID(AuthController.authUser.getUserID());
		gameData = FXCollections.observableArrayList();
        totalPrice = 0;
        cartList.clear();
        gameData.clear();
        grossPriceLbl.setText("Gross Price: Rp "+totalPrice);
        gameTable.setItems(gameData);
    }
	
	public void setActionHandler() {
		checkOutBtn.setOnAction(e->{
			if(cartList.isEmpty()) {
				showAlert("Invalid Request","Cart is empty","Transaction Not Possible", AlertType.WARNING);
			}else {
				String transactionID = transactionController.generateTransactionID();
				transactionController.insertTransactionHeader(transactionID, AuthController.authUser.getUserID());
				for (Cart cart : cartList) {
					transactionController.insertTransactionDetail(transactionID,cart.getGameID(), cart.getQuantity());
				}
				transactionController.clearCart(AuthController.authUser.getUserID());
				clearTable();
				showAlert("Successful","Checkout Successful","Thank you for the purchase",AlertType.INFORMATION);
			}
		});
		menu1Item1.setOnAction(e->{
			Main.root.getChildren().clear();
			Main.root.getChildren().add(new UserHomePage());
		});
		menu1Item2.setOnAction(e->{
			Main.root.getChildren().clear();
			Main.root.getChildren().add(this);
		});
		menu2Item1.setOnAction(e->{
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
	public CartPage()
	{
		init();
		initComponent();
		setPosition();
		setActionHandler();
		setArrangement();
		setSize();
		setTable();
	}
}

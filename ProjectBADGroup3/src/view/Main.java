package view;

import controller.AuthController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import util.Connect;

public class Main extends Application{
	public static StackPane root;
	public static BorderPane loginBP;
	Stage stage;
	Scene loginScene;
	VBox loginVB;
	GridPane loginGP;
	Label loginLbl, emailLbl, passLbl;
	TextField emailTF;
	PasswordField passPF;
	Button signInBtn;
	MenuBar menuBar;
	Menu menu;
	MenuItem menuItem1, menuItem2;
	Alert alert;
	AuthController authController;

	public void init() {
		root = new StackPane();
		authController = new AuthController();
		loginBP = new BorderPane();
		loginVB = new VBox();
		loginGP = new GridPane();
		loginScene = new Scene(root,700,700);
		loginLbl = new Label("LOGIN");
		emailLbl = new Label("Email");
		passLbl = new Label("Password");
		emailTF = new TextField();
		passPF = new PasswordField();
		signInBtn = new Button("Sign In");
		menuBar = new MenuBar();
		menu = new Menu("Menu");
		menuItem1 = new MenuItem("Login");
		menuItem2 = new MenuItem("Register");

	}

	public void initComponent() {
		root.getChildren().add(loginBP);
		loginGP.add(emailLbl, 0, 0);
		loginGP.add(emailTF, 0, 1);
		loginGP.add(passLbl, 0, 2);
		loginGP.add(passPF, 0, 3);
		loginVB.getChildren().addAll(loginLbl,loginGP,signInBtn);
		menu.getItems().addAll(menuItem1,menuItem2);
		menuBar.getMenus().add(menu);
	}
	public void setPosition() {
		loginBP.setCenter(loginVB);
		loginBP.setTop(menuBar);
	}
	public void setSize() {
		emailTF.setPrefWidth(200);
		passPF.setPrefWidth(200);
		loginLbl.setFont(Font.font("Arial",FontWeight.BOLD,50));
		signInBtn.setFont(Font.font("Arial",15));
		signInBtn.setPrefSize(80, 40);
	}

	public void setArrangement() {
		loginGP.setAlignment(Pos.CENTER);
		loginGP.setVgap(5);
		loginVB.setSpacing(20);
		loginVB.setAlignment(Pos.CENTER);
	}

	public void setActionEvent() {
		menuItem1.setOnAction(e->{
			root.getChildren().clear();
			root.getChildren().add(loginBP);
		});
		menuItem2.setOnAction(e->{
			emailTF.clear();
			passPF.clear();
			root.getChildren().clear();
			root.getChildren().add(new Register());
		});
		signInBtn.setOnAction(e->{
			String email = emailTF.getText();
			String pass = passPF.getText();
			boolean isCredentialValid = authController.isCredentialValid(email, pass);
			if(isCredentialValid) {
				String loggedInUsername = authController.loggedInUser(email);
				String loggedInUserID = authController.loggedInUserID(email);
				String loggedInUserRole = authController.loggedInUserRole(email);
				AuthController.authUser = new User();
				AuthController.authUser.setUsername(loggedInUsername);
				AuthController.authUser.setEmail(email);
				AuthController.authUser.setUserID(loggedInUserID);
				if(loggedInUserRole.equals("admin")) {
					emailTF.clear();
					passPF.clear();
					root.getChildren().clear();
					root.getChildren().add(new AdminHomePage());
				}else {
					emailTF.clear();
					passPF.clear();
					root.getChildren().clear();
					root.getChildren().add(new UserHomePage());					
				}
			}else {
				showAlert("Invalid Request", "Wrong Credentials","Email or password is invalid",AlertType.WARNING);
			}

		});
	}

	private void showAlert(String title, String header, String message, AlertType alertType) {
		alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		init();
		initComponent();
		setPosition();
		setArrangement();
		setSize();
		setActionEvent();

		stage = primaryStage;
		stage.setResizable(false);
		stage.setTitle("SNeam");
		stage.setScene(loginScene);
		stage.show();
	}
}



package view;

import controller.AuthController;
import javafx.geometry.Pos;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Register extends BorderPane{
	Label registerLbl ,unameLbl, rEmailLbl, rPassLbl,confirmLbl,phoneLbl;
	VBox registerVB;
	GridPane registerGP;
	MenuBar rMenuBar;
	MenuItem rMenuItem1, rMenuItem2;
	Menu rMenu;
	TextField unameTF, rEmailTF, phoneTF;
	PasswordField rPassPF, confirmPF;
	Button signUpBtn;
	Alert alert;
	AuthController authController;
	public void init() {
		authController = new AuthController();
		registerGP = new GridPane();
		registerVB = new VBox();	
		
		registerLbl = new Label("REGISTER");
		unameLbl = new Label("Username");
		rEmailLbl = new Label("Email");
		rPassLbl = new Label("Password");
		confirmLbl = new Label("Confirm Password");
		phoneLbl = new Label("Phone Number");
		
		unameTF = new TextField();
		rEmailTF = new TextField();
		rPassPF = new PasswordField();
		confirmPF = new PasswordField();
		phoneTF = new TextField();
		
		signUpBtn = new Button("Sign Up");
		rMenuBar = new MenuBar();
		rMenu = new Menu("Menu");
		rMenuItem1 = new MenuItem("Login");
		rMenuItem2 = new MenuItem("Register");
	}
	public void initComponent() {
		registerGP.add(unameLbl,0,0);
		registerGP.add(unameTF, 0, 1);
		registerGP.add(rEmailLbl,0, 2);
		registerGP.add(rEmailTF, 0, 3);
		registerGP.add(rPassLbl, 0, 4);
		registerGP.add(rPassPF, 0, 5);
		registerGP.add(confirmLbl, 0, 6);
		registerGP.add(confirmPF, 0, 7);
		registerGP.add(phoneLbl, 0, 8);
		registerGP.add(phoneTF, 0, 9);
		registerVB.getChildren().addAll(registerLbl,registerGP,signUpBtn);
		rMenu.getItems().addAll(rMenuItem1,rMenuItem2);
		rMenuBar.getMenus().add(rMenu);
	}
	public void setArrangement() {
		registerGP.setAlignment(Pos.CENTER);
		registerGP.setVgap(5);
		registerVB.setSpacing(20);
		registerVB.setAlignment(Pos.CENTER);
	}
	public void setPosition() {
		this.setCenter(registerVB);
		this.setTop(rMenuBar);
	}
	public void setSize() {
		unameTF.setPrefWidth(200);
		rEmailTF.setPrefWidth(200);
		rPassPF.setPrefWidth(200);
		confirmPF.setPrefWidth(200);
		phoneTF.setPrefWidth(200);
		registerLbl.setFont(Font.font("Arial",FontWeight.BOLD,50));
		signUpBtn.setFont(Font.font("Arial",15));
		signUpBtn.setPrefSize(80, 40);
	}
	public void setActionHandler() {
		rMenuItem1.setOnAction(e->{
			unameTF.clear();
			rEmailTF.clear();
			rPassPF.clear();
			confirmPF.clear();
			phoneTF.clear();
			Main.root.getChildren().clear();
			Main.root.getChildren().add(Main.loginBP);
		});
		rMenuItem2.setOnAction(e->{
			Main.root.getChildren().clear();
			Main.root.getChildren().add(this);
		});
		signUpBtn.setOnAction(e->{
			AuthController authController = new AuthController();
			String userID = authController.generateUserID();
			String uname = unameTF.getText();
			String rEmail = rEmailTF.getText();
			String rPass = rPassPF.getText();
			String confirm = confirmPF.getText();
			String phone = phoneTF.getText();
			String role = "customer";
			
			boolean isEmailUnique = authController.isEmailUnique(rEmail);
			
			if(uname.length()<4||uname.length()>20) {
				showAlert("Invalid Input","Username is invalid","Username must contain 4 - 20 characters",AlertType.WARNING);
			}else if(!rEmail.contains("@")) {
				showAlert("Invalid Input","Email is invalid","Email must contains \"@\" in it",AlertType.WARNING);
			}else if(isEmailUnique==false) {
				showAlert("Invalid Input","Email is invalid","Email must be unique",AlertType.WARNING);
			}else if(rPass.length()<6||rPass.length()>20) {
				showAlert("Invalid Input","Password is invalid","Password must contain 6-20 characters",AlertType.WARNING);
			} else if(!rPass.matches("^[a-zA-Z0-9]+$")) {
				showAlert("Invalid Input","Password is invalid","Password must be alphanumeric",AlertType.WARNING);
			}else if(!confirm.equals(rPass)) {
				showAlert("Invalid Input","Password is invalid","Confirm Password must be the same as Password",AlertType.WARNING);
			}else if(!phone.matches("^[0-9]+$")) {
				showAlert("Invalid Input","Phone Number is invalid","Phone Number can only be numeric",AlertType.WARNING);
			}else if(phone.length()<9||phone.length()>20) {
				showAlert("Invalid Input","Phone Number is invalid","Phone Number must be 9-20 numbers",AlertType.WARNING);
			}else {
				authController.insertUser(userID,uname,rPass,phone,rEmail,role);
				showAlert("Succesful","Successfully registered a new account","",AlertType.INFORMATION);
				unameTF.clear();
				rEmailTF.clear();
				rPassPF.clear();
				confirmPF.clear();
				phoneTF.clear();
				Main.root.getChildren().clear();
				Main.root.getChildren().add(Main.loginBP);
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
	public Register() {
		init();
		initComponent();
		setPosition();
		setArrangement();
		setSize();
		setActionHandler();
	}
}

package com.gylang.netty.client.gui;

/**
 * @author gylang
 * data 2021/4/1
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;

public class Login extends Application {
    @FXML
    public Button loginBtn;
    @FXML
    private Button registerBtn;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            // Read file fxml and draw interface.
            URL resource = getClass().getResource("/Login.fxml");
            System.out.println(resource);
            Parent root = FXMLLoader.load(resource);

            primaryStage.setTitle("My Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
//更多请阅读：https://www.yiibai.com/javafx/javafx-tutorial-for-beginners.html


    }

    public void doLogin(ActionEvent actionEvent) {
        System.out.println(username.getText());
        System.out.println(password.getText());
        System.out.println(actionEvent);
    }
}

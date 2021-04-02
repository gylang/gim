package com.gylang.netty.client.gui.controller;

import com.gylang.netty.client.gui.util.GuiUtil;
import io.datafx.controller.ViewController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;

/**
 * @author gylang
 * data 2021/4/2
 */
@ViewController("/fxml/main.fxml")
@Slf4j
public class MainController extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {


        final Stage main = new Stage();
        URL resource = getClass().getResource("/fxml/Main.fxml");
        log.info("[start] : {}", resource);
        Parent root = FXMLLoader.load(resource);
        main.setTitle("主页");
        main.setScene(new Scene(root));
        main.show();
    }

    public void openPrivateChat(ActionEvent actionEvent) {
        GuiUtil.openNewView(PrivateChatController.class);
    }
}

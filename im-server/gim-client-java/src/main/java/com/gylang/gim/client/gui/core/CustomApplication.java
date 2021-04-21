package com.gylang.gim.client.gui.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/4
 */

@Slf4j
public abstract class CustomApplication extends Application implements InitializerCallBack {

    private String title;

    private String url;

    public CustomApplication(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        final Stage main = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));

        loader.setController(this);
        Parent load = loader.load();

        main.setTitle(title);
        main.setScene(new Scene(load));
        main.show();

    }

    @Override
    public void init(URL location, ResourceBundle resources) {

    }

    @Override
    public void beforeInit(URL location, ResourceBundle resources) {

    }

    @Override
    public void afterInit(URL location, ResourceBundle resources) {

    }
}

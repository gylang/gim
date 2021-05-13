package com.gylang.gim.client.gui.core;

import com.gylang.gim.client.gui.GuiStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
public abstract class CustomApplication extends Application implements Initializable {

    private String title;

    private String url;

    private Stage stage;

    protected CustomApplication(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
        Object t1 = this;
        loader.setController(t1);
        Parent load = loader.load();
        stage.setTitle(title);
        stage.setScene(new Scene(load));
        stage.show();
    }


    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        GuiStore.store(this, stage);
        beforeInit(location, resources);
        init(location, resources);
        afterInit(location, resources);
    }

    /**
     * 初始化前
     *
     * @param location
     * @param resources
     */
    public void beforeInit(URL location, ResourceBundle resources) {
    }

    /**
     * 初始化后
     *
     * @param location
     * @param resources
     */
    public void init(URL location, ResourceBundle resources) {
    }

    /**
     * 初始化后
     *
     * @param location
     * @param resources
     */
    public void afterInit(URL location, ResourceBundle resources) {
    }

}

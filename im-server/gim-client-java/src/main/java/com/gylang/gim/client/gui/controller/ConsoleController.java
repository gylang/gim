package com.gylang.gim.client.gui.controller;

import com.alibaba.fastjson.JSON;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.client.gui.core.CustomApplication;
import com.gylang.gim.client.gui.util.GuiUtil;
import com.gylang.gim.remote.SocketHolder;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/5/6
 */
public class ConsoleController extends CustomApplication {

    @FXML
    private TextField textField;
    @FXML
    private ListView<TextField> userListView;

    public ConsoleController() {
        super("操作台", "/fxml/ConsoleController.fxml");
    }

    @Override
    public void beforeInit(URL location, ResourceBundle resources) {

    }

    @Override
    public void init(URL location, ResourceBundle resources) {

        userListView.setItems(FXCollections.observableList(new LinkedList<>()));

        SocketHolder.getInstance().globalBind(msg -> GuiUtil.update(() -> {
            TextField textField = new TextField();
            int type =  msg.getType();
            Color color = new Color(type, type, type, type);
            Background background = new Background(new BackgroundFill(color, null, null));
            textField.setBackground(background);
            userListView.getItems().add(textField);
            userListView.refresh();
        }));
    }

    @Override
    public void afterInit(URL location, ResourceBundle resources) {

    }

    @FXML
    public void send() {
        String text = textField.getText();
        SocketHolder.getInstance().send(JSON.parseObject(text, MessageWrap.class));
        textField.setText("");
    }
}

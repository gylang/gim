package com.gylang.gim.client.gui.controller;

import com.gylang.gim.client.gui.core.CustomApplication;
import com.gylang.gim.client.gui.util.GuiUtil;
import io.datafx.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 功能分类主页
 *
 * @author gylang
 * data 2021/4/2
 */
@ViewController("/fxml/main.fxml")
@Slf4j
public class MainController extends CustomApplication {


    public MainController() {
        super("功能菜单", "/fxml/Main.fxml");
    }


    @FXML
    public void userSearchEvent(ActionEvent actionEvent) {
        GuiUtil.openNewView(SearchUserController.class);
    }
    @FXML
    public void openLog(ActionEvent actionEvent) {

    }
    @FXML
    public void openFriendApply(ActionEvent actionEvent) {

    }
    @FXML
    public void openNotify(ActionEvent actionEvent) {

    }
    @FXML
    public void openGroupChat(ActionEvent actionEvent) {

    }
    @FXML
    public void openFriend(ActionEvent actionEvent) {
        GuiUtil.openNewView(UserIndexController.class);
    }
    @FXML
    public void groupSearchEvent(ActionEvent actionEvent) {

    }
    @FXML
    public void openconsole(ActionEvent actionEvent) {
        GuiUtil.openNewView(new ConsoleController());
    }

    @Override
    public void beforeInit(URL location, ResourceBundle resources) {

    }

    @Override
    public void init(URL location, ResourceBundle resources) {

    }

    @Override
    public void afterInit(URL location, ResourceBundle resources) {

    }
}

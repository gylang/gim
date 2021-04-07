package com.gylang.gim.client.gui.controller;

import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.client.enums.MockKey;
import com.gylang.gim.client.util.MockUtil;
import com.gylang.gim.client.gui.component.list.UserItemListComponent;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/3
 */
@Slf4j
public class UserIndexController extends Application implements Initializable {

    public ListView<ImUserFriendDTO> userListView;


    @Override
    public void start(Stage primaryStage) throws Exception {


        final Stage main = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/UserIndex.fxml"));
//        URL resource = getClass().getResource("/fxml/UserIndex.fxml");
        log.info("[start] : {}", fxmlLoader);
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        main.setTitle("好友");
        main.setScene(new Scene(root));
        main.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        userListView.setCellFactory(callback -> new UserItemListComponent());
        List<ImUserFriendDTO> chatMsgList = MockUtil.mock(new TypeReference<List<ImUserFriendDTO>>() {
        })
                .actual(new ArrayList<>())
                .mockK(MockKey.USER_ITEM_LIST.getKey())
                .get();
        userListView.setItems(FXCollections.observableList(chatMsgList));
    }
}

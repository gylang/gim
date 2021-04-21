package com.gylang.gim.client.gui.controller;

import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.client.enums.MockKey;
import com.gylang.gim.client.gui.component.UserItemC;
import com.gylang.gim.client.gui.core.CustomApplication;
import com.gylang.gim.client.util.MockUtil;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 用户主页
 *
 * @author gylang
 * data 2021/4/3
 */
@Slf4j
public class UserIndexController extends CustomApplication implements Initializable {

    public ListView<ImUserFriendDTO> userListView;

    public UserIndexController() {
        super("用户列表", "/fxml/UserIndex.fxml");
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        userListView.setCellFactory(callback -> new UserItemC());
        List<ImUserFriendDTO> chatMsgList = MockUtil.mock(new TypeReference<List<ImUserFriendDTO>>() {
        })
                .actual(new ArrayList<>())
                .mockK(MockKey.USER_ITEM_LIST.getKey())
                .get();
        userListView.setItems(FXCollections.observableList(chatMsgList));
    }
}

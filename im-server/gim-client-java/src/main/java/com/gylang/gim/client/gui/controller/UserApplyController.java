package com.gylang.gim.client.gui.controller;

import com.gylang.gim.api.dto.UserApplyDTO;
import com.gylang.gim.client.gui.core.CustomApplication;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/22
 */
public class UserApplyController extends CustomApplication {

    @FXML
    private ListView<UserApplyDTO> userListView;


    public UserApplyController() {
        super("用户申请通知", "/fxml/UserIndex.fxml");
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

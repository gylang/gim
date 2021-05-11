package com.gylang.gim.client.gui.controller;

import com.gylang.gim.api.dto.UserApplyDTO;
import com.gylang.gim.client.gui.core.CustomApplication;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

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



}

package com.gylang.gim.client.gui.component;

import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.client.GuiClientApplication;
import com.gylang.gim.client.gui.controller.PrivateChatController;
import com.gylang.gim.client.gui.util.GuiUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class UserItemC extends BaseCellC<ImUserFriendDTO> {


    public ImageView avatar;
    public TextField nickname;
    public TextField username;

    public UserItemC() {
        super(GuiClientApplication.class.getResource("/fxml/component/UserItem.fxml"));
    }


    @Override
    public void bindData(ImUserFriendDTO item) {

        nickname.setText(item.getNickname());
        username.setText(item.getUsername());
    }

    public void openChat(ActionEvent actionEvent) {
        log.info("{}", actionEvent.getSource());
        ImUserFriendDTO item = getItem();
        GuiUtil.openNewView(new PrivateChatController(item));
    }



    public void delFriendAction(ActionEvent actionEvent) {

        log.info("{}", actionEvent.getSource());
        ImUserFriendDTO item = getItem();
    }
}

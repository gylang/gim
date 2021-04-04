package com.gylang.netty.client.gui.component.list;

import com.gylang.im.api.domain.Kv;
import com.gylang.im.api.dto.ImUserFriendDTO;
import com.gylang.netty.client.GuiClientApplication;
import com.gylang.netty.client.gui.controller.PrivateChatController;
import com.gylang.netty.client.gui.util.GuiUtil;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class UserItemListComponent extends BaseCell<ImUserFriendDTO> {


    public ImageView avatar;
    public TextField nickname;
    public TextField username;

    public UserItemListComponent() {
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
}

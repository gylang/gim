package com.gylang.gim.client.gui.component.list;

import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.client.GuiClientApplication;
import com.gylang.gim.client.gui.controller.PrivateChatController;
import com.gylang.gim.client.gui.util.GuiUtil;
import javafx.event.ActionEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class UserNotifyComponent extends BaseCell<ImUserFriendDTO> {




    public UserNotifyComponent() {
        super(GuiClientApplication.class.getResource("/fxml/component/MessageNotify.fxml"));
    }


    @Override
    public void bindData(ImUserFriendDTO item) {


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

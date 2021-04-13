package com.gylang.gim.client.gui.component.list;

import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.dto.request.UserApplyRequest;
import com.gylang.gim.api.enums.AnswerType;
import com.gylang.gim.client.GuiClientApplication;
import com.gylang.gim.client.api.FriendApi;
import com.gylang.gim.client.gui.controller.PrivateChatController;
import com.gylang.gim.client.gui.util.GuiUtil;
import com.gylang.gim.client.util.HttpUtil;
import com.gylang.gim.client.util.store.UserStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class FriendApplyNotifyComponent extends BaseCell<ImUserFriendDTO> {

    @FXML
    private ImageView avatar;
    @FXML
    private TextField nickname;
    @FXML
    private TextField username;

    FriendApi friendApi = HttpUtil.getApi(FriendApi.class);

    public FriendApplyNotifyComponent() {
        super(GuiClientApplication.class.getResource("/fxml/component/FriendApplyNotify.fxml"));
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

    @FXML
    public void aggree(ActionEvent event) {
        UserApplyRequest request = new UserApplyRequest();
        ImUserFriendDTO item = getItem();
        request.setApplyId(UserStore.getInstance().getUid());
        request.setAnswerId(item.getFriendId());
        request.setAnswerType(AnswerType.AGREEMENT);
        friendApi.answer(request);
    }

    @FXML
    public void reject(ActionEvent event) {
        UserApplyRequest request = new UserApplyRequest();
        ImUserFriendDTO item = getItem();
        request.setApplyId(UserStore.getInstance().getUid());
        request.setAnswerId(item.getFriendId());
        request.setAnswerType(AnswerType.REJECT);
        friendApi.answer(request);
    }
}

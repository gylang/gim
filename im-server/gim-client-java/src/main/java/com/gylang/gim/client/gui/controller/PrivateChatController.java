package com.gylang.gim.client.gui.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.constant.cmd.PrivateChatCmd;
import com.gylang.gim.api.domain.chat.ChatMsg;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.dto.ImUserFriendDTO;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.client.enums.MockKey;
import com.gylang.gim.client.gui.component.ChatListItemC;
import com.gylang.gim.client.gui.core.CustomApplication;
import com.gylang.gim.client.gui.dialog.CommonDialog;
import com.gylang.gim.client.util.MockUtil;
import com.gylang.gim.remote.SocketHolder;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 聊天主页
 *
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
@Setter
public class PrivateChatController extends CustomApplication {


    @FXML
    public ListView<ChatMsg> chatListView;
    @FXML
    public TextArea inputText;
    @FXML
    public TextField nicknameView;
    @FXML
    public Button send;
    private String userId;

    private String nickname;

    private String username;

    public PrivateChatController(ImUserFriendDTO friend) {
        super("私聊", "/fxml/PrivateChat.fxml");
        this.userId = friend.getUid();
        this.username = friend.getUsername();
        this.nickname = friend.getNickname();
    }



    private void loadMsg() {


    }

    @FXML
    public void sendMsg(ActionEvent actionEvent) {

        // 获取消息体
        String text = inputText.getText();
        MessageWrap messageWrap = MessageWrap.builder()
                .type(ChatTypeEnum.PRIVATE_CHAT)
                .cmd(PrivateChatCmd.SIMPLE_PRIVATE_CHAT)
                .clientMsgId(IdUtil.objectId())
                .qos(2)
                .receive(userId)
                .content(text)
                .build();

        SocketHolder.getInstance().sendAndCallBack(messageWrap, msg -> CommonDialog.getInstance().showMsg(msg.getContent()));
    }

    @Override
    public void beforeInit(URL location, ResourceBundle resources) {

    }

    @Override
    public void init(URL location, ResourceBundle resources) {

        nicknameView.setText(username);
        chatListView.setCellFactory(callback -> new ChatListItemC());
        List<ChatMsg> chatMsgList = MockUtil.mock(new TypeReference<List<ChatMsg>>() {
        })
                .actual(new ArrayList<>())
                .mockK(MockKey.PRIVATE_CHAT.getKey() + userId)
                .get();
        chatListView.setItems(FXCollections.observableList(chatMsgList));

        // 增加监听器 监听消息回调

    }

    @Override
    public void afterInit(URL location, ResourceBundle resources) {

    }
}

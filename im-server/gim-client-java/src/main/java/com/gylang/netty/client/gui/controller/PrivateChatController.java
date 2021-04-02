package com.gylang.netty.client.gui.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.TypeReference;
import com.gylang.netty.client.coder.ChatTypeEnum;
import com.gylang.netty.client.constant.cmd.PrivateChatCmd;
import com.gylang.netty.client.domain.MessageWrap;
import com.gylang.netty.client.domain.chat.ChatMsg;
import com.gylang.netty.client.enums.MockKey;
import com.gylang.netty.client.gui.component.list.ChatListComponent;
import com.gylang.netty.client.util.MockUtil;
import com.gylang.netty.client.socket.SocketHolder;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class PrivateChatController extends Application implements Initializable {


    public ListView<ChatMsg> chatListView;

    public TextArea inputText;

    public TextField nickname;

    public Button send;

    private String userId;

    @Override
    public void start(Stage primaryStage) throws Exception {


        final Stage main = new Stage();
        URL resource = getClass().getResource("/fxml/PrivateChat.fxml");
        log.info("[start] : {}", resource);
        Parent root = FXMLLoader.load(resource);
        main.setTitle("单聊");
        main.setScene(new Scene(root));
        userId = (String) primaryStage.getUserData();
        main.show();
    }

    private void loadMsg() {


    }


    public void sendMsg(ActionEvent actionEvent) {

        // 获取消息体
        String text = inputText.getText();
        MessageWrap messageWrap = MessageWrap.builder()
                .type(ChatTypeEnum.PRIVATE_CHAT.getType())
                .cmd(PrivateChatCmd.PRIVATE_CHAT)
                .msgId(IdUtil.objectId())
                .qos(true)
                .receive(userId)
                .content(text)
                .build();

        SocketHolder.getInstance().send(messageWrap);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chatListView.setCellFactory(callback -> new ChatListComponent());
        List<ChatMsg> chatMsgList = MockUtil.mock(new TypeReference<List<ChatMsg>>() {})
                .actual(new ArrayList<>())
                .mockK(MockKey.PRIVATE_CHAT.getKey() + "111")
                .get();
        chatListView.setItems(FXCollections.observableList(chatMsgList));

        // 增加监听器 监听消息回调

    }
}

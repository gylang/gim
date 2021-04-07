package com.gylang.gim.web.client.gui.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.web.api.dto.ImUserFriendDTO;
import com.gylang.gim.web.api.enums.ChatTypeEnum;
import com.gylang.gim.web.api.constant.cmd.PrivateChatCmd;
import com.gylang.gim.web.api.domain.MessageWrap;
import com.gylang.gim.web.api.domain.chat.ChatMsg;
import com.gylang.gim.web.client.enums.MockKey;
import com.gylang.gim.web.client.gui.core.CustomApplication;
import com.gylang.gim.web.client.socket.SocketHolder;
import com.gylang.gim.web.client.util.MockUtil;
import com.gylang.gim.web.client.gui.component.list.ChatListComponent;
import com.gylang.gim.web.client.gui.dialog.CommonDialog;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
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

        this.userId = friend.getUid().toString();
        this.username = friend.getUsername();
        this.nickname = friend.getNickname();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final Stage main = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PrivateChat.fxml"));

        // Create a controller instance
//        PrivateChatController controller = new PrivateChatController(userId, nickname, username);
        // Set it in the FXMLLoader
        loader.setController(this);
        Parent load = loader.load();

        main.setTitle("好友");
        main.setScene(new Scene(load));
        main.show();


//        final Stage main = new Stage();
//        URL resource = getClass().getResource("/fxml/PrivateChat.fxml");
//        log.info("[start] : {}", resource);
//        Parent root = FXMLLoader.load(resource);
//        main.setTitle("聊天");
//        main.setScene(new Scene(root));
//        main.show();
    }

    private void loadMsg() {


    }


    public void sendMsg(ActionEvent actionEvent) {

        // 获取消息体
        String text = inputText.getText();
        MessageWrap messageWrap = MessageWrap.builder()
                .type(ChatTypeEnum.PRIVATE_CHAT.getType())
                .cmd(PrivateChatCmd.SIMPLE_PRIVATE_CHAT)
                .clientMsgId(IdUtil.objectId())
                .qos(true)
                .receive(userId)
                .content(text)
                .build();

        SocketHolder.getInstance().sendAndCallBack(messageWrap, msg -> CommonDialog.getInstance().showMsg(msg.getContent()));
    }

    @Override
    public void init(URL location, ResourceBundle resources) {


        chatListView.setCellFactory(callback -> new ChatListComponent());
        List<ChatMsg> chatMsgList = MockUtil.mock(new TypeReference<List<ChatMsg>>() {
        })
                .actual(new ArrayList<>())
                .mockK(MockKey.PRIVATE_CHAT.getKey() + userId)
                .get();
        chatListView.setItems(FXCollections.observableList(chatMsgList));

        // 增加监听器 监听消息回调

    }
}

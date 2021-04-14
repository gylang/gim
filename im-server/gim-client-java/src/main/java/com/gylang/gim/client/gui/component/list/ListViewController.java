package com.gylang.gim.client.gui.component.list;

import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.api.domain.chat.ChatMsg;
import com.gylang.gim.client.enums.MockKey;
import com.gylang.gim.client.gui.core.CustomApplication;
import com.gylang.gim.client.util.MockUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * author gylang
 * data 2021/4/14
 */
public class ListViewController extends CustomApplication {


    @FXML
    public ListView<ChatMsg> chatListView;
    @FXML
    public TextArea title;


    public ListViewController() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final Stage main = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/ListviewContainer.fxml"));

        loader.setController(this);
        Parent load = loader.load();

        main.setTitle("好友");
        main.setScene(new Scene(load));
        main.show();


    }

    private void loadMsg() {


    }




    @Override
    public void init(URL location, ResourceBundle resources) {


        chatListView.setCellFactory(callback -> new ChatListComponent());
        List<ChatMsg> chatMsgList = MockUtil.mock(new TypeReference<List<ChatMsg>>() {
        })
                .actual(new ArrayList<>())
                .mockK(MockKey.FRIEND_APPLY_NOTIFY.getKey())
                .get();

        chatListView.setItems(FXCollections.observableList(chatMsgList));

        // 增加监听器 监听消息回调

    }
}

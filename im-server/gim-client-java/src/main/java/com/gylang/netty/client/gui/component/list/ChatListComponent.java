package com.gylang.netty.client.gui.component.list;

import com.gylang.netty.client.GuiMainApplication;
import com.gylang.netty.client.domain.chat.ChatMsg;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @author gylang
 * data 2021/4/2
 */
@Slf4j
public class ChatListComponent extends ListCell<ChatMsg> implements Callback<Class<?>, Object>, Initializable {


    public ImageView avatarView;
    public Text nicknameText;
    public TextArea msgText;

    private static URL LEFT = null;
    private static URL RIGHT = null;

    static {
        LEFT = GuiMainApplication.class.getResource("/fxml/component/ChatMsgLeft.fxml");
        RIGHT = GuiMainApplication.class.getResource("/fxml/component/ChatMsgRight.fxml");
    }

    @Override
    protected void updateItem(ChatMsg item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            // 如果这是一个空行
            setText(null);
            setGraphic(null);
        } else if (getGraphic() == null) {
            try {
                if (item.isMe()) {
                    setGraphic(FXMLLoader.load(RIGHT, null, null, this, StandardCharsets.UTF_8));
                } else {
                    setGraphic(FXMLLoader.load(LEFT, null, null, this, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bindData(item);
        } else {
            // 已经创建过节点，更新数据显示
            bindData(item);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //


    }


    public void bindData(ChatMsg item) {
        nicknameText.setText(item.getNickname());
        avatarView.setImage(new Image(item.getAvatar()));
        msgText.setText(item.getContent());
    }

    @Override
    public Object call(Class<?> param) {
        return this;
    }
}

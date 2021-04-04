package com.gylang.netty.client.gui.component.list;

import com.gylang.im.api.constant.cmd.PrivateChatCmd;
import com.gylang.im.api.domain.MessageWrap;
import com.gylang.im.api.enums.ChatTypeEnum;
import com.gylang.netty.client.GuiClientApplication;
import com.gylang.im.api.domain.chat.ChatMsg;
import com.gylang.netty.client.call.GimCallBack;
import com.gylang.netty.client.gui.util.GuiUtil;
import com.gylang.netty.client.socket.SocketHolder;
import com.gylang.netty.client.socket.SocketManager;
import com.gylang.netty.client.util.Store.UserStore;
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
    private String uid = "111";
    private String nickname = "李元霸";
    private static URL LEFT = null;
    private static URL RIGHT = null;

    static {
        LEFT = GuiClientApplication.class.getResource("/fxml/component/ChatMsgLeft.fxml");
        RIGHT = GuiClientApplication.class.getResource("/fxml/component/ChatMsgRight.fxml");
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
        SocketHolder.getInstance()
                .bind(ChatTypeEnum.PRIVATE_CHAT.getType(), PrivateChatCmd.SIMPLE_PRIVATE_CHAT, msg -> {

                    UserStore userStore = UserStore.getInstance();
                    if (userStore.getUid().equals(msg.getSender())
                            && uid.equals(msg.getReceive())) {
                        // 这是自己发出去得消息
                        ChatMsg chatMsg = new ChatMsg();
                        chatMsg.setContent(msg.getContent());
                        chatMsg.setMsgId(msg.getMsgId());
                        chatMsg.setTimeStamp(msg.getTimeStamp());
                        chatMsg.setMe(true);
                        chatMsg.setNickname(userStore.getNickname());
                        GuiUtil.update(() -> updateItem(chatMsg, false));
                    }
                    if (uid.equals(msg.getSender())) {
                        ChatMsg chatMsg = new ChatMsg();
                        chatMsg.setContent(msg.getContent());
                        chatMsg.setMsgId(msg.getMsgId());
                        chatMsg.setTimeStamp(msg.getTimeStamp());
                        chatMsg.setMe(false);
                        chatMsg.setNickname(nickname);
                        GuiUtil.update(() -> updateItem(chatMsg, false));
                    }
                });

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

package com.gylang.gim;

import com.gylang.gim.api.constant.common.CommonConstant;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatType;
import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.remote.call.GimCallBack;

import java.util.Scanner;

/**
 * @author gylang
 * data 2021/5/17
 */
public class RoomClient {

    private static final String IP = "47.102.211.48";
//    private static final String IP = "127.0.0.1";

    private static final Integer PORT = 9998;


    public static void main(String[] args) {

        // 连接服务器
        System.out.println("输入你的用户名, 别用其他人的,现在还没同步回调校验: ");
        Scanner usernameSc = new Scanner(System.in);
        String username = usernameSc.nextLine();
        SocketManager socketManager = SocketHolder.getInstance();
        login.setContent(username);

        // 监听用户发送的消息
        GimCallBack<MessageWrap> msgCall = m -> {
            if (!m.getSender().equals(username)) {
                if (CommonConstant.SYSTEM_SENDER.equals(m.getSender())) {
                    System.out.println("系统 : " + m.getContent());
                } else {
                    System.out.println(m.getSender() + " : " + m.getContent());
                }
            }

        };

        socketManager.typeBind(ChatType.CHAT_ROOM, msgCall);

        socketManager.connect(IP, PORT, login, c -> {
            System.out.println("登录成功");
        });
        String sendMsg = null;
        Scanner msgSc = new Scanner(System.in);
        while (true) {
            sendMsg = msgSc.nextLine();
            MessageWrap messageWrap = send.copyBasic();
            messageWrap.setContent(sendMsg);
            socketManager.send(messageWrap);
        }
    }

    private static final MessageWrap login = new MessageWrap();

    private static final MessageWrap send = new MessageWrap();

    static {
        login.setType(ChatType.CLIENT_AUTH);
        login.setQos(1);

        send.setQos(2);
        send.setType(ChatType.CHAT_ROOM);
    }
}

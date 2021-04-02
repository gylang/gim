package com.gylang.netty.client;

import cn.hutool.core.bean.BeanUtil;
import com.gylang.netty.client.config.ClientConfig;
import com.gylang.netty.client.gui.controller.LoginController;
import com.gylang.netty.client.gui.controller.PrivateChatController;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author gylang
 * data 2021/4/2
 */
public class GuiClientApplication {


    public static void main(String[] args) {

        // 读取配置文件
        Properties properties = null;
        properties = new Properties();
        URL url = null;
        for (int i = 0; i < args.length; i++) {
            if ("-client.config.path".equals(args[i])) {
                try {
                    url = new URL(args[i + 1]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        url = GuiClientApplication.class.getResource("/client-config.properties");
        try (InputStream is = url.openStream()) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BeanUtil.copyProperties(properties, ClientConfig.getInstance());
        SocketClientApplication.main(args);

        LoginController.launch(LoginController.class, args);
//        LoginController.launch(PrivateChatController.class, args);
    }
}

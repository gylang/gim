package com.gylang.gim.client;

import cn.hutool.core.bean.BeanUtil;
import com.gylang.gim.client.config.ClientConfig;
import com.gylang.gim.client.gui.controller.LoginController;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.LogManager;

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
        // 环境变量
        SocketClientApplication.run();
        LoginController.launch(LoginController.class, args);
    }
}

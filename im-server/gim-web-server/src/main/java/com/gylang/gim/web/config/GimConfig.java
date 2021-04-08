package com.gylang.gim.web.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gylang.gim.api.constant.CommonConstant;
import com.gylang.gim.api.constant.QosConstant;
import com.gylang.gim.api.constant.cmd.AdminChatCmd;
import com.gylang.gim.api.domain.admin.AdminUser;
import com.gylang.gim.api.domain.common.MessageWrap;
import com.gylang.gim.api.enums.ChatTypeEnum;
import com.gylang.gim.remote.SocketHolder;
import com.gylang.gim.remote.SocketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gylang
 * data 2021/4/7
 */
@Configuration
@Slf4j
public class GimConfig {

    @Value("${gim.port}")
    private Integer port;
    @Value("${gim.ip}")
    private String ip;
    @Value("${gim.username}")
    private String username;
    @Value("${gim.password}")
    private String password;

    @Bean
    public SocketManager socketManager() {
        SocketManager socketManager = SocketHolder.getInstance();
        // 构建登录新
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        MessageWrap messageWrap = MessageWrap.builder()
                .cmd(AdminChatCmd.REMOTE_LOGIN)
                .type(ChatTypeEnum.ADMIN_CHAT.getType())
                .clientMsgId(IdWorker.getIdStr())
                .qos(QosConstant.ACCURACY_ONE_ARRIVE)
                .content(JSON.toJSONString(adminUser))
                .build();
        socketManager.connect(ip, port, messageWrap, s -> {

            if (CommonConstant.TRUE_INT_STR.equals(s)) {
                log.info("[gim 连接] : 连接成功");
            } else {
                log.info("[gim 连接] : (重)连接失败");
            }
        });
        return socketManager;
    }

}

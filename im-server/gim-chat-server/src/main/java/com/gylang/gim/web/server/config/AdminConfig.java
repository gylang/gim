package com.gylang.gim.web.server.config;

import com.gylang.gim.web.server.domain.AdminUser;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gylang
 * data 2021/4/7
 */
@Component
@ConfigurationProperties(value = "admin-user")
@Data
public class AdminConfig {


    Map<String, AdminUser> adminUser;
}

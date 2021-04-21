package com.gylang.gim.server.config;

import com.gylang.gim.api.domain.admin.AdminUser;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gylang
 * data 2021/4/7
 */
@Component
@ConfigurationProperties(prefix = "gim")
@Data
public class AdminConfig {


    Map<String, AdminUser> adminUser;
}

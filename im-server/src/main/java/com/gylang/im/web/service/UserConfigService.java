package com.gylang.im.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gylang.im.dao.entity.UserConfig;

/**
 * 用户设置表(UserConfig)表服务接口
 *
 * @author makejava
 * @since 2021-03-06 17:10:10
 */
public interface UserConfigService extends IService<UserConfig> {


    UserConfig findUserConfig(long targetId);
}
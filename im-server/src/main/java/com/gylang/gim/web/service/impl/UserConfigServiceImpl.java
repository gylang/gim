package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.gim.web.dao.entity.UserConfig;
import com.gylang.gim.web.dao.mapper.UserConfigMapper;
import com.gylang.gim.web.service.UserConfigService;
import org.springframework.stereotype.Service;


/**
 * 用户设置表(UserConfig)表服务实现类
 *
 * @author makejava
 * @since 2021-03-06 17:10:10
 */
@Service("userConfigService")
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements UserConfigService {


    @Override
    public UserConfig findUserConfig(String targetId) {

        // 当前环境通过数据库信息获取用户信息
        return getOne(new QueryWrapper<UserConfig>()
                .eq("uid", targetId));
    }
}
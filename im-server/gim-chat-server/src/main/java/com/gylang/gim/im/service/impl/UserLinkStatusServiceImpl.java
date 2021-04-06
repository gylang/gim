package com.gylang.gim.im.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.gim.im.service.UserLinkStatusService;
import com.gylang.im.api.constant.CacheConstant;
import com.gylang.im.api.constant.CommonConstant;
import com.gylang.im.api.domain.UserLinkStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2021/4/6
 */
@Service
public class UserLinkStatusServiceImpl implements UserLinkStatusService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void setStatus(String account, UserLinkStatus status) {

        // 连接断开
        if (StrUtil.isNotEmpty(account)) {
            //更新用户状态 先后顺序
            String userLinkStatusJson = redisTemplate.<String, String>opsForHash()
                    .get(CacheConstant.USER_LINK_STATUS, account);
            UserLinkStatus userStatus = JSON.parseObject(userLinkStatusJson, UserLinkStatus.class);
            if (null == userStatus) {
                userStatus = new UserLinkStatus();
            }
            userStatus.setStatus(CommonConstant.FALSE_INT_STR);
            // 更新状态
            redisTemplate.opsForHash()
                    .put(CacheConstant.USER_LINK_STATUS, account, userStatus);

        }
    }

    @Override
    public List<UserLinkStatus> statusList(List<String> account) {

        List<UserLinkStatus> result = new ArrayList<>(account.size());
        if (CollUtil.isNotEmpty(account)) {
            //更新用户状态 先后顺序
            List<String> userLinkStatusJsonList = redisTemplate.<String, String>opsForHash()
                    .multiGet(CacheConstant.USER_LINK_STATUS, account);

            if (CollUtil.isNotEmpty(userLinkStatusJsonList)) {
                for (String json : userLinkStatusJsonList) {
                    result.add(JSON.parseObject(json, UserLinkStatus.class));
                }
            }
        }
        return result;
    }
}

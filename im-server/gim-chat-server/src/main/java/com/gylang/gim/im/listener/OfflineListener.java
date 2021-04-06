package com.gylang.gim.im.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.gylang.im.api.constant.CacheConstant;
import com.gylang.im.api.constant.CommonConstant;
import com.gylang.im.api.domain.UserLinkStatus;
import com.gylang.netty.sdk.constant.EventTypeConst;
import com.gylang.netty.sdk.domain.model.IMSession;
import com.gylang.netty.sdk.event.message.MessageEvent;
import com.gylang.netty.sdk.event.message.MessageEventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/4/6
 */
@Component
public class OfflineListener implements MessageEventListener<IMSession> {



    @Override
    @MessageEvent(EventTypeConst.OVER_TIME_CLOSE)
    public void onEvent(String key, IMSession session) {



    }
}

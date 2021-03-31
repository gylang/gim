package com.gylang.im.common;

import com.gylang.cache.CacheManager;
import com.gylang.im.common.constant.CommonConstant;
import com.gylang.im.common.mybatis.UserHelper;
import com.gylang.im.web.dto.cache.UserCache;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author gylang
 * data 2021/3/6
 */
@Component
public class UserHelperImpl implements UserHelper {

    @Resource
    private CacheManager cacheManager;

    @Override
    public Long getUid() {
        return getUid(getToken());
    }

    @Override
    public Long getUid(String token) {

        UserCache userCache = getUserInfo(token);
        return null != userCache ? userCache.getId() : null;
    }

    @Override
    public UserCache getUserInfo() {
        return getUserInfo(getToken());
    }

    @Override
    public UserCache getUserInfo(String token) {
        UserCache userCache = null;
        if (null != token) {
            userCache = cacheManager.get(token);
        }
        return userCache;
    }

    private String getToken() {
        String token = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            token = request.getHeader(CommonConstant.TOKEN_HEADER);
        }
        return token;
    }
}

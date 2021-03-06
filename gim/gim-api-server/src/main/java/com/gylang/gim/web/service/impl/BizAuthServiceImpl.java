package com.gylang.gim.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gylang.cache.CacheManager;
import com.gylang.gim.api.constant.CacheConstant;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.entity.UserCache;
import com.gylang.gim.api.dto.request.LoginRequest;
import com.gylang.gim.api.dto.request.RegistryRequest;
import com.gylang.gim.api.dto.response.LoginResponse;
import com.gylang.gim.remote.SocketManager;
import com.gylang.gim.web.common.constant.CommonConstant;
import com.gylang.gim.web.common.util.Asserts;
import com.gylang.gim.web.common.util.MappingUtil;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.entity.PtUserInfo;
import com.gylang.gim.web.service.PtUserInfoService;
import com.gylang.gim.web.service.PtUserService;
import com.gylang.gim.web.service.biz.BizAuthService;
import com.gylang.gim.web.service.im.ImUserManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author gylang
 * data 2021/3/6
 */
@Service
public class BizAuthServiceImpl implements BizAuthService {

    @Resource
    private PtUserService ptUserService;
    @Resource
    private PtUserInfoService userInfoService;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private SocketManager socketManager;
    @Resource
    private ImUserManager imUserManager;

    @Override
    public CommonResult<LoginResponse> login(@RequestBody LoginRequest request) {

        // 获取账户密码
        Asserts.notEmpty(request.getUsername(), "用户名不能为空");
        Asserts.notEmpty(request.getPassword(), "密码不能为空");

        // 查询用户是否存在
        QueryWrapper<PtUser> query = new QueryWrapper<PtUser>()
                .eq("username", request.getUsername());
        PtUser ptUser = ptUserService.getOne(query);
        // 判断是否存在 状态是否正常
        Asserts.notNull(ptUser, "用户或密码错误");
        Asserts.isTrue(CommonConstant.NORMAL_INT == ptUser.getStatus(), "账户被冻结");

        // 判断密码是否正确
        Asserts.isTrue(DigestUtil.md5Hex(request.getPassword() + ptUser.getSalt(), StandardCharsets.UTF_8)
                .equals(ptUser.getPassword()), "用户或密码错误");

        // 正常登录
        // 获取用户扩展信息
        PtUserInfo userInfo = userInfoService.getOne(new QueryWrapper<PtUserInfo>().eq("uid", ptUser.getId()));
        LoginResponse loginResponse = MappingUtil.map(ptUser, LoginResponse.class);
        MappingUtil.map(userInfo, loginResponse);
        UserCache userCache = MappingUtil.map(userInfo, UserCache.class);
        MappingUtil.map(ptUser, userCache);
        String token = CacheConstant.AUTH_TOKEN_PREFIX + IdWorker.getId();

        String socketToken = CacheConstant.IM_AUTH_TOKEN_PREFIX + IdWorker.getId();
        // 单机版
        loginResponse.setImToken(socketToken);
        loginResponse.setImIp("127.0.0.1");
        loginResponse.setImPort(46001);

        cacheManager.set(token, userCache, 6 * 60 * 60L);
        userCache.setToken(token);
        imUserManager.imUserAuth(loginResponse);
        return CommonResult.ok(loginResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Boolean> registry(RegistryRequest request) {

        // 判断是否存在 状态是否正常
        Asserts.notNull(request.getUsername(), "用户名不能为空");
        Asserts.notNull(request.getPassword(), "密码不能为空");

        // 用户注册
        QueryWrapper<PtUser> query = new QueryWrapper<PtUser>()
                .eq("username", request.getUsername());
        PtUser ptUser = ptUserService.getOne(query);
        Asserts.isNull(ptUser, "用户已被注册");

        // 注册用户
        String salt = IdWorker.getId() + request.getUsername();
        String saltDecode = DigestUtil.md5Hex(salt);

        // 保存用户信息
        ptUser = new PtUser();
        ptUser.setUsername(request.getUsername());
        String password = DigestUtil.md5Hex(request.getPassword() + saltDecode, StandardCharsets.UTF_8);
        ptUser.setPassword(password);
        ptUser.setNickname(ObjectUtil.defaultIfBlank(request.getNickname(), request.getUsername()));
        ptUser.setSalt(saltDecode);
        boolean save = ptUserService.save(ptUser);
        PtUserInfo ptUserInfo = MappingUtil.map(ptUser, PtUserInfo.class);
        ptUserInfo.setUid(ptUser.getId().toString());
        ptUserInfo.setId(null);
        save = save && userInfoService.save(ptUserInfo);
        Asserts.isTrue(save, "注册失败");
        // 推送用户注册 im服务生成socket用户信息
        UserCache userCache = MappingUtil.map(request, new UserCache());
        imUserManager.addUser(userCache);
        return CommonResult.ok();
    }


}

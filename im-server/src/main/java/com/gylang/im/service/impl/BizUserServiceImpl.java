package com.gylang.im.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.domain.common.PageResponse;
import com.gylang.im.common.mybatis.PageDTO;
import com.gylang.im.common.util.MappingUtil;
import com.gylang.im.dao.entity.PtUser;
import com.gylang.im.service.BizUserService;
import com.gylang.im.service.PtUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/7
 */
@Service
public class BizUserServiceImpl implements BizUserService {

    @Resource
    private PtUserService ptUserService;

    @Override
    public CommonResult<PageResponse> search(PageDTO<PtUser> user) {

        PtUser param = ObjectUtil.defaultIfNull(user.getParam(), new PtUser());

        PageDTO<PtUser> userPageDTO = ptUserService.page(user, new QueryWrapper<PtUser>()
                .or().likeRight(StrUtil.isNotEmpty(param.getUsername()), "username", param.getUsername())
                .or().likeRight(StrUtil.isNotEmpty(param.getNickname()), "nickname", param.getNickname())
                .or().likeRight(ObjectUtil.isNotNull(param.getId()), "id", param.getId())

        );
        return CommonResult.ok(MappingUtil.map(userPageDTO, new PageResponse()));
    }
}

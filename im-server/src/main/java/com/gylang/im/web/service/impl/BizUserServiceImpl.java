package com.gylang.im.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.common.dto.PageDTO;
import com.gylang.im.dao.entity.PtUser;
import com.gylang.im.web.dto.PtUserDTO;
import com.gylang.im.web.service.BizUserService;
import com.gylang.im.web.service.PtUserService;
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
    public CommonResult<PageDTO<PtUser>> search(PageDTO<PtUser> user) {

        PtUser param = ObjectUtil.defaultIfNull(user.getParam(), new PtUser());

        PageDTO<PtUser> userPageDTO = ptUserService.page(user, new QueryWrapper<PtUser>()
                .or().likeRight(StrUtil.isNotEmpty(param.getUsername()), "username", param.getUsername())
                .or().likeRight(StrUtil.isNotEmpty(param.getNickname()), "nickname", param.getNickname())
                .or().likeRight(ObjectUtil.isNotNull(param.getId()), "id", param.getId())

        );
        return CommonResult.ok(userPageDTO);
    }
}

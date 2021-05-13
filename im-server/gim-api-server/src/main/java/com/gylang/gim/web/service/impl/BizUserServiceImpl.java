package com.gylang.gim.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.service.BizUserService;
import com.gylang.gim.web.service.PtUserService;
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
    public CommonResult<PageResponse<PtUserDTO>> search(Page<PtUser> user) {

        PtUser param = ObjectUtil.defaultIfNull(user.getParam(), new PtUser());

        Page<PtUser> userPageDTO = ptUserService.page(user, new QueryWrapper<PtUser>()
                .or().likeRight(StrUtil.isNotEmpty(param.getUsername()), "username", param.getUsername())
                .or().likeRight(StrUtil.isNotEmpty(param.getNickname()), "nickname", param.getNickname())
                .or().likeRight(ObjectUtil.isNotNull(param.getId()), "id", param.getId())

        );
        return CommonResult.ok(userPageDTO.toDTO(PtUserDTO.class));
    }
}

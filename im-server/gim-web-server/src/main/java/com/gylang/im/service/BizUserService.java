package com.gylang.im.service;

import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.domain.common.PageResponse;
import com.gylang.im.common.mybatis.Page;
import com.gylang.im.entity.PtUser;

/**
 * @author gylang
 * data 2021/3/7
 */
public interface BizUserService {

    CommonResult<PageResponse<PtUser>> search(Page<PtUser> user);
}

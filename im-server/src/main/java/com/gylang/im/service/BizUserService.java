package com.gylang.im.service;

import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.domain.common.PageResponse;
import com.gylang.im.common.mybatis.PageDTO;
import com.gylang.im.dao.entity.PtUser;

/**
 * @author gylang
 * data 2021/3/7
 */
public interface BizUserService {

    CommonResult<PageResponse> search(PageDTO<PtUser> user);
}

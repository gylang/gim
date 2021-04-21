package com.gylang.gim.web.service;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.entity.PtUser;

/**
 * @author gylang
 * data 2021/3/7
 */
public interface BizUserService {

    CommonResult<PageResponse<PtUserDTO>> search(Page<PtUser> user);
}

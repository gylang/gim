package com.gylang.im.web.service;

import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.common.dto.PageDTO;
import com.gylang.im.web.dao.entity.PtUser;

/**
 * @author gylang
 * data 2021/3/7
 */
public interface BizUserService {

    CommonResult<PageDTO<PtUser>> search(PageDTO<PtUser> user);
}

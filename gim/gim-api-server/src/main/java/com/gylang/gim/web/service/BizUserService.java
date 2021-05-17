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

    /**
     * 查询用户信息
     *
     * @param user
     * @return
     */
    CommonResult<PageResponse<PtUserDTO>> search(Page<PtUser> user);

    /**
     * 删除用户
     *
     * @param user
     * @return
     */
    Boolean del(PtUserDTO user);

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    Boolean save(PtUserDTO user);
}

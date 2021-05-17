package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.web.common.mybatis.Page;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.service.BizUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author gylang
 * data 2021/3/7
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private BizUserService bizUserService;

    /**
     * 用户搜索
     *
     * @param user
     * @return
     */
    @RequestMapping("search")
    public CommonResult<PageResponse<PtUserDTO>> search(@RequestBody Page<PtUser> user) {

        return bizUserService.search(user);

    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @RequestMapping("save")
    public CommonResult<Boolean> add(@RequestBody PtUserDTO user) {

        return CommonResult.auto(bizUserService.save(user));

    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @RequestMapping("del")
    public CommonResult<Boolean> del(@RequestBody PtUserDTO user) {

        return CommonResult.auto(bizUserService.del(user));

    }


}

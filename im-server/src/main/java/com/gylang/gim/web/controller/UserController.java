package com.gylang.gim.web.controller;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.web.common.mybatis.PageDTO;
import com.gylang.gim.web.dao.entity.PtUser;
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

    @RequestMapping("search")
    public CommonResult<PageResponse> search(@RequestBody PageDTO<PtUser> user) {

        return bizUserService.search(user);

    }
}

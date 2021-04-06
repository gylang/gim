package com.gylang.im.controller;

import com.gylang.im.api.domain.common.CommonResult;
import com.gylang.im.api.domain.common.PageResponse;
import com.gylang.im.common.mybatis.Page;
import com.gylang.im.entity.PtUser;
import com.gylang.im.service.BizUserService;
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
    public CommonResult<PageResponse<PtUser>> search(@RequestBody Page<PtUser> user) {

        return bizUserService.search(user);

    }
}

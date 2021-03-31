package com.gylang.im.web.controller;

import com.gylang.im.common.dto.CommonResult;
import com.gylang.im.common.dto.PageDTO;
import com.gylang.im.web.dao.entity.PtUser;
import com.gylang.im.web.service.BizUserService;
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
    public CommonResult<PageDTO<PtUser>> search(@RequestBody PageDTO<PtUser> user) {

        return bizUserService.search(user);

    }
}

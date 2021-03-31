package com.gylang.im.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.web.dao.entity.SysUser;
import com.gylang.im.web.dao.mapper.SysUserMapper;
import com.gylang.im.web.service.SysUserService;
import org.springframework.stereotype.Service;


/**
 * (SysUser)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

   
}
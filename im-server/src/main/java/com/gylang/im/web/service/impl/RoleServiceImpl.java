package com.gylang.im.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.dao.entity.Role;
import com.gylang.im.dao.mapper.RoleMapper;
import com.gylang.im.web.service.RoleService;
import org.springframework.stereotype.Service;


/**
 * (Role)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

   
}
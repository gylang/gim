package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.gim.web.dao.entity.MerUser;
import com.gylang.gim.web.dao.mapper.MerUserMapper;
import com.gylang.gim.web.service.MerUserService;
import org.springframework.stereotype.Service;


/**
 * (MerUser)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("merUserService")
public class MerUserServiceImpl extends ServiceImpl<MerUserMapper, MerUser> implements MerUserService {

   
}
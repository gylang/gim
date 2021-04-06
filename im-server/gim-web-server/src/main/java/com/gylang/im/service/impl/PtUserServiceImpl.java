package com.gylang.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.entity.PtUser;
import com.gylang.im.mapper.PtUserMapper;
import com.gylang.im.service.PtUserService;
import org.springframework.stereotype.Service;


/**
 * (PtUser)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("ptUserService")
public class PtUserServiceImpl extends ServiceImpl<PtUserMapper, PtUser> implements PtUserService {

   
}
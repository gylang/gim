package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.mapper.PtUserMapper;
import com.gylang.gim.web.service.PtUserService;
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
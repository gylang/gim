package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.gim.web.dao.entity.UserApply;
import com.gylang.gim.web.dao.mapper.UserApplyMapper;
import com.gylang.gim.web.service.UserApplyService;
import org.springframework.stereotype.Service;


/**
 * 好友申请表(UserApply)表服务实现类
 *
 * @author makejava
 * @since 2021-03-06 20:46:20
 */
@Service("userApplyService")
public class UserApplyServiceImpl extends ServiceImpl<UserApplyMapper, UserApply> implements UserApplyService {

   
}
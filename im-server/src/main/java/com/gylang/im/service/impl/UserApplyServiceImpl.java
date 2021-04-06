package com.gylang.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.dao.entity.UserApply;
import com.gylang.im.dao.mapper.UserApplyMapper;
import com.gylang.im.service.UserApplyService;
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
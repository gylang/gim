package com.gylang.im.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.entity.PtUserInfo;
import com.gylang.im.mapper.PtUserInfoMapper;
import com.gylang.im.service.PtUserInfoService;
import org.springframework.stereotype.Service;


/**
 * 用户信息表(PtUserInfo)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("ptUserInfoService")
public class PtUserInfoServiceImpl extends ServiceImpl<PtUserInfoMapper, PtUserInfo> implements PtUserInfoService {

   
}
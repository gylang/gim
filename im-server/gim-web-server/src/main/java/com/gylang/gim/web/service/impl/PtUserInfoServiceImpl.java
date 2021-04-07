package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.gim.web.entity.PtUserInfo;
import com.gylang.gim.web.mapper.PtUserInfoMapper;
import com.gylang.gim.web.service.PtUserInfoService;
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
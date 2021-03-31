package com.gylang.im.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.im.web.dao.entity.PtUserInfo;
import com.gylang.im.web.dao.mapper.PtUserInfoMapper;
import com.gylang.im.web.service.PtUserInfoService;
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
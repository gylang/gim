package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.cache.CacheManager;
import com.gylang.gim.web.common.constant.CacheConstant;
import com.gylang.gim.web.dao.entity.ImUserGroup;
import com.gylang.gim.web.dao.mapper.ImUserGroupMapper;
import com.gylang.gim.web.service.ImUserGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 群聊好友人员关系(ImUserGroup)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("imUserGroupService")
public class ImUserGroupServiceImpl extends ServiceImpl<ImUserGroupMapper, ImUserGroup> implements ImUserGroupService {
    @Resource
    private CacheManager cacheManager;

    @Override
    public boolean checkIsYouGroup(String account, String targetId) {
        return cacheManager.mapContainKey(CacheConstant.GROUP_LIST_PREFIX + account, String.valueOf(targetId));

    }
}
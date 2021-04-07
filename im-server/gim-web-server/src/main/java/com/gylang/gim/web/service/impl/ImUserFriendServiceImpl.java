package com.gylang.gim.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gylang.cache.CacheManager;
import com.gylang.gim.web.common.constant.CacheConstant;
import com.gylang.gim.api.dto.UserFriendVO;
import com.gylang.gim.web.entity.ImUserFriend;
import com.gylang.gim.web.mapper.ImUserFriendMapper;
import com.gylang.gim.web.service.ImUserFriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 好友关系表(ImUserFriend)表服务实现类
 *
 * @author makejava
 * @since 2021-03-03 21:58:55
 */
@Service("imUserFriendService")
public class ImUserFriendServiceImpl extends ServiceImpl<ImUserFriendMapper, ImUserFriend> implements ImUserFriendService {

    @Resource
    private CacheManager cacheManager;

    @Resource
    private ImUserFriendMapper userFriendMapper;

    @Override
    public boolean checkIsYouFriend(String account, String targetId) {

        return cacheManager.mapContainKey(CacheConstant.USER_FRIEND_LIST_PREFIX + account, String.valueOf(targetId));

    }

    @Override
    public List<UserFriendVO> selectFriendListByUid(Long id) {

        return userFriendMapper.selectFriendListByUid(id);
    }
}
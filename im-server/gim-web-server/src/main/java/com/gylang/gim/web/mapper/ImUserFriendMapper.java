package com.gylang.gim.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gylang.gim.api.dto.UserFriendVO;
import com.gylang.gim.web.entity.ImUserFriend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 好友关系表(ImUserFriend)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-06 11:06:40
 */
public interface ImUserFriendMapper extends BaseMapper<ImUserFriend> {


    /**
     * 查询用户好友
     */
    List<UserFriendVO> selectFriendListByUid(@Param("uid") String id);
}
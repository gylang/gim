package com.gylang.gim.web.service.impl;

import com.gylang.gim.api.domain.manager.WhiteBlackList;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.api.dto.response.WBListUserInfoDTO;
import com.gylang.gim.web.common.util.MappingUtil;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.service.PtUserService;
import com.gylang.gim.web.service.UserWbListService;
import com.gylang.gim.web.service.im.ImUserWhiteBlackManager;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gylang
 * data 2021/5/16
 */
public class UserWbListServiceImpl implements UserWbListService {

    @Resource
    private ImUserWhiteBlackManager imUserWhiteBlackManager;
    @Resource
    private PtUserService userService;

    @Override
    public WhiteBlackList whiteBlackList(WhiteBlackList whiteBlackList) {
        return imUserWhiteBlackManager.query(whiteBlackList);
    }

    @Override
    public Boolean update(WhiteBlackList whiteBlackList) {
        imUserWhiteBlackManager.save(whiteBlackList);
        return true;
    }

    @Override
    public WBListUserInfoDTO membersInfo(WhiteBlackList group) {
        WhiteBlackList whiteBlackList = whiteBlackList(group);
        if (null != whiteBlackList) {
            WBListUserInfoDTO wbListUserInfoDTO = MappingUtil.map(whiteBlackList, new WBListUserInfoDTO());
            List<String> addBlack = whiteBlackList.getAddBlack();
            List<PtUser> blackList = userService.listByIds(addBlack);
            wbListUserInfoDTO.setBlack(MappingUtil.mapAsList(blackList, PtUserDTO.class));

            List<String> addWhite = whiteBlackList.getAddWhite();
            List<PtUser> whiteList = userService.listByIds(addWhite);
            wbListUserInfoDTO.setWhite(MappingUtil.mapAsList(whiteList, PtUserDTO.class));
            return wbListUserInfoDTO;
        }
        return null;
    }
}

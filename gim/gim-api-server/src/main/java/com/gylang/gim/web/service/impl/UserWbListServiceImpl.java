package com.gylang.gim.web.service.impl;

import com.gylang.gim.api.domain.manager.BlackWhiteList;
import com.gylang.gim.api.dto.PtUserDTO;
import com.gylang.gim.api.dto.response.WBListUserInfoDTO;
import com.gylang.gim.web.common.util.MappingUtil;
import com.gylang.gim.web.entity.PtUser;
import com.gylang.gim.web.service.PtUserService;
import com.gylang.gim.web.service.UserWbListService;
import com.gylang.gim.web.service.im.ImUserBlackWhiteManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gylang
 * data 2021/5/16
 */
@Service
public class UserWbListServiceImpl implements UserWbListService {

    @Resource
    private ImUserBlackWhiteManager imUserWhiteBlackManager;
    @Resource
    private PtUserService userService;

    @Override
    public BlackWhiteList whiteBlackList(BlackWhiteList blackWhiteList) {
        return imUserWhiteBlackManager.query(blackWhiteList);
    }

    @Override
    public Boolean update(BlackWhiteList blackWhiteList) {
        imUserWhiteBlackManager.save(blackWhiteList);
        return true;
    }

    @Override
    public WBListUserInfoDTO membersInfo(BlackWhiteList group) {
        BlackWhiteList blackWhiteList = whiteBlackList(group);
        if (null != blackWhiteList) {
            WBListUserInfoDTO wbListUserInfoDTO = MappingUtil.map(blackWhiteList, new WBListUserInfoDTO());
            List<String> addBlack = blackWhiteList.getAddBlack();
            List<PtUser> blackList = userService.listByIds(addBlack);
            wbListUserInfoDTO.setBlack(MappingUtil.mapAsList(blackList, PtUserDTO.class));

            List<String> addWhite = blackWhiteList.getAddWhite();
            List<PtUser> whiteList = userService.listByIds(addWhite);
            wbListUserInfoDTO.setWhite(MappingUtil.mapAsList(whiteList, PtUserDTO.class));
            return wbListUserInfoDTO;
        }
        return null;
    }
}

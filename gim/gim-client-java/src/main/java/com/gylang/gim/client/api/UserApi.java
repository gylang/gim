package com.gylang.gim.client.api;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.api.domain.common.PageRequest;
import com.gylang.gim.api.domain.common.PageResponse;
import com.gylang.gim.api.dto.PtUserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author gylang
 * data 2021/4/21
 */
public interface UserApi {


    /**
     * 好友搜索
     * @param request
     * @return
     */
    @POST("/api/user/search")
    public Call<CommonResult<PageResponse<PtUserDTO>>> userSearch(@Body PageRequest<PtUserDTO> request);
}

package com.gylang.gim.client.call;

import com.gylang.gim.api.domain.common.CommonResult;
import com.gylang.gim.client.enums.BaseResultCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author gylang
 * data 2021/3/31
 */
public abstract class ICallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T body = response.body();
        if (body instanceof CommonResult) {
            if (BaseResultCode.OK.getCode().equals(((CommonResult<?>) body).getCode())) {
            success(call, response);
            } else {
                fail(call, (CommonResult<?>) body);
            }
        } else {
            fail(call, CommonResult.fail(BaseResultCode.SYSTEM_ERROR.getCode(), "响应类型无法解析"));
        }

    }

    public abstract void success(Call<T> call, Response<T> response);

    public abstract void fail(Call<T> call, CommonResult<?> response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        fail(call, CommonResult.fail(BaseResultCode.NETWORK_RESOURCE_SERVICE_ERROR.getCode(), t.getMessage()));
    }
}

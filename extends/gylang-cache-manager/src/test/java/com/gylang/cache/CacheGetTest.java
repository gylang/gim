package com.gylang.cache;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gylang
 * data 2021/2/2
 */
public class CacheGetTest {

    @Resource
    private CacheManager cacheManager;

    public void test1() {
        List<ResCO> redisConfigs = new ArrayList<>();
        ResCO resCO = new ResCO();
        resCO.setName("ggg");
        resCO.setPs("ss");
        redisConfigs.add(resCO);
        cacheManager.set("testMap", redisConfigs);
        // list 相关cache 建议通过其他方式出来
        List<ResCO> resCO1 = cacheManager.get("testMap");

        resCO1.get(0).setName("ssss");

    }

}

package com.gylang.gim.client.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gylang.gim.client.config.ClientConfig;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author gylang
 * data 2021/4/2
 */
public class MockUtil {

    private static final Map<String, String> mockData = new HashMap<>();

    static {

        // 初始化
        String mockPath = ClientConfig.getInstance()
                .getMockPath();

        File file = FileUtil.file(mockPath);

        if (null != file) {

            File[] files = file.listFiles();

            for (File f : files) {
                // 读取 json
                String key = StrUtil.subBefore(f.getName(), ".", true);
                String json = IoUtil.read(FileUtil.getReader(f, StandardCharsets.UTF_8));
                mockData.put(key, json);
            }
        }
    }

    public static <T> Mock<T> mock(Class<T> clazz) {

        return new Mock<>(clazz);
    }

    public static <T> Mock<T> mock(TypeReference<T> clazz) {

        return new Mock<>(clazz);
    }


    public static class Mock<T> {

        private T successRet;

        private Class<T> clazz;

        private T mockRet;

        private String mockKey;

        private TypeReference<T> type;

        public Mock(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Mock(TypeReference<T> type) {
            this.type = type;
        }

        public Mock<T> actual(T t) {
            successRet = t;
            return this;
        }

        public Mock<T> actual(Supplier<T> t) {
            successRet = t.get();
            return this;
        }

        public Mock<T> mockK(String key) {

            mockKey = key;
            return this;
        }

        public Mock<T> mockT(T t) {

            mockRet = t;
            return this;
        }

        public T get() {
            boolean dev = "dev".equals(ClientConfig.getInstance()
                    .getEnv());
            if (dev) {

                if (null != mockRet) {
                    return mockRet;
                }
                Assert.notNull(mockKey, "mock结果不存在");
                String result = mockData.get(mockKey);
                Assert.notNull(result, "mock结果不存在");
                if (null != type) {
                    return JSON.parseObject(result, type);
                }
                if (null != clazz) {
                    return JSON.parseObject(result, clazz);
                }


            } else {
                return successRet;
            }
            return null;
        }
    }
}

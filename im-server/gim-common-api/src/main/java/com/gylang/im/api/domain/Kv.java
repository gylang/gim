package com.gylang.im.api.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gylang
 * data 2021/4/3
 */
public class Kv {

    private Map<String, Object> kvMap = new HashMap<>();


    public static Kv create() {
        return new Kv();
    }
    public Kv put(String k, Object v) {
        kvMap.put(k, v);
        return this;
    }

    public Map<String, Object> toMap() {
        return kvMap;
    }
}

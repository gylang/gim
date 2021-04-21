package com.gylang.gim.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author gylang
 * data 2021/3/31
 */
public interface Api {


    @Getter
    @AllArgsConstructor
    public enum Type {


        ;
        private final String uri;
        private final Method method;
        private final ContentType contentType;
    }

    enum Method {
        POST,
        GET;
    }

    enum ContentType {
        JSON,
        FORM;
    }

}

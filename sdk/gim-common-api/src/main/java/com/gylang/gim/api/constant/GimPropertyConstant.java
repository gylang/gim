package com.gylang.gim.api.constant;

/**
 * @author gylang
 * data 2021/5/16
 */
public interface GimPropertyConstant {


    /** 运行模式 */
    String RUN_MODEL_KEY = "gim.mode";

    enum RUN_MODEL {
        ;
        /** 单机 */
        public static final String STANDALONE = "standalone";

        /** 标准模式 */
        public static final  String STANDARD = "standard";
    }

}

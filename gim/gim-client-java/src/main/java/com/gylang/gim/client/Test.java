package com.gylang.gim.client;

/**
 * @author gylang
 * data 2021/5/14
 */
public class Test extends ClassLoader {

    static {
        System.out.println("我不可能被执行");
    }
}

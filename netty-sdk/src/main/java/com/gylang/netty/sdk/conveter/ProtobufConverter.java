//package com.gylang.netty.sdk.conveter;
//
//
//import cn.hutool.core.util.ClassUtil;
//import com.google.protobuf.ByteString;
//import com.gylang.netty.sdk.domain.MessageWrap;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * protobuf 数据类型转化工具
// *
// * @author gylang
// * data 2020/11/3
// * @version v0.0.1
// */
//public class ProtobufConverter implements DataConverter {
//
//    private static final Map<Class<?>, Method> parseMethod = new ConcurrentHashMap<>();
//    private static final Map<Class<?>, Method> encodeMethod = new ConcurrentHashMap<>();
//
//    private static final String PARSE_METHOD_NAME = "parseFrom";
//    private static final String ENCODE_METHOD_NAME = "toByteArray";
//
//
//
//    @Override
//    public Object converterTo(Class<?> clazz, MessageWrap messageWrap) {
//        // 缓存类型转化的方法
//        if (clazz.equals(String.class)) {
//            return  messageWrap.getContent();
//        }
//        Method method = parseMethod.get(clazz);
//        if (null == method) {
//            Method declaredMethod = ClassUtil.getDeclaredMethod(clazz, PARSE_METHOD_NAME, ByteString.class);
//            declaredMethod.setAccessible(declaredMethod.isAccessible());
//            parseMethod.put(clazz, declaredMethod);
//            method = declaredMethod;
//        }
//        try {
//            return  method.invoke(null, messageWrap.getCode());
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public <T, S> S encode(T object) {
//        Class<?> clazz = object.getClass();
//        Method method = encodeMethod.get(clazz);
//        if (null == method) {
//            Method declaredMethod = ClassUtil.getDeclaredMethod(clazz, ENCODE_METHOD_NAME, ByteString.class);
//            declaredMethod.setAccessible(declaredMethod.isAccessible());
//            encodeMethod.put(clazz, declaredMethod);
//            method = declaredMethod;
//        }
//        try {
//            return (S) method.invoke(object, object);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}

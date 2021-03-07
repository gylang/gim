package com.gylang.cache;

import com.gylang.cache.redis.RedisCacheManager;
import com.gylang.cache.redis.RedisConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gylang
 * data 2021/1/5
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RedisConfig.class, RedisCacheManager.class})
public @interface RedisCacheSupport {

}

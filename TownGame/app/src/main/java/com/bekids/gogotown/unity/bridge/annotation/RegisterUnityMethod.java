package com.bekids.gogotown.unity.bridge.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: LuckyFind
 * Date: 2021/1/29
 * Desc: 处理unity传递过来得消息处理类得注解，只有标明了该注解的类，才会被认为是一个
 * 处理unity message的策略类，才可以被桥接类调用
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterUnityMethod {
    String [] value() default {};
}

package com.bekids.gogotown.unity.bridge.interf;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:
 */
public interface INativeCallUnity {
    /**
     * android端调用unity的方法，给unity传参
     * @param method 调用unity的方法名
     * @param params 传递给unity的参数
     * @param blockId unity传过来的id
     * @param originMethod 处理unity消息的方法名
     */
    void callUnity(String method,String params,String blockId,String originMethod);
}

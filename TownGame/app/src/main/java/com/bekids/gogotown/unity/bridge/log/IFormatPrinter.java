package com.bekids.gogotown.unity.bridge.log;


import com.bekids.gogotown.unity.bridge.dispatcher.UnityMessage;

/**
 * Author: LuckyFind
 * Date: 2021/3/30
 * Desc: 打印和Unity交互的日志
 */
public interface IFormatPrinter {

    /**
     * 打印Unity传递过来的原始请求信息
     * @param methodName
     * @param params
     * @param blockID
     */
    void printUnityRawRequestInfo(String methodName,String params,String blockID);

    /**
     * 打印分发器分发的请求信息
     * @param unityMessage
     */
    void printDispatchUnityMessageInfo(UnityMessage unityMessage, String targetStrategy);

    /**打印分发器无法分发的消息，这种情况一般是找不到对应的策略类
     * @param unityMessage
     * @param err
     */
    void printDispatchUnityMessageError(UnityMessage unityMessage,String err);

    /**
     *打印处理unity请求信息
     * @param strategyName
     * @param methodName
     * @param params
     * @param blockID
     */
    void printProcessUnityMessageInfo(String strategyName,String methodName,String params,String blockID,String returnValue);

    /**
     *打印处理unity请求信息
     * @param strategyName
     * @param methodName
     * @param params
     * @param blockID
     * @param err
     */
    void printProcessUnityMessageError(String strategyName,String methodName,String params,String blockID,String err);
    /**
     * 打印回调unity的信息
     * @param method
     * @param params
     * @param blockId
     * @param originMethod
     */
    void printUnityMessageCallbackInfo(String strategyName,String method, String params, String blockId,String originMethod);
}

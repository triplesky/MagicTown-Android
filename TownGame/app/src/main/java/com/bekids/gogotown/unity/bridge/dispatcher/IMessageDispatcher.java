package com.bekids.gogotown.unity.bridge.dispatcher;

/**
 * Author: LuckyFind
 * Date: 2021/1/27
 * Desc: UnityMessage的消息分发器
 */
public interface IMessageDispatcher {
    String dispatchMessage(String message, String params, String blockID);
}

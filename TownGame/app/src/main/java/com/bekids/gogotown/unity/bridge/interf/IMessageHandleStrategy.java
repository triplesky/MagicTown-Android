package com.bekids.gogotown.unity.bridge.interf;

import com.bekids.gogotown.unity.bridge.dispatcher.UnityMessage;

/**
 * Author: LuckyFind
 * Date: 2021/1/27
 * Desc:
 */
public interface IMessageHandleStrategy {

    void bindIUnityView(IUnityView iUnityView);

    String handleUnityMessage(UnityMessage unityMessage);
}

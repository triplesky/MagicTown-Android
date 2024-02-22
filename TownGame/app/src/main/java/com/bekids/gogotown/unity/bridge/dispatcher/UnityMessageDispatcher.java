package com.bekids.gogotown.unity.bridge.dispatcher;

import com.bekids.gogotown.unity.bridge.interf.IUnityView;

/**
 * Author: LuckyFind
 * Date: 2021/1/26
 * Desc: unity消息的分发者
 */
public class UnityMessageDispatcher extends BaseMessageDispatcher {
    MessageHandleContext messageHandleContext;

    public UnityMessageDispatcher(IUnityView iUnityView) {
        this.messageHandleContext = new MessageHandleContext(iUnityView);

    }

    @Override
    public String dispatchMessage(String message, String params, String blockID) {
        UnityMessage unityMessage = new UnityMessage(message, params, blockID);
        return messageHandleContext.dispatchMessage(unityMessage);
    }

    public void stopDispatchMessage(){
        if(messageHandleContext!=null){
            messageHandleContext.stopDispatch();
        }
    }

}

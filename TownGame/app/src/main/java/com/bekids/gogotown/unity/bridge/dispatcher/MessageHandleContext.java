package com.bekids.gogotown.unity.bridge.dispatcher;

import android.text.TextUtils;

import com.bekids.gogotown.unity.bridge.annotation.AnnotationScannerUtils;
import com.bekids.gogotown.unity.bridge.interf.IMessageHandleStrategy;
import com.bekids.gogotown.unity.bridge.interf.IUnityView;
import com.bekids.gogotown.unity.bridge.log.UnityBridgeLogPrinter;

import java.util.HashMap;
import java.util.Set;

/**
 * Author: LuckyFind
 * Date: 2021/1/27
 * Desc: 策略模式的上下文，在这里维护策略类的引用，在这里选中相应的策略
 *
 */
public class MessageHandleContext {
    private IUnityView iUnityView;
    private BaseAbstractStrategy baseStrategy;
    private HashMap<String, IMessageHandleStrategy> strategyHashMap;
    private Set<String> strategyKeySet;

    public MessageHandleContext(IUnityView unityView) {
        this.iUnityView = unityView;
        generateStrategyList();
    }

    /**
     * 将UnityMessage分发给具体的策略类去执行
     * 策略类是在运行时通过反射创建的，实例存储在strategyHashMap中
     * key：{@RegisterUnityMethod}注解值的字符串，例如“read_text_from_android_assets/copy_file_from_android_assets_to”
     * value：对应的策略类
     * 如果有两个策略类具有相同的RegisterUnityMethod值，那么在调用时候之后用最先查找到的一个
     * 所以不建议使用相同的值
     * @param unityMessage
     * @return
     */
    public String dispatchMessage(UnityMessage unityMessage) {
        if (TextUtils.isEmpty(unityMessage.getMethodName())) {
            UnityBridgeLogPrinter.getInstance().printDispatchUnityMessageError(unityMessage,"The message  is illegal !!");
            return "The method name is null !!";
        }
        if (strategyHashMap == null || strategyHashMap.size() == 0) {
            UnityBridgeLogPrinter.getInstance().printDispatchUnityMessageError(unityMessage,"StrategyHashMap is null ,can not found match strategy!");

            return "Not found match method,please check it !";
        }
        baseStrategy = (BaseAbstractStrategy) strategyHashMap.get(findMatchStrategy(unityMessage.getMethodName()));

        if (baseStrategy == null) {
            UnityBridgeLogPrinter.getInstance().printDispatchUnityMessageError(unityMessage,"Can not found match strategy!");
            return "Not found match method,please check it !";
        }
        UnityBridgeLogPrinter.getInstance().printDispatchUnityMessageInfo(unityMessage,baseStrategy.getClass().getSimpleName());
        String result = baseStrategy.handleUnityMessage(unityMessage);

        return result;

    }

    /**
     * 根据注解找到对应的策略类
     * @param key
     * @return
     */
    private String findMatchStrategy(String key) {
        key = key + "/";
        String keyResult = "";
        if (strategyKeySet != null && strategyKeySet.size() > 0) {
            for (String s : strategyKeySet) {
                if (s.contains(key)) {
                    keyResult = s;
                    break;
                }
            }
        }
        return keyResult;
    }

    private void generateStrategyList() {
        strategyHashMap = AnnotationScannerUtils.getAllStrategy(this.iUnityView.getUnityContext());
        strategyKeySet = strategyHashMap.keySet();
        if (strategyKeySet.size() > 0) {
            for (String s : strategyKeySet) {
                if(!TextUtils.isEmpty(s)&&strategyHashMap.get(s)!=null){
                    strategyHashMap.get(s).bindIUnityView(this.iUnityView);
                }
            }
        }
    }

    public void stopDispatch() {
        strategyHashMap.clear();
        strategyKeySet.clear();
        this.iUnityView = null;
        baseStrategy.destroy();
    }
}

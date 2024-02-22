package com.bekids.gogotown.unity.bridge.dispatcher;

import android.content.Context;
import android.util.Log;

import com.bekids.gogotown.unity.bridge.NativeCommunicatorForAndroid;
import com.bekids.gogotown.unity.bridge.interf.IGenerateResultJson;
import com.bekids.gogotown.unity.bridge.interf.IMessageHandleStrategy;
import com.bekids.gogotown.unity.bridge.interf.INativeCallUnity;
import com.bekids.gogotown.unity.bridge.interf.IUnityView;
import com.bekids.gogotown.unity.bridge.log.UnityBridgeLogPrinter;
import com.bekids.gogotown.util.string.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:处理unity消息的策略类的基类
 */
public abstract class BaseAbstractStrategy implements IMessageHandleStrategy, INativeCallUnity, IGenerateResultJson {
    protected Gson gson;
    private IUnityView iUnityView;

    @Override
    public void bindIUnityView(IUnityView iUnityView) {
        if (this.iUnityView == null) {
            this.iUnityView = iUnityView;
            gson = new GsonBuilder().setLenient().create();
        }
    }

    protected Context getContext() {
        if (iUnityView == null) {
            throw new RuntimeException("you must call bindIUnityView(IUnityView iUnityView) before handleMessage ");
        }
        return iUnityView.getUnityContext();
    }

    protected IUnityView getUnityView() {
        if (iUnityView == null) {
            throw new RuntimeException("you must call bindIUnityView(IUnityView iUnityView) before handleMessage ");
        }
        return iUnityView;
    }

    protected void destroy() {
    }


    /**
     * 具体的消息处理方法
     *
     * @param method
     * @param jsonArray
     * @param blockId
     * @return
     * @throws JSONException
     * @throws IndexOutOfBoundsException
     */
    protected abstract String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException;

    /**
     * 处理unity传过来的消息，这里是第一层处理，主要是为了校验和处理参数，捕获异常
     * 避免在具体实现中一一捕获
     * 实际消息处理会交给 process 方法
     *
     * @param unityMessage
     * @return
     */
    @Override
    public String handleUnityMessage(UnityMessage unityMessage) {
        Log.d("BaseAbstractStrategy", "Thread111----:" + Thread.currentThread().getName());
        String result = "";
        try {
            JSONArray jsonArray = null;
            if (StringUtils.isEmpty(unityMessage.getParams()) || unityMessage.getParams().equals("{}")) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(unityMessage.getParams());
            }
            result = process(unityMessage.getMethodName(), jsonArray, unityMessage.getBlockid());
            UnityBridgeLogPrinter.getInstance().printProcessUnityMessageInfo(getClass().getSimpleName(), unityMessage.getMethodName(), unityMessage.getParams(), unityMessage.getBlockid(), result);
        } catch (JSONException e) {
            e.printStackTrace();
            UnityBridgeLogPrinter.getInstance().printProcessUnityMessageError(getClass().getSimpleName(), unityMessage.getMethodName(), unityMessage.getParams(), unityMessage.getBlockid(), "参数格式异常，不是合法json");
            return "参数格式异常，不是合法json";
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            UnityBridgeLogPrinter.getInstance().printProcessUnityMessageError(getClass().getSimpleName(), unityMessage.getMethodName(), unityMessage.getParams(), unityMessage.getBlockid(), "参数列表不合法,数组下标越界");
            return "参数格式不合法,数组下标越界";
        }
        return result;
    }


    /**
     * @param method
     * @param params
     * @param blockId
     * @param originMethod
     */
    @Override
    public void callUnity(String method, String params, String blockId, String originMethod) {
        UnityBridgeLogPrinter.getInstance().printUnityMessageCallbackInfo(getClass().getSimpleName(), method,
                params, blockId, originMethod);
        NativeCommunicatorForAndroid.shared_instance().callUnity(method, params, blockId, originMethod);
    }

    protected String generateNormalJson(JSONObject o) {
        if (o == null) {
            return "";
        }
        return o.toString();



    }


    protected String generateNormalFailedJson(int errCode, String errMsg) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", errCode);
            jsonObject.put("message", errMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public String generateBooleanJson(boolean b) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "bool");
            jsonObject.put("value", b);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public String generateNumJson(int num) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "num");
            jsonObject.put("value", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public String generateNumJson(long num) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "num");
            jsonObject.put("value", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public String generateNumJson(float num) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "num");
            jsonObject.put("value", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public String generateObjJson(JSONObject o) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "obj");
            jsonObject.put("value", o);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }


}

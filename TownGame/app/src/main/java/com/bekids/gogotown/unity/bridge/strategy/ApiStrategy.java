package com.bekids.gogotown.unity.bridge.strategy;//package com.ihuman.enlighten.unity.bridge.strategy;

import android.text.TextUtils;
import android.util.Log;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.bekids.gogotown.util.string.StringUtils;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.library.accountsdk.IHNetworkHelp;
import com.ihuman.sdk.lib.model.FailMessage;
import com.ihuman.sdk.lib.network.api.IHNetFetch;
import com.ihuman.sdk.lib.network.callback.IHNetworkFetchCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Author: LuckyFind
 * Date: 2021/2/2
 * fetchApiWithUrl网络请求
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_FETCH_API_WITH_URL, MessageConstants.MESSAGE_FEEDBACK_URL_WITH_EXTRA})
public class ApiStrategy extends BaseAbstractStrategy {

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_FETCH_API_WITH_URL:
                result = fetchApiWithUrl(jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(4), blockId);
                break;
            case MessageConstants.MESSAGE_FEEDBACK_URL_WITH_EXTRA:
                result = getFeedbackUrlWithExtra();
                break;
            default:
                break;
        }
        return result;
    }

    private String getFeedbackUrlWithExtra() {

        return AccountHelp.getInstance().feedbackURLWithExtra(null);
    }

    private String fetchApiWithUrl(String url, String signParam, String type, String blockId) {
        HashMap<String, String> signHashMap = new HashMap<>();
        parseUnKnownJson(signHashMap, signParam);
        signHashMap.put("platform", "android");
        if (StringUtils.isEmpty(type)) {
            type = "post";
        }

        IHNetworkHelp.getInstance()
                .fetchAPIWithAllSign(url, signHashMap, type.equals("post")? IHNetFetch.POST:IHNetFetch.GET, new IHNetworkFetchCallback() {
                    @Override
                    public void onFailure(FailMessage failmsg) {
                        fetchApiFailed(failmsg.getCode(), failmsg.getMessage(), url, blockId);
                    }

                    @Override
                    public void onSuccess(String result) {
                        fetchApiSuccess(result, url, blockId);
                    }
                });
        return "";
    }

    private void fetchApiSuccess(String res, String url, String blockId) {
        try {
            Log.d("ApiStrategy", "====fetchApiWithUrl====" +
                    "\nurl:" + url +
                    "\nresult:" + res);
            JSONObject callback_obj = new JSONObject();
            JSONObject object = new JSONObject(res);
            callback_obj.putOpt("data", object);
            callback_obj.put("code", 0);
            callback_obj.put("url", url);
            callUnity(MessageConstants.MESSAGE_FETCH_API_WITH_URL, generateNormalJson(callback_obj), blockId, MessageConstants.MESSAGE_FETCH_API_WITH_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchApiFailed(int code, String msg, String url, String blockId) {
        JSONObject failJsonObject = new JSONObject();
        try {
            failJsonObject.put("code", code);
            failJsonObject.put("message", msg);
            failJsonObject.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callUnity(MessageConstants.MESSAGE_FETCH_API_WITH_URL, generateNormalJson(failJsonObject), blockId, MessageConstants.MESSAGE_FETCH_API_WITH_URL);
    }

    private void parseUnKnownJson(HashMap<String, String> hashMap, String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                Iterator<String> objs = jsonObject.keys();
                String key;
                while (objs.hasNext()) {
                    key = objs.next();
                    String value = jsonObject.optString(key);
                    hashMap.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}


package com.bekids.gogotown.unity.bridge.strategy;

import com.bekids.gogotown.BuildConfig;
import com.bekids.gogotown.base.utils.DeviceUtils;
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.sdk.lib.utils.IHUtilHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:App运行的各种信息
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_GET_ENVIRONMENT})
public class EnvironmentStrategy extends BaseAbstractStrategy {

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("debug_mode", BuildConfig.DEBUG);
        jsonObject.put("env", BuildConfig.APP_ENVIRONMENT);
        jsonObject.put("device_id",AccountHelp.getInstance().getDeviceId());
        jsonObject.put("platform","android");
        jsonObject.put("phone", !(IHUtilHelper.getInstance().isPad()|| DeviceUtils.isTablet(getContext())));
        jsonObject.put("app_version",BuildConfig.VERSION_NAME);
        jsonObject.put("app_url","todo");
        jsonObject.put("channel", "Google");
        return generateObjJson(jsonObject);
    }

}

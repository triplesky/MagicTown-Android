package com.bekids.gogotown.unity.bridge.strategy;

import android.content.pm.ActivityInfo;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.sdk.module.privacy.IHPrivacyComponent;
import com.ihuman.sdk.module.privacy.callback.IHPrivacyCenterType;
import com.ihuman.sdk.module.privacy.callback.InfoManagementCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:隐私策略
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_PRIVACY_URL_TITLE,MessageConstants.MESSAGE_PRIVACY_URL, MessageConstants.MESSAGE_SHOW_PRIVACY_CENTER_WITH_USER_INFO,
MessageConstants.MESSAGE_TERMS_URL, MessageConstants.MESSAGE_TERMS_URL_TITLE,MessageConstants.MESSAGE_PRIVACY_CENTER_TITLE})
public class PrivacyStrategy extends BaseAbstractStrategy {

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_PRIVACY_URL_TITLE:
                JSONObject object = new JSONObject();
                try {
                    object.put("type", "obj");
                    object.put("value", get_privacy_url_title());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = object.toString();
                break;
            case MessageConstants.MESSAGE_PRIVACY_URL:
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "obj");
                    obj.put("value", get_privacy_url());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = obj.toString();
                break;
            case MessageConstants.MESSAGE_SHOW_PRIVACY_CENTER_WITH_USER_INFO:
                result = openPrivacyCenterWithUserInfo(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_TERMS_URL_TITLE:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "obj");
                    jsonObject.put("value", openTermsTitle());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = jsonObject.toString();
                break;
            case MessageConstants.MESSAGE_TERMS_URL:
                JSONObject json = new JSONObject();
                try {
                    json.put("type", "obj");
                    json.put("value", openTermsUrl());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = json.toString();
                break;
            case MessageConstants.MESSAGE_PRIVACY_CENTER_TITLE:
                JSONObject j = new JSONObject();
                try {
                    j.put("type", "obj");
                    j.put("value", privacy_center_title());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result = j.toString();
                break;
            default:
                break;
        }
        return result;
    }

    private String openTermsTitle() {
        return IHPrivacyComponent.getInstance().termsURLTitle();
    }

    private String openPrivacyCenterWithUserInfo(String currentUserId) {
        IHPrivacyComponent.getInstance()
                .showPrivacyCenterWithUserInfo(AccountHelp.getInstance().getCurrentUid(), ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,new InfoManagementCallback() {
                    @Override
                    public void IHPrivacyCenterBlock(IHPrivacyCenterType type) {
                    }
                });
        return "open PrivacyCenter success";
    }

    private String get_privacy_url_title() {
        return IHPrivacyComponent.getInstance().privacyURLTitle();
    }

    private String get_privacy_url() {
        return IHPrivacyComponent.getInstance().privacyURL();
    }

    private String openTermsUrl() {
        return IHPrivacyComponent.getInstance().termsURL();
    }

    private String privacy_center_title() {
        return IHPrivacyComponent.getInstance().privacyCenterTitle();
    }
}

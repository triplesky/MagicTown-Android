package com.bekids.gogotown.unity.bridge.strategy;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.sdk.lib.account.model.IHAccountBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:登录相关
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_CURRENT_ACCOUNT, MessageConstants.MESSAGE_LAST_ACCOUNT,
        MessageConstants.MESSAGE_MAIN_PAGE_DID_SHOW})
public class LoginInfoStrategy extends BaseAbstractStrategy  {

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_CURRENT_ACCOUNT:
                result = getCurrentAccountInfo();
                break;
            case MessageConstants.MESSAGE_LAST_ACCOUNT:
                result = getLastAccountInfo();
                break;
            case MessageConstants.MESSAGE_MAIN_PAGE_DID_SHOW:
                hideLoginFragment();
                break;
            default:
                break;
        }
        return result;
    }

    private void hideLoginFragment() {
        AccountHelp.getInstance().getIhumanHandler().post(new Runnable() {
            @Override
            public void run() {
                getUnityView().hideUnityLoadingDialog();
                getUnityView().hideLoginFragment();
            }
        });

    }



    private String getLastAccountInfo() {
        IHAccountBean ihAccountBean;
        if (null == AccountHelp.getInstance()
                .getLastAccount()) {
            ihAccountBean = new IHAccountBean();
        } else {
            ihAccountBean = AccountHelp.getInstance()
                    .getLastAccount();
        }
        return generateObjJson(generateLoginBean(ihAccountBean));
    }

    private String getCurrentAccountInfo() {
        IHAccountBean ihAccountBean;
        if (null == AccountHelp.getInstance()
                .getCurrentAccount()) {
            ihAccountBean = new IHAccountBean();
        } else {
            ihAccountBean = AccountHelp.getInstance()
                    .getCurrentAccount();
        }
        return generateObjJson(generateLoginBean(ihAccountBean));
    }

    /**
     * private String uid;
     * private String safeuid;
     * private String channel;
     * private String mobile;
     * private String email;
     * private boolean sim_login;
     * private String utoken;
     */
    private JSONObject generateLoginBean(IHAccountBean ihAccountBean) {
        JSONObject object = new JSONObject();
        try {
            object.put("uid", null == ihAccountBean.getUserid() ? "" : ihAccountBean.getUserid());
            object.put("safeuid", null == ihAccountBean.getSafeUid() ? "" : ihAccountBean.getSafeUid());
            object.put("channel", null == ihAccountBean.getChannelType() ? "" : ihAccountBean.getChannelType());
            object.put("mobile", null == ihAccountBean.getMobile() ? "" : ihAccountBean.getMobile());
            object.put("email", null == ihAccountBean.getEmail() ? "" : ihAccountBean.getEmail());
            object.put("utoken", null == ihAccountBean.getUToken() ? "" : ihAccountBean.getUToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

}

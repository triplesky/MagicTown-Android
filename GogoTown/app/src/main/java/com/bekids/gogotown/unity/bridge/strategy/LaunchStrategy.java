package com.bekids.gogotown.unity.bridge.strategy;

import android.content.Intent;

import com.bekids.gogotown.MainActivity;
import com.bekids.gogotown.MainApplication;
import com.bekids.gogotown.router.RouterPath;
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.sdk.module.launch.IHLaunchComponent;
import com.ihuman.sdk.module.launch.callback.IHLaunchViewCallback;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * **************************************
 * 项目名称: bekids Town
 *
 * @Author ll
 * 创建时间: 2023/6/5    11:16
 * 用途
 * **************************************
 */
@RegisterUnityMethod(MessageConstants.MESSAGE_APP_SHOW_LAUNCH_VIEW_ACTION)
public class LaunchStrategy extends BaseAbstractStrategy {
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_APP_SHOW_LAUNCH_VIEW_ACTION:
                showLaunch(method, jsonArray, blockId);
                break;
        }
        return result;
    }


    private void showLaunch(String method, JSONArray jsonArray, String blockId){
        MainApplication.mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IHLaunchComponent.getInstance().showLaunchView(MainApplication.mainActivity, new IHLaunchViewCallback() {
                    @Override
                    public void onDismiss() {
                        callUnity(method, "", blockId, "");
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                    }
                });
            }
        });
    }

}

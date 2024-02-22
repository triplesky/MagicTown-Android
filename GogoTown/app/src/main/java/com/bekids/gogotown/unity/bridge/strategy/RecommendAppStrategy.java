package com.bekids.gogotown.unity.bridge.strategy;

import android.app.Activity;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.bean.RecommendAppCheckResult;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.google.gson.JsonObject;
import com.ihuman.sdk.lib.webkit.ui.IHCloseCallback;
import com.ihuman.sdk.module.recommend.IHRecommendComponent;
import com.ihuman.sdk.module.recommend.bean.IHRecommendApp;
import com.ihuman.sdk.module.recommend.callback.IHSelectRecommendCallback;
import com.ihuman.sdk.module.recommend.callback.IHShowRecommendAppsCallback;
import com.ihuman.sdk.module.recommend.ui.type.IHAGDType;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: LuckyFind
 * Date: 2021/12/1
 * Description:互推
 */
@RegisterUnityMethod({
MessageConstants.MESSAGE_CHECK_RECOMMEND_APPS_STATUS_WITH_BLOCK, MessageConstants.MESSAGE_SHOW_RECOMMEND_VIEW_WITH_SELECTED_AND_HIDE_BLOCK,
MessageConstants.MESSAGE_HANDLE_SELECTED_APP, MessageConstants.MESSAGE_HIDE_RECOMMAND_VIEW, MessageConstants.MESSAGE_HANDLE_SELECTED_APP_OPEN_STORE_IN_APP
})
public class RecommendAppStrategy extends BaseAbstractStrategy {
    private boolean ready;

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        switch (method) {
            case MessageConstants.MESSAGE_CHECK_RECOMMEND_APPS_STATUS_WITH_BLOCK:

                IHRecommendComponent.getInstance().checkRecommendAppsStatus(new IHShowRecommendAppsCallback() {
                    @Override
                    public void onResult(boolean isReady, List<IHRecommendApp> appList) {
                        ready = isReady;
                        reportRecommendAppCheckResult(isReady, appList, blockId);
                    }

                });
                break;
            case MessageConstants.MESSAGE_SHOW_RECOMMEND_VIEW_WITH_SELECTED_AND_HIDE_BLOCK:
                IHRecommendComponent.getInstance().showRecommendView((Activity) getContext(), new IHSelectRecommendCallback() {
                    @Override
                    public void onSelected(IHRecommendApp recommendApp, boolean isOpen) {
                        reportRecommendAppSelectionAction(true, true, recommendApp, blockId);
                        IHRecommendComponent.getInstance().handleSelectedApp(IHAGDType.AUTO_DOWNLOAD_FOR_DEFAULT);
                    }
                }, new IHCloseCallback() {
                    @Override
                    public void onClose() {
                        reportRecommendAppSelectionAction(false, false, null, blockId);
                    }
                });
                break;
            case MessageConstants.MESSAGE_HANDLE_SELECTED_APP_OPEN_STORE_IN_APP:
                break;
            case MessageConstants.MESSAGE_HANDLE_SELECTED_APP:
                IHRecommendComponent.getInstance().handleSelectedApp();
                break;
            case MessageConstants.MESSAGE_HIDE_RECOMMAND_VIEW:
                IHRecommendComponent.getInstance().hideRecommendView();
                break;
        }
        return "";
    }

    private void reportRecommendAppCheckResult(boolean isReady, List<IHRecommendApp> appList, String blockId) {
        RecommendAppCheckResult recommendAppCheckResult = new RecommendAppCheckResult();
        List<RecommendAppCheckResult.UnityRecommendApp> unityRecommendApps =new ArrayList<>();
        if (appList!=null&&appList.size()>0){
            for (int i=0;i<appList.size();i++){
                RecommendAppCheckResult.UnityRecommendApp unityRecommendApp =new RecommendAppCheckResult.UnityRecommendApp();
                unityRecommendApp.setApp_id(appList.get(i).getAppId());
                unityRecommendApp.setIcon_path(appList.get(i).getIconImagePath());
                unityRecommendApp.setInstalled(appList.get(i).isInstalled());
                unityRecommendApps.add(unityRecommendApp);
            }
            recommendAppCheckResult.setIs_ready(true);
        }else {
            recommendAppCheckResult.setIs_ready(false);
        }
        recommendAppCheckResult.setApps(unityRecommendApps);
        callUnity("",
                gson.toJson(recommendAppCheckResult),
                blockId,
                MessageConstants.MESSAGE_CHECK_RECOMMEND_APPS_STATUS_WITH_BLOCK);
    }

    private void reportRecommendAppSelectionAction(boolean openAction, boolean isOpen, IHRecommendApp recommendApp, String blockId) {
        JsonObject jsonObject = new JsonObject();
        if (openAction) {
            JsonObject object = new JsonObject();
            object.addProperty("app_id", recommendApp.getAppId());
            object.addProperty("icon_path", recommendApp.getIconImagePath());
            object.addProperty("installed", recommendApp.isInstalled());
            jsonObject.addProperty("open_act", true);
            jsonObject.addProperty("is_open", isOpen);
            jsonObject.add("app", object);
        } else {
            jsonObject.addProperty("open_act", false);
        }

        callUnity("",
                jsonObject.toString(),
                blockId,
                MessageConstants.MESSAGE_SHOW_RECOMMEND_VIEW_WITH_SELECTED_AND_HIDE_BLOCK);
    }
}

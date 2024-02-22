package com.bekids.gogotown.unity.bridge.strategy;

import static com.bekids.gogotown.unity.bridge.bean.MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_DIRECTION;

import android.content.pm.ActivityInfo;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.sdk.lib.statistics.IHStatisticsComponent;
import com.ihuman.sdk.lib.webkit.IHWebKitComponent;
import com.ihuman.sdk.lib.webkit.ui.IHWebViewCallback;
import com.ihuman.sdk.lib.webkit.ui.titlebar.CustomTitle;
import com.ihuman.sdk.lib.webkit.ui.titlebar.IHWebViewTitleBarConfig;
import com.ihuman.sdk.lib.widget.manage.IHActivityManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:开启指定网页
 */
@RegisterUnityMethod({MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_DIRECTION,
        MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_NAV_CONFIG,
        MessageConstants.MESSAGE_FEEDBACK_URL_WITH_EXTRA,
        MessageConstants.MESSAGE_HIDE_ALL_WEB_VIEW,
        MessageConstants.MESSAGE_PRELOAD_WEBVIEW_WITH_URL})
public class WebOperationStrategy extends BaseAbstractStrategy {
    HashMap<String,String> blockIdMap = new HashMap<>();
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";

        blockIdMap.put(method,blockId);
        switch (method) {

            case MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_DIRECTION:
                result = openWebView(jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2), jsonArray.getInt(3));
                break;
            case MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_NAV_CONFIG:
                result = openWebViewWithNavConfig(jsonArray.getString(0), jsonArray.getString(1),
                        jsonArray.getString(2), jsonArray.getInt(3),
                        jsonArray.getJSONObject(4).getInt("navbar_alpha"),
                        jsonArray.getJSONObject(4).getInt("navbar_height"));
                break;
            case MessageConstants.MESSAGE_FEEDBACK_URL_WITH_EXTRA:
                result = AccountHelp.getInstance().feedbackURLWithExtra(null);
                break;
            case MessageConstants.MESSAGE_HIDE_ALL_WEB_VIEW:
                AccountHelp.getInstance().getIhumanHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        IHActivityManager.getInstance().finishAllActivity();
                    }
                });
                break;
            case MessageConstants.MESSAGE_PRELOAD_WEBVIEW_WITH_URL:
                result = preload_webview_with_url(jsonArray.getString(0));
                break;
            default:
                break;
        }
        return result;
    }

    private String preload_webview_with_url(String url){
        IHWebKitComponent.getInstance().preload(url);
        return "preload_success";
    }

    private String openWebViewWithNavConfig(String url, String title, String scene, int ori, int navbar_alpha, int navbar_height) {
        IHWebViewTitleBarConfig.Builder builder = new IHWebViewTitleBarConfig.Builder();
        builder.navBarAlpha(navbar_alpha)
                        .navBarHeight(navbar_height)
                .setTranslucent(true).setHideTitleBar(true)
                                .build();
        IHWebKitComponent.getInstance().showWebView(getContext(), url, new CustomTitle(title), 0, scene,  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE , new IHWebViewTitleBarConfig(builder),true,new IHWebViewCallback() {
            @Override
            public void onFinish(String url) {

            }

            @Override
            public void onError(String url, int code, String message) {
                IHWebKitComponent.getInstance().hideWebView();
                Map resultMap = new HashMap();
                resultMap.put("reason", message);
                IHStatisticsComponent.getInstance().onEvent("open_web_view_fail", resultMap);
            }

            @Override
            public void onClose() {
                callUnity(
                        MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_NAV_CONFIG,
                        "",
                        blockIdMap.get(MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_NAV_CONFIG),
                        MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_NAV_CONFIG
                );
                getUnityView().onWebViewClosed();
            }
        });
        return "open WebView success ";
    }

    private String openWebView(String url, String title, String scene, int ori) {

        IHWebKitComponent.getInstance().showWebView(getContext(), url, new CustomTitle(title), 0, scene,  ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE , new IHWebViewCallback() {
                    @Override
                    public void onFinish(String url) {

                    }

                    @Override
                    public void onError(String url, int code, String message) {
                        IHWebKitComponent.getInstance().hideWebView();
                        Map resultMap = new HashMap();
                        resultMap.put("reason", message);
                        IHStatisticsComponent.getInstance().onEvent("open_web_view_fail", resultMap);
                    }

                    @Override
                    public void onClose() {
                        callUnity(
                                MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_DIRECTION,
                                "",
                                blockIdMap.get(MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_DIRECTION),
                                MessageConstants.MESSAGE_SHOW_WEBVIEW_WITH_URL_AND_DIRECTION
                        );
                        getUnityView().onWebViewClosed();
                    }
                });
        return "open WebView success ";
    }
}

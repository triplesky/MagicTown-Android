package com.bekids.gogotown.unity.bridge.strategy;

import android.util.Log;

import com.bekids.gogotown.BuildConfig;
import com.bekids.gogotown.MainApplication;
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.sdk.lib.inapp_review.IHInAppReviewComponent;
import com.ihuman.sdk.lib.inapp_review.IHPublicInAppReviewApi;
import com.ihuman.sdk.lib.platform.utils.IHMarketUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * **************************************
 * 项目名称: Town Game
 *
 * @Author ll
 * 创建时间: 2023/9/6    16:59
 * 用途
 * **************************************
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_CAN_OPEN_URL,
        MessageConstants.MESSAGE_CAN_OPEN_APP,
        MessageConstants.MESSAGE_OPEN_APP,
        MessageConstants.MESSAGE_OPEN_APP_STORE_TO_APP,
        MessageConstants.MESSAGE_GET_GOGO_TOWN_APP_SCHEME_LIST, MessageConstants.MESSAGE_SHOW_IN_APP_REVIEW})
public class AppStoreStrategy extends BaseAbstractStrategy {
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_CAN_OPEN_URL:
               // IHRecommendComponent.getInstance().downloadOrOpenApp(IHAGDType.AUTO_DOWNLOAD_FOR_DEFAULT, "包名");
                if (jsonArray.getString(0).endsWith("//")) {
                    String pack_name = jsonArray.getString(0).substring(0, jsonArray.getString(0).indexOf(":"));

                    Log.i("lldebug can", pack_name);
                    result = generateBooleanJson(IHMarketUtils.canOpenApp(pack_name));
                    //IHRecommendComponent.getInstance().downloadOrOpenApp(IHAGDType.AUTO_DOWNLOAD_FOR_DEFAULT, pack_name);
//                    if (IHAppUtils.INSTANCE.isAppInstalled(pack_name)) {
//                        result = generateBooleanJson(IHMarketUtils.canOpenApp(pack_name));
//                    }else {
//                        result = generateBooleanJson(false); MessageConstants.MESSAGE_SHOW_IN_APP_REVIEW
//                    }
                } else{
                    result = generateBooleanJson(true);
                }

                break;
            case MessageConstants.MESSAGE_SHOW_IN_APP_REVIEW:
                IHInAppReviewComponent.getInstance().showInAppReview(MainApplication.mainActivity, new IHPublicInAppReviewApi.OnInAppReviewListener() {
                    @Override
                    public void onComplete() {

                    }
                });
                break;
            case MessageConstants.MESSAGE_CAN_OPEN_APP:
                result = generateBooleanJson(IHMarketUtils.canOpenApp(jsonArray.getString(0)));
                break;
            case MessageConstants.MESSAGE_OPEN_APP:
                result = generateBooleanJson(IHMarketUtils.openApp(jsonArray.getString(0)));
                break;
            case MessageConstants.MESSAGE_OPEN_APP_STORE_TO_APP:
                IHMarketUtils.gotoMarket(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_GET_GOGO_TOWN_APP_SCHEME_LIST:
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "arr");
                    jsonObject.put("value", listCovert2arr(get_gogo_town_app_scheme_list()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("lldebug list", jsonObject.toString());
                result =  jsonObject.toString();
                break;
        }
        return result;
    }

    private JSONArray listCovert2arr(List<String> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject tmpObj = null;
        int count = list.size();
        for(int i = 0; i < count; i++)
        {
            jsonArray.put(list.get(i));

        }
        return jsonArray;

    }

    private List<String> get_gogo_town_app_scheme_list(){
        List<String> list = new ArrayList<>();
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(getContext().getAssets().open("package_name.csv"));
            BufferedReader reader = new BufferedReader(is);
            reader.readLine();//读取每行
            String line;
            while ((line = reader.readLine()) != null) {
                if (BuildConfig.FLAVOR.equals("staging")) {
                    line = line + ".staging";
                }
                if (!BuildConfig.APPLICATION_ID.equals(line)) {
                    list.add(line);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

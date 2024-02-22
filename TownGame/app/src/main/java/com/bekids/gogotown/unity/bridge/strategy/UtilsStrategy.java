package com.bekids.gogotown.unity.bridge.strategy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bekids.gogotown.base.utils.ClipboardUtil;
import com.bekids.gogotown.base.utils.DeviceUtils;
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.google.gson.JsonObject;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.sdk.lib.abtest.IHABTestComponent;
import com.ihuman.sdk.lib.abtest.IHPublicABTestApi;
import com.ihuman.sdk.lib.network.detect.IHNetDiagnoseHelp;
import com.ihuman.sdk.lib.utils.IHDeviceUtils;
import com.ihuman.sdk.lib.utils.MD5Utils;
import com.ihuman.sdk.lib.utils.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Author: LuckyFind
 * Date: 2021/2/4
 * Desc:工具类操作
 */
@RegisterUnityMethod(
        {MessageConstants.MESSAGE_IS_APP_INSTALLED, MessageConstants.MESSAGE_OPEN_APP_PREFERENCE
        , MessageConstants.MESSAGE_CURRENT_SERVER_TIME, MessageConstants.MESSAGE_DEVICE_FREE_SPACE,
        MessageConstants.MESSAGE_REGISTER_SYSTEM_NOTIFICATION, MessageConstants.MESSAGE_PASTEBOARD_TEXT,
        MessageConstants.MESSAGE_JUMP_TO_WECHAT, MessageConstants.MESSAGE_ANDROID_UNITY_INIT_DID_COMPLETE,
        MessageConstants.MESSAGE_GET_ABTEST_GROUP_ID, MessageConstants.MESSAGE_OPEN_MINI_PROGRAM_WITH_USER_NAME,
        MessageConstants.MESSAGE_SHOW_NET_DIAGNOSE_VIEW_WITH_CONFIG, MessageConstants.MESSAGE_GOGO_KEY_P1, MessageConstants.MESSAGE_GOGO_KEY_P1_P2,
        MessageConstants.MESSAGE_GET_STRING_MD5, MessageConstants.MESSAGE_DATE_STRING_WITH_SYSTEM_TIMEZONE,
        MessageConstants.MESSAGE_GET_AB_CONFIG_WITH_BLOCK,MessageConstants.MESSAGE_SET_AB_CONFIG,
        MessageConstants.MESSAGE_APP_REGION}
        )
public class UtilsStrategy extends BaseAbstractStrategy {


    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_IS_APP_INSTALLED:
                result = isAppInstalled(jsonArray.getInt(0));
                break;
            case MessageConstants.MESSAGE_OPEN_APP_PREFERENCE:
                result = openAppSettingView();
                break;
            case MessageConstants.MESSAGE_CURRENT_SERVER_TIME:
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("type", "num");
                jsonObject.addProperty("value", TimeUtils.getCurrentServerTime());
                result = jsonObject.toString();
                break;
            case MessageConstants.MESSAGE_DEVICE_FREE_SPACE:
                result = generateNumJson(DeviceUtils.getInternalAvailableMemorySize(getContext()));
                break;
            case MessageConstants.MESSAGE_PASTEBOARD_TEXT:
                clipTextToSystemBoard(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_JUMP_TO_WECHAT:
                jumpToWechat();
                break;
            case MessageConstants.MESSAGE_REGISTER_SYSTEM_NOTIFICATION:
                //todo
                break;
            case MessageConstants.MESSAGE_ANDROID_UNITY_INIT_DID_COMPLETE:
                hideUnityLoading();
                break;
            case MessageConstants.MESSAGE_GET_ABTEST_GROUP_ID:
                result = getAbTestGroupId();
                break;
            case MessageConstants.MESSAGE_OPEN_MINI_PROGRAM_WITH_USER_NAME:
                openMiniProgram(jsonArray.getString(0), jsonArray.getString(1), jsonArray.optInt(2), jsonArray.getString(3));
                break;
            case MessageConstants.MESSAGE_SHOW_NET_DIAGNOSE_VIEW_WITH_CONFIG:
                diagnoseNetwork(jsonArray.getJSONObject(0));
                break;
            case MessageConstants.MESSAGE_GOGO_KEY_P1:
                result = generateStringJson(gogo_key_p1(jsonArray.getString(0), jsonArray.getString(1)));
                break;
            case MessageConstants.MESSAGE_GOGO_KEY_P1_P2:
                result = generateStringJson(gogo_key_p1_p2(jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2)));
                break;
            case MessageConstants.MESSAGE_GET_STRING_MD5:
                result = generateStringJson(MD5Utils.INSTANCE.encode(jsonArray.getString(0)));
                break;
            case MessageConstants.MESSAGE_DATE_STRING_WITH_SYSTEM_TIMEZONE:
                result = generateStringJson(formatDateToString(jsonArray.getLong(0) * 1000, jsonArray.getString(1)));
                break;
            case MessageConstants.MESSAGE_GET_AB_CONFIG_WITH_BLOCK:
                result = getABConfigWithBlock(blockId);
                break;
            case MessageConstants.MESSAGE_SET_AB_CONFIG:
                IHABTestComponent.getInstance().setABConfig(jsonArray.getString(0));
                result = "";
                break;
            case MessageConstants.MESSAGE_APP_REGION:
                result = generateStringJson(getCurrentTimeZone());
                break;
            default:
                break;
        }
        return result;
    }


    public  String getCurrentTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String strTz = tz.getDisplayName(false, TimeZone.SHORT);
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(getContext().getAssets().open("country.csv"));
            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(tz.getID())){
                    strTz = line.split(",")[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strTz;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCountryCode(String countryName) {
        String country = Locale.getDefault(Locale.Category.FORMAT).getCountry();
        // Get all country codes in a string array.
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();
        Locale locale;
        String name;

        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            locale = new Locale("", code);
            // Get country name for each code.
            name = locale.getDisplayCountry();
            // Map all country names and codes in key - value pairs.
            countryMap.put(name, code);
        }

        // Return the country code for the given country name using the map.
        // Here you will need some validation or better yet
        // a list of countries to give to user to choose from.
        return countryMap.get(countryName); // "NL" for Netherlands.
    }



    private String getABConfigWithBlock(String blockId) {


        IHABTestComponent.getInstance().getABConfig(new IHPublicABTestApi.IABTestConfigCallback() {
            @Override
            public void onResult(@Nullable String s) {
                Log.i("lldebug deviceid", IHDeviceUtils.INSTANCE.getDeviceId());
                Log.i("lldebug abstring", s);
                IHABTestComponent.getInstance().setABConfig(s);
                JSONObject callback_obj = new JSONObject();
                JSONObject object = null;
                try {

                    callback_obj.put("ab_json_string", s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callUnity(MessageConstants.MESSAGE_NOTI_AB_CONFIG_DID_CHANGE,
                        generateNormalJson(callback_obj),
                        "",
                        MessageConstants.MESSAGE_NOTI_AB_CONFIG_DID_CHANGE);
            }
        });
        return "";
    }

    public static String formatDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return format.format(date);
    }

    /**
     * diagnose network
     *
     * @param jsonObject
     */
    private void diagnoseNetwork(JSONObject jsonObject) {
        String apiUrl = jsonObject.optString("api_url");
        String cdnUrl = jsonObject.optString("cdn_url");
        String cdnMd5 = jsonObject.optString("cdn_md5");
        IHNetDiagnoseHelp.IHNetDiagnoseConfig config = new IHNetDiagnoseHelp.IHNetDiagnoseConfig(
                AccountHelp.getInstance().getCurrentAccount().getShowUid(),apiUrl, cdnUrl, cdnMd5);
        IHNetDiagnoseHelp.INSTANCE.showNetDiagnose((Activity) getContext(), config);
    }

    private void hideUnityLoading() {
        AccountHelp.getInstance().getIhumanHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilsStrategy.this.getUnityView().hideUnitySplashPage();
            }
        }, 1000);
    }

    private void jumpToWechat() {
        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        getContext().startActivity(intent);
    }

    private void clipTextToSystemBoard(String content) {
        AccountHelp.getInstance().getIhumanHandler().post(new Runnable() {
            @Override
            public void run() {
                ClipboardUtil.clipToBoard(UtilsStrategy.this.getContext(), content);
            }
        });
    }

    private String isAppInstalled(int type) {
        return generateBooleanJson(AccountHelp.getInstance().isAppInstalledForChannelType(type + ""));
    }

    private String openAppSettingView() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getContext().getPackageName(), null));
        getContext().startActivity(intent);
        return "Start app setting view";
    }

    private String getAbTestGroupId() {
        return generateNumJson(Integer.parseInt(AccountHelp.getInstance().getGroupId()));

    }

    private void openMiniProgram(String programId, String path, int type, String scene) {
        try {
            path = URLEncoder.encode(path,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        AccountHelp.getInstance().openMiniProgram(programId, path, type, scene);
    }


    private String gogo_key_p1(String key,String p1){
        StringBuilder str = new StringBuilder(AccountHelp.getInstance().getDeviceId());
        str.append(key);
        if (!TextUtils.isEmpty(p1)){
            str.append(MD5Utils.INSTANCE.encode(p1));
        }
        if (TextUtils.isEmpty(str)){
            return "";
        }
        return MD5Utils.INSTANCE.encode(str.toString());
    }

    private String gogo_key_p1_p2(String key,String p1, String p2){
        StringBuilder str = new StringBuilder(gogo_key_p1(key, p1));
        if (!TextUtils.isEmpty(p2)){
            str.append(p2);
        }
        if (TextUtils.isEmpty(str)){
            return "";
        }
        return MD5Utils.INSTANCE.encode(str.toString());
    }


    private String  generateStringJson(String str){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "s");
            jsonObject.put("value", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }







}

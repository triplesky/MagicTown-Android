package com.bekids.gogotown.unity.bridge.strategy;

import android.text.TextUtils;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.google.gson.JsonSyntaxException;
import com.ihuman.sdk.lib.statistics.IHStatisticsComponent;
import com.ihuman.sdk.lib.utils.IHDataConvertUtils;
import com.tencent.bugly.crashreport.BuglyLog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: LuckyFind
 * Date: 2021/3/4
 * Desc:埋点
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_LOG_ACTION, MessageConstants.MESSAGE_LOG_MISSION
,MessageConstants.MESSAGE_BUGLY_REPORT_LOG})
public class LogEventStrategy extends BaseAbstractStrategy {
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        switch (method) {
            case MessageConstants.MESSAGE_LOG_ACTION:
                IHStatisticsComponent.getInstance().onEvent(jsonArray.getString(0), json2map(jsonArray.getString(1)));
                break;
            case MessageConstants.MESSAGE_LOG_MISSION:
                IHStatisticsComponent.getInstance().logMission(jsonArray.getString(0), IHDataConvertUtils.jsonToMap(jsonArray.getString(1)), null);
                break;
            case MessageConstants.MESSAGE_BUGLY_REPORT_LOG:
                BuglyLog.d("bugly_report_log",jsonArray.getString(0));
                break;

        }
        return "success";
    }

    private Map json2map(String jsonStr){
        Map resultMap = null;

        if (TextUtils.isEmpty(jsonStr)) {
            resultMap = new HashMap();
            return resultMap;
        }

        try {
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(jsonStr);
            return jsonObject;

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        if (resultMap == null) {
            resultMap = new HashMap();
        }

        return resultMap;
    }
}

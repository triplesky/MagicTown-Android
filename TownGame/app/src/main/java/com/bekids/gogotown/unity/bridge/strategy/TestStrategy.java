package com.bekids.gogotown.unity.bridge.strategy;

import static com.bekids.gogotown.unity.bridge.bean.MessageConstants.MESSAGE_BUGLY_REPORT_EXCEPTION;

import android.app.AlertDialog;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.bekids.gogotown.util.string.StringUtils;
import com.ihuman.library.accountsdk.AccountHelp;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Author: LuckyFind
 * Date: 2021/4/17
 * Desc:
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_SHOW_DEBUG_ALERT_VIEW,MESSAGE_BUGLY_REPORT_EXCEPTION,MessageConstants.MESSAGE_BUGLY_REPORT_ERROE})
public class TestStrategy extends BaseAbstractStrategy {
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        switch (method){
            case MessageConstants.MESSAGE_SHOW_DEBUG_ALERT_VIEW:
                showLuaErrorDialog(jsonArray.getString(0),jsonArray.getString(1));
                break;
            case MESSAGE_BUGLY_REPORT_EXCEPTION:
                //showLuaErrorDialog("LuaError",jsonArray.getString(0));
                uploadLuaErrorToBugly(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_BUGLY_REPORT_ERROE:
                uploadLuaErrorToBugly(jsonArray.getString(0));
                break;
        }
        return "";
    }

    private void uploadLuaErrorToBugly(String error) {
        CrashReport.postCatchedException(new Throwable(error));
    }

    private void showLuaErrorDialog(String title,String content){
//        if(MMKVUtils.getDefaultInstance().getInt("environment_type")==3){
//            return;
//        }

        AccountHelp.getInstance().getIhumanHandler().post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(StringUtils.isTrimEmpty(title)?"":title);
                builder.setMessage(StringUtils.isTrimEmpty(content)?"":content);
                builder.create().show();
            }
        });
    }
}

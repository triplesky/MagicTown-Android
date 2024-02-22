package com.bekids.gogotown.unity.bridge;

/**
 * 这个接口及其方法是给C#回调用的，所以不要轻易修改
 * */
public interface NativeCommunicatorForAndroidListener {
//调用c#的方法，走通知的方式
public void recive_native_call_application(String message,String params);

//调用c#的方法，走block的方式
public void recive_native_call_application_block(String blockID,String params);

//给下载用的，快捷方式。不会有json encode 和 decode操作
public void recive_report_download_task_report(String url, int status, int cbyte, int tbytes, int ecode, String errmsg);

//获取app那边的运行状态，给Bugly用的
public String recive_native_request_runing_state();

public String application_report_log();

}

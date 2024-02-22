package com.bekids.gogotown.unity.bridge.strategy;

import android.app.Activity;
import android.util.Log;

import com.bekids.gogotown.base.BaseApplication;
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.bekids.gogotown.unity.bridge.interf.IUnityView;
import com.ihuman.sdk.lib.download.IHDownloadComponent;
import com.ihuman.sdk.lib.download.callback.IHDownloadCallback;
import com.ihuman.sdk.lib.download.core.IHDownloadStatus;
import com.ihuman.sdk.lib.download.core.IHDownloadTask;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: LuckyFind
 * Date: 2021/2/3
 * Desc:下载模块
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_ADD_DOWNLOAD_TASK_FOR_URL, MessageConstants.MESSAGE_CLEAN_ALL_DOWNLOAD_TASKS,MessageConstants.MESSAGE_CLEAN_DOWNLOAD_TASK_FOR_URL})
public class DownloadStrategy extends BaseAbstractStrategy {
    Activity activity;
    private long totalBytes;

    @Override
    public void bindIUnityView(IUnityView iUnityView) {
        super.bindIUnityView(iUnityView);
        Log.i("lldebug download",getContext().getFilesDir()+ File.separator+"download");
        IHDownloadComponent.getInstance().initialize(BaseApplication.instance);
        IHDownloadComponent.getInstance().setDownloadPath(getContext().getFilesDir()+ File.separator+"download");
        IHDownloadComponent.getInstance().addDownloadListener((Activity) getContext(), false, new IHDownloadCallback() {
            @Override
            public void taskUpdated(IHDownloadTask task, String path) {
                if(task.getStatus()== IHDownloadStatus.IHDownloadStatusFinshed){
                    /**
                     * 因为这个task会在下载结束的时候将totalBytes和DownloadBytes置为0，
                     * 从而导致下载进度计算不正常，所以，在这里单独赋值
                     * */

//                    task.setTotalBytes(totalBytes);
//                    task.setDownloadBytes(totalBytes);
                    reportDownload(task.getUrl(),task.getStatus().value,(int)task.getDownloadBytes(),(int)task.getTotalBytes(),0,task.getDownloadPath());
                }else {
                    if(task.getTotalBytes()>0){
                        /**
                         * 下载sdk并没有链接成功，准备开始下载的状态码，在waiting阶段的时候，totalBytes会突然从0变为该资源的
                         * 大小，所以在这里只有获取到了资源总大小，才会去回调unity
                         * */

                        totalBytes = task.getTotalBytes();

                        reportDownload(task.getUrl(),task.getStatus().value,(int)task.getDownloadBytes(),(int)task.getTotalBytes(),0,"");
                    }
                }
            }

            @Override
            public void taskErrorOccurred(IHDownloadTask task, String respCode, String respMessage) {
                reportDownload(task.getUrl(),task.getStatus().value,(int)task.getDownloadBytes(),(int)task.getTotalBytes(), Integer.parseInt(respCode),respMessage);
                Map<String,String> map = new HashMap<String,String>();
                map.put("url",task.getUrl());
                map.put("status",task.getStatus().value+"");
                map.put("download_bytes", task.getDownloadBytes()+"");
                map.put("total_bytes",task.getTotalBytes()+"");
                map.put("error_code",respCode);
                map.put("error_message", respMessage);
                CrashReport.postCatchedException(new Throwable("资源下载失败",new Throwable(gson.toJson(map))));
            }
        });
    }

    private void reportDownload(String url, int status, int cbyte, int tbytes, int ecode, String errmsg){
        if(getUnityView().getUnityMainHandler()!=null){
            getUnityView().getUnityMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    getUnityView().reportDownloadTask(url, status, cbyte, tbytes, ecode, errmsg);
                }
            });
        }else {
            getUnityView().reportDownloadTask(url, status, cbyte, tbytes, ecode, errmsg);
        }
    }

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        switch (method){
            case MessageConstants.MESSAGE_ADD_DOWNLOAD_TASK_FOR_URL:
                addDownloadTask(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_CLEAN_ALL_DOWNLOAD_TASKS:
                cleanAllDownloadTask();
                break;
            case MessageConstants.MESSAGE_CLEAN_DOWNLOAD_TASK_FOR_URL:
                cleanDownloadTaskForUrl(jsonArray.getString(0));
                break;
        }
        return "process";
    }

    private void cleanDownloadTaskForUrl(String url) {
        try {
            IHDownloadComponent.getInstance().clearDownloadTaskForURL(url);
        } catch (Exception e) {
            Log.e("lldebug", e.getLocalizedMessage());
        }

        reportDownload(url, IHDownloadStatus.IHDownloadStatusCleared.value, 0,0,-1,"cancle");
    }

    private void addDownloadTask(String url){
        activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IHDownloadComponent.getInstance().addDownloadTaskForURL(url);
            }
        });

    }

    private void cleanAllDownloadTask(){
        IHDownloadComponent.getInstance().clearAllDownloadTasks();
    }


}

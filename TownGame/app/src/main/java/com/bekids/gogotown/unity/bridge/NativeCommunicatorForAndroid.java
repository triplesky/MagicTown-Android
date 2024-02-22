package com.bekids.gogotown.unity.bridge;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.bekids.gogotown.MainApplication;
import com.bekids.gogotown.unity.bridge.dispatcher.UnityMessageDispatcher;
import com.bekids.gogotown.unity.bridge.log.UnityBridgeLogPrinter;
import com.bekids.gogotown.util.file.AssetsFileUtil;


public class NativeCommunicatorForAndroid {
    //单例声明
    private static NativeCommunicatorForAndroid shared_instance = null;
    protected NativeCommunicatorForAndroidListener nativeCommunicaterForAndroidListener = null;
    protected UnityMessageDispatcher messageDispatcher = null;
    private Handler handler;
    private Looper looper;

    private NativeCommunicatorForAndroid() {

    }

    /**
     * 这个方法需要提供给Unity调用，所以不要随意修改
     * */
    public synchronized static NativeCommunicatorForAndroid shared_instance() {

        if (shared_instance == null) {
            synchronized (NativeCommunicatorForAndroid.class) {
                if (shared_instance == null) {
                    shared_instance = new NativeCommunicatorForAndroid();
                }
            }
        }
        return shared_instance;
    }



    /**
     * 这个方法需要提供给Unity调用，所以不要随意修改
     * */
    public String on_message(String message, String params, String blockID) {

        /**
         * UnityPlayer会启动一个叫UnityMain的线程去更新unity的相关UI，所以这个方法在被unity调用时，是执行在UnityMain线程的。
         * UnityMain线程在初始化的时候会启动一个looper，所以在此处，可以获取到这个线程的looper。在此创建一个handler用来切换线程
         * */
        if (looper == null) {
            looper = Looper.myLooper();
            handler = new Handler(looper);
        }

    if (messageDispatcher != null) {
            UnityBridgeLogPrinter.getInstance().printUnityRawRequestInfo(message, params, blockID);
            return messageDispatcher.dispatchMessage(message, params, blockID);
        } else {
            Log.d("MessageStrategy", "无法调用函数 call_application->message_distributer = nil,无法调用");
            return "";
        }
    }


    public byte[] get_assets_file_byte(String path) {
        if (AssetsFileUtil.fileExistsInAssets(MainApplication.application, path)) {
            return AssetsFileUtil.getFileByteFromAssets(MainApplication.application, path);
        }
        return null;
    }

    /**
     * 这个方法需要提供给Unity调用，所以不要随意修改
     * */
    public void register_listener(NativeCommunicatorForAndroidListener listener) {
        nativeCommunicaterForAndroidListener = listener;
    }

    /**
     * 注册Unity消息分发器
     * @param messageDispatcher
     */
    public void registerMessageDispatcher(UnityMessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    /**
     * 给unity回调下载进度
     * @param url
     * @param status
     * @param cbyte
     * @param tbytes
     * @param ecode
     * @param errmsg
     */
    public void reportDownloadTask(String url, int status, int cbyte, int tbytes, int ecode, String errmsg) {
        if (nativeCommunicaterForAndroidListener != null) {
            Log.d("DownloadTask", "**********reportDownloadTask**********\nurl:" + url + ",\nstatus:" + status + "\ncbyte:" + cbyte + "\ntbytes:" + tbytes + "\necode:" + ecode + "\nerrmsg:" + errmsg);
            nativeCommunicaterForAndroidListener.recive_report_download_task_report(url, status, cbyte, tbytes, ecode, errmsg);
        } else {
            Log.d("CallUnity", "无法调用函数 call_application->message_distributer = nil,无法调用");
        }
    }

    /**
     * 通过blockId回调Unity
     * @param blockID
     * @param params
     */
    public void call_application_with_block(String blockID, String params) {
        Log.d("BaseAbstractStrategy", "Thread222----:" + Thread.currentThread().getName());

        if (nativeCommunicaterForAndroidListener != null) {
            nativeCommunicaterForAndroidListener.recive_native_call_application_block(blockID, params);
        } else {
            Log.d("CallUnity", "无法调用函数 call_application->message_distributer = nil,无法调用");
        }
    }

    /**
     * 通过方法名回调Unity
     * @param message
     * @param params
     */
    public void call_application(String message, String params) {
        Log.d("BaseAbstractStrategy", "Thread222----:" + Thread.currentThread().getName());

        if (nativeCommunicaterForAndroidListener != null) {
            nativeCommunicaterForAndroidListener.recive_native_call_application(message, params);
        } else {
            Log.d("CallUnity", "无法调用函数 call_application->message_distributer = nil,无法调用");
        }
    }

    /**
     * 上报lua error到Bugly
     * @param error
     */
//    public void uploadLuaErrorToBugly(String error) {
//        CrashReport.postCatchedException(new Throwable(error));
//    }

    /**
     * 获取UnityMain所在线程的Handler
     * @return
     */
    public Handler getUnityMainThreadHandler() {
        return handler;
    }

    /**
     * 给unity发消息
     * @param method
     * @param params
     * @param blockId
     * @param originMethod
     */
    public void callUnity(String method, String params, String blockId, String originMethod){

        if (handler != null) {
            handler.post(() -> {
                if (TextUtils.isEmpty(blockId)) {
                    NativeCommunicatorForAndroid.shared_instance().call_application(method, params);
                } else {
                    NativeCommunicatorForAndroid.shared_instance().call_application_with_block(blockId, params);
                }
            });
        } else {
            if (TextUtils.isEmpty(blockId)) {
                NativeCommunicatorForAndroid.shared_instance().call_application(method, params);
            } else {
                NativeCommunicatorForAndroid.shared_instance().call_application_with_block(blockId, params);
            }
        }
    }
}

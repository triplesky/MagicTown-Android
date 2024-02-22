package com.bekids.gogotown.unity.bridge.interf;

import android.content.Context;
import android.os.Handler;

/**
 * Author: LuckyFind
 * Date: 2021/4/16
 * Desc:
 */
public interface IUnityView {
    /**
     * 获取UnityActivity的上下文
     *
     * */
    Context getUnityContext();

    /**
     * 展示原生的登录页
     * @param phone   手机号
     * @param url     登录地址
     * @param blockId  回调unity的blockid
     * @param fromPage 来源 0：unity页面，1：一键登录
     * */
    void showLoginFragment(String phone, String url, String blockId,String fromPage);

    /**
     * 隐藏原生登录页
     * */
    void hideLoginFragment();

    /**
     * 展示一个loading等待框
     * */
    void showUnityLoadingDialog();

    /**
     * 隐藏一个loading等待框
     * */
    void hideUnityLoadingDialog();

    /**
     * WebView关闭时的回调
     * */
    void onWebViewClosed();

    /**
     * 关闭unity splash页面
     * */
    void hideUnitySplashPage();

    /**
     * 获取UnityMain线程所在的handler
     * */
    Handler getUnityMainHandler();

    /**
     * 回调下载进度
     * */
    void reportDownloadTask(String url, int status, int cbyte, int tbytes, int ecode, String errmsg);
}

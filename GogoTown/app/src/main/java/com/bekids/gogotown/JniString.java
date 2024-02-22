package com.bekids.gogotown;

/**
 * **************************************
 * 项目名称: bekids Town
 *
 * @Author ll
 * 创建时间: 2023/1/9    5:15 下午
 * 用途
 * **************************************
 */
public class JniString {
    static {
        System.loadLibrary("gogokey");
    }

    public static native String stringForJni();
}

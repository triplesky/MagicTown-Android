package com.bekids.gogotown.unity.bridge.interf;

/**
 * Author: LuckyFind
 * Date: 2021/3/8
 * Desc:
 */
public interface IPickImageCompleteListener {
    void pickFailed();
    void pickSuccess(String path);
}

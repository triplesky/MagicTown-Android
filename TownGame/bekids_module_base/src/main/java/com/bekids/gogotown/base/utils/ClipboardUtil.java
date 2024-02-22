package com.bekids.gogotown.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * Author: LuckyFind
 * Date: 2021/4/20
 * Desc:剪切板辅助类
 */
public class ClipboardUtil {

    public static boolean clipToBoard(Context context, String text) {
        if(TextUtils.isEmpty(text)){
            return false;
        }
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        return true;
    }

}

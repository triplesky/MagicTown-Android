package com.bekids.gogotown.permission;


import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * **************************************
 * 项目名称:ParentCommunity
 *
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/11/25    2:49 下午
 * 用途
 * **************************************
 */
public final class IHPermission {

    public static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    public static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";

    public static final String CAMERA = "android.permission.CAMERA";

    public static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    public static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    public static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";

    public static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    public static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    public static final String RECORD_AUDIO = "android.permission.RECORD_AUDIO";

    public static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    public static final String CALL_PHONE = "android.permission.CALL_PHONE";
    public static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    public static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    public static final String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL";
    public static final String USE_SIP = "android.permission.USE_SIP";
    public static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";

    public static final String BODY_SENSORS = "android.permission.BODY_SENSORS";

    public static final String SEND_SMS = "android.permission.SEND_SMS";
    public static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    public static final String READ_SMS = "android.permission.READ_SMS";
    public static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    public static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";

    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final String BLUETOOTH = "android.permission.BLUETOOTH";

    public static final class Group {
        public static final String[] CALENDAR = new String[]{
                IHPermission.READ_CALENDAR,
                IHPermission.WRITE_CALENDAR};

        public static final String[] CAMERA = new String[]{IHPermission.CAMERA};

        public static final String[] CONTACTS = new String[]{
                IHPermission.READ_CONTACTS,
                IHPermission.WRITE_CONTACTS,
                IHPermission.GET_ACCOUNTS};

        public static final String[] LOCATION = new String[]{
                IHPermission.ACCESS_FINE_LOCATION,
                IHPermission.ACCESS_COARSE_LOCATION};

        public static final String[] MICROPHONE = new String[]{IHPermission.RECORD_AUDIO};

        public static final String[] PHONE = new String[]{
                IHPermission.READ_PHONE_STATE,
                IHPermission.CALL_PHONE,
                IHPermission.READ_CALL_LOG,
                IHPermission.WRITE_CALL_LOG,
                IHPermission.ADD_VOICEMAIL,
                IHPermission.USE_SIP,
                IHPermission.PROCESS_OUTGOING_CALLS};

        public static final String[] SENSORS = new String[]{IHPermission.BODY_SENSORS};

        public static final String[] SMS = new String[]{
                IHPermission.SEND_SMS,
                IHPermission.RECEIVE_SMS,
                IHPermission.READ_SMS,
                IHPermission.RECEIVE_WAP_PUSH,
                IHPermission.RECEIVE_MMS};

        public static final String[] STORAGE = new String[]{
                IHPermission.READ_EXTERNAL_STORAGE,
                IHPermission.WRITE_EXTERNAL_STORAGE};
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String... permissions) {
        return transformText(context, Arrays.asList(permissions));
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, String[]... groups) {
        List<String> permissionList = new ArrayList<>();
        for (String[] group : groups) {
            permissionList.addAll(Arrays.asList(group));
        }
        return transformText(context, permissionList);
    }

    /**
     * Turn permissions into text.
     */
    public static List<String> transformText(Context context, List<String> permissions) {
        List<String> textList = new ArrayList<>();

        return textList;
    }
}

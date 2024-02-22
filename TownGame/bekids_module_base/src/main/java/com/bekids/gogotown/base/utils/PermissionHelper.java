package com.bekids.gogotown.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.bekids.gogotown.base.utils.MMKVImpl;import java.util.List;

public class PermissionHelper {
    public static final int PERMISSION_GRANTED = 3;
    public static final int PERMISSION_DENIED = 2;
    public static final int PERMISSION_NOT_DETERMINED= 0;
    private static final int REQUEST_PERMISSION_CODE = 10;

    private Context mContext;

    private PermissionListener mListener;

    private List<String> mPermissionList;

    public PermissionHelper(@NonNull Context context) {
        checkCallingObjectSuitability(context);
        this.mContext = context;

    }



    /**
     * 判断是否具有某权限
     *
     * @param context
     * @param perms
     * @return
     */
    public static boolean hasPermissions(@NonNull Context context, @NonNull String... perms) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    public static boolean isFirstRequestPermission(String perms ){
        return !MMKVImpl.INSTANCE.getBoolean(perms, false);
    }

    public static int judgeCurrentPermissionsStatus(Context context,String... perms) {
        if (hasPermissions(context, perms)) {
            return PERMISSION_GRANTED;
        } else {
            for (String s : perms) {
                if (!PermissionHelper.isFirstRequestPermission(s)) {//代表权限组不是第一次申请
                    if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, s)) {//用户点击了不再询问
                        return PERMISSION_DENIED;
                    }
                }
            }
            return PERMISSION_NOT_DETERMINED;
        }
    }

    /**
     * 判断是否具有某权限
     *
     * @param context
     * @param perms
     * @return
     */
    public static int getPermissionStatus(@NonNull Context context, @NonNull String... perms) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PackageManager.PERMISSION_GRANTED;
        }

        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm) !=
                    PackageManager.PERMISSION_GRANTED) {
                return ContextCompat.checkSelfPermission(context, perm);
            }
        }

        return PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 兼容fragment
     *
     * @param object
     * @param perm
     * @return
     */
    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(@NonNull Object object, @NonNull String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    /**
     * 执行申请,兼容fragment
     *
     * @param object
     * @param perms
     * @param requestCode
     */
    @TargetApi(23)
    private static void executePermissionsRequest(@NonNull Object object, @NonNull String[] perms, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof androidx.fragment.app.Fragment) {
            ((androidx.fragment.app.Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    /**
     * 检查传递Context是否合法
     *
     * @param object
     */
    private static void checkCallingObjectSuitability(@Nullable Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof Fragment;
        if (!(isSupportFragment || isActivity || (isAppFragment && isNeedRequest()))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }

    public static boolean isNeedRequest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void showMessageOKCancel(CharSequence message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    public interface PermissionListener {

        void doAfterGrand(String... permission);

        void doAfterDenied(String... permission);
    }

}

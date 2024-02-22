package com.bekids.gogotown.permission;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.ihuman.sdk.lib.permission.rxpermission.Permission;
import com.ihuman.sdk.lib.permission.rxpermission.RxPermissions;

import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * **************************************
 * 项目名称:ParentCommunity
 *
 * @Author ll
 * 邮箱：lulong@ihuman.com
 * 创建时间: 2021/11/25    2:34 下午
 * 用途
 * **************************************
 */
public class PermissionsUtils {
    public static IHSetting.Action aAction = null;

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionsConstant.REQUEST_SETTING_PAGE) {
            if (aAction != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (aAction != null) {
                            aAction.onAction();
                            aAction = null;
                        }
                    }
                }, 100);
            }
        }
    }


    public static void requestPermission(FragmentActivity tFragmentActivity,
                                         Action<List<String>> grantedAction,
                                         String... permissions) {
        requestPermission(tFragmentActivity, grantedAction, null, null, null, permissions);
    }


    public static void requestPermission(FragmentActivity tFragmentActivity,
                                         Action<List<String>> grantedAction,
                                         Action<List<String>> deniedAction,
                                         IHSetting.Action comebackAction,
                                         String... permissions) {
        requestPermission(tFragmentActivity, grantedAction, deniedAction, comebackAction, null, permissions);
    }


    public static void requestPermission(FragmentActivity tFragmentActivity,
                                         Action<List<String>> grantedAction,
                                         final Action<List<String>> deniedAction,
                                         final IHSetting.Action comebackAction,
                                         final View.OnClickListener cancelListener,
                                         String... permissions) {

        if (tFragmentActivity == null) {
            return;
        }
        tFragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RxPermissions rxPermissions = new RxPermissions(tFragmentActivity);
                rxPermissions
                        .requestEachCombined(permissions)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    // `permission.name` is granted !
                                    grantedAction.onAction(null);
                                } else {

                                    if (deniedAction != null) {
                                        deniedAction.onAction(null);
                                    }

                                    if (permission.shouldShowRequestPermissionRationale) {
//                            tempActivity.showSettingDialog(tempActivity, null, null, Arrays.asList(Permission.WRITE_EXTERNAL_STORAGE));
                                    } else {
                                        // Denied permission with ask never again
                                        // Need to go to the settings
                                        showSettingDialog(tFragmentActivity, comebackAction, null, cancelListener, Arrays.asList(permissions), null);
                                    }
                                }
                            }
                        });
            }
        });
    }

    /**
     * Display setting dialog.
     */
    private static AlertDialog permissionDialog;

    public static boolean isPermissionDialogShow() {
        if (permissionDialog != null) {
            return permissionDialog.isShowing();
        }
        return false;
    }


    public static void dismissSettingDialog() {
        if (permissionDialog != null&&permissionDialog.isShowing()) {
            try {
                permissionDialog.dismiss();
            } catch (IllegalArgumentException e) {
            }
            permissionDialog = null;
        }
    }


    public static void showSettingDialog(Context context,
                                         final IHSetting.Action comebackAction,
                                         final List<String> permissions) {

        showSettingDialog(context, comebackAction, null, null, permissions, null);
    }

    public static void showSettingDialog(Context context,
                                         View.OnClickListener confirmListener,
                                         final List<String> permissions,
                                         String rationale) {

        showSettingDialog(context, null, confirmListener, null, permissions, rationale);
    }

    public static void showSettingDialog(Context context,
                                         final IHSetting.Action comebackAction,
                                         View.OnClickListener dialogConfirmListener,
                                         View.OnClickListener cancelListener,
                                         final List<String> permissions,
                                         String rationale) {






    }

    /**
     * 对话框点击事件
     *
     * @param tContext
     * @param comebackAction
     * @param dialogConfirmListener
     * @param cancelListener
     * @param tViewOK
     * @param tVieCancel
     */
    private static void viewClickFun(Context tContext, IHSetting.Action comebackAction,
                                     View.OnClickListener dialogConfirmListener,
                                     View.OnClickListener cancelListener,
                                     View tViewOK, View tVieCancel) {

        tViewOK.setOnClickListener(view -> {
            if (permissionDialog != null) {
                try {
                    permissionDialog.dismiss();
                } catch (IllegalArgumentException e) {
                }
                permissionDialog = null;
            }
            if (dialogConfirmListener != null) {
                dialogConfirmListener.onClick(null);
            } else {
                PermissionsUtils.setPermission(tContext, comebackAction);
            }
        });
        tVieCancel.setOnClickListener(view -> {
            if (permissionDialog != null) {
                try {
                    permissionDialog.dismiss();
                } catch (IllegalArgumentException e) {
                }
                permissionDialog = null;
            }
            if (cancelListener != null) {
                cancelListener.onClick(view);
            }
        });
    }

    public static void setPermission(Context tContext, IHSetting.Action action) {
        if (tContext == null) {
            return;
        }
        RuntimeSettingPage setting = new RuntimeSettingPage(new ContextSource(tContext));
        setting.start(PermissionsConstant.REQUEST_SETTING_PAGE);
        aAction = action;
    }


    public static void checkStoragePermission(Context context,
                                              Consumer<Boolean> tConsumer) {

    }


    public static void checkCallPhonePermission(Context context,
                                              Consumer<Boolean> tConsumer) {
    }
}

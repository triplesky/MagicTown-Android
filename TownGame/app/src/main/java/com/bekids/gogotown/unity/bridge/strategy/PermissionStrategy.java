package com.bekids.gogotown.unity.bridge.strategy;

import static com.bekids.gogotown.base.utils.PermissionHelper.PERMISSION_GRANTED;

import android.Manifest;
import android.os.Build;

import androidx.fragment.app.FragmentActivity;

import com.bekids.gogotown.base.utils.MMKVImpl;
import com.bekids.gogotown.base.utils.PermissionHelper;
import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.ihuman.library.accountsdk.AccountHelp;
import com.ihuman.sdk.lib.permission.rxpermission.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.functions.Consumer;

/**
 * Author: LuckyFind
 * Date: 2021/3/9
 * Desc: app权限请求相关
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_REQUEST_AUTHORIZATION_STATUS_FOR_AUDIO , MessageConstants.MESSAGE_REQUEST_AUTHORIZATION_STATUS_FOR_CAMERA ,
        MessageConstants.MESSAGE_AUTHORIZATION_STATUS_FOR_CAMERA ,MessageConstants.MESSAGE_AUTHORIZATION_STATUS_FOR_AUDIO,MessageConstants.MESSAGE_AUTHORIZATION_STATUS_FOR_PHOTO,MessageConstants.MESSAGE_REQUEST_AUTHORIZATION_STATUS_FOR_PHOTO})
public class PermissionStrategy extends BaseAbstractStrategy {

    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        switch (method) {
            case MessageConstants.MESSAGE_AUTHORIZATION_STATUS_FOR_CAMERA:
                result = getAuthStatusForCamera();
                break;
            case MessageConstants.MESSAGE_AUTHORIZATION_STATUS_FOR_PHOTO:
                result = checkAlbumPermission();
                break;
            case MessageConstants.MESSAGE_REQUEST_AUTHORIZATION_STATUS_FOR_CAMERA:
            case MessageConstants.MESSAGE_REQUEST_AUTHORIZATION_STATUS_FOR_PHOTO:
                requestPermission(method, blockId, Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            case MessageConstants.MESSAGE_AUTHORIZATION_STATUS_FOR_AUDIO:
                result = getAuthStatusForAudio();
                break;
            case MessageConstants.MESSAGE_REQUEST_AUTHORIZATION_STATUS_FOR_AUDIO:
                requestPermission(method, blockId, Manifest.permission.RECORD_AUDIO);
                break;
        }
        return result;
    }

    private void requestPermission(String method, String blockId, String... perms) {
        AccountHelp.getInstance().getIhumanHandler().post(new Runnable() {
            @Override
            public void run() {
                RxPermissions rxPermissions = new RxPermissions((FragmentActivity) getContext());
                rxPermissions.request(perms)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                responsePermissionRequestResult(method, blockId, perms);
                            }
                        });
            }
        });
    }

    private void responsePermissionRequestResult(String method, String blockId,String... perms){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", PermissionHelper.judgeCurrentPermissionsStatus(getContext(),perms));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(String s:perms){
            MMKVImpl.INSTANCE.saveValue(s, true);
        }
        callUnity(method, generateNormalJson(jsonObject), blockId, method);
    }

    private String checkAlbumPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return generateNumJson(PERMISSION_GRANTED);
        } else {
            return generateNumJson(PermissionHelper.judgeCurrentPermissionsStatus(getContext(),Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE));
        }
    }


    private String getAuthStatusForCamera() {

        return generateNumJson(PermissionHelper.judgeCurrentPermissionsStatus(getContext(),Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }


    private String getAuthStatusForAudio() {

        return generateNumJson(PermissionHelper.judgeCurrentPermissionsStatus(getContext(),Manifest.permission.RECORD_AUDIO));
    }

}

package com.bekids.gogotown.unity.bridge.strategy;


import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.bekids.gogotown.util.file.AssetsFileUtil;
import com.bekids.gogotown.util.file.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Author: LuckyFind
 * Date: 2021/4/28
 * Desc: android assets目录文件操作类
 * 因为unity运行在unityMain thread线程中，不会阻塞主线程，所以这里的文件操作可以直接操作，不用开线程
 */
@RegisterUnityMethod({MessageConstants.MESSAGE_READ_TEXT_FROM_ANDROID_ASSETS,
        MessageConstants.MESSAGE_COPY_FILE_FROM_ANDROID_ASSETS_TO,
        MessageConstants.MESSAGE_COPY_FOLDER_FROM_ANDROID_ASSETS_TO,
        MessageConstants.MESSAGE_IS_HAVE_FILE_IN_ANDROID_ASSETS, MessageConstants.MESSAGE_READ_ANDROID_ASSETS_FILE_BYTE
        ,MessageConstants.MESSAGE_FILE_IN_ANDROID_ASSETS_MD5})
public class AssetsFileOperateStrategy extends BaseAbstractStrategy {
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result="";
        switch (method){
            case MessageConstants.MESSAGE_READ_TEXT_FROM_ANDROID_ASSETS:
                result = readTextFromAssets(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_COPY_FILE_FROM_ANDROID_ASSETS_TO:
            case MessageConstants.MESSAGE_COPY_FOLDER_FROM_ANDROID_ASSETS_TO:
                result = copyFileFromAssets(jsonArray.getString(0),jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_IS_HAVE_FILE_IN_ANDROID_ASSETS:
                result = fileExistsInAssets(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_READ_ANDROID_ASSETS_FILE_BYTE:
                result = getByteFromAssets(jsonArray.getString(0));
                break;

            case MessageConstants.MESSAGE_FILE_IN_ANDROID_ASSETS_MD5:
                String fileName = jsonArray.getString(0);
                if (fileName.startsWith("/")) {
                    fileName = fileName.substring(1);
                }
                if (AssetsFileUtil.fileExistsInAssets(getContext(), fileName)) {
                    String unityPath = getContext().getExternalFilesDir(null).getAbsolutePath();
                    if (AssetsFileUtil.copyFilesFromAssets(getContext(), fileName, unityPath)){
                        File file = new File(unityPath + File.separator
                                + fileName);
                        if (file.exists()) {
                            result = generateStringJson(FileUtils.getFileMD5ToString(file)).toLowerCase();
                            FileUtils.delete(file);
                        }else{
                            result = generateStringJson("");
                        }
                    } else{
                        result = generateStringJson("");
                    }
                } else{
                    result = generateStringJson("");
                }

                break;
        }
        return result;
    }

    private String fileExistsInAssets(String fileName) {
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        return generateBooleanJson(AssetsFileUtil.fileExistsInAssets(getContext(),fileName));
    }

    private String copyFileFromAssets(String srcFileName, String targetDirName) {
        return generateBooleanJson(AssetsFileUtil.copyFilesFromAssets(getContext(),srcFileName,targetDirName));

    }

    private String readTextFromAssets(String fileName) {
        return generateStringJson(AssetsFileUtil.readTextFromAssets(getContext(),fileName));
    }


    private String getByteFromAssets(String fileName){
        return generateByteJson(AssetsFileUtil.getByteFromAssets(getContext(), fileName));
    }

    private String  generateByteJson(byte[] str){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "byte");
            jsonObject.put("value", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private String  generateStringJson(String str){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "s");
            jsonObject.put("value", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}

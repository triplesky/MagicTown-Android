package com.bekids.gogotown.unity.bridge.strategy;

import android.text.TextUtils;

import com.bekids.gogotown.unity.bridge.annotation.RegisterUnityMethod;
import com.bekids.gogotown.unity.bridge.bean.MessageConstants;
import com.bekids.gogotown.unity.bridge.dispatcher.BaseAbstractStrategy;
import com.bekids.gogotown.util.file.IHFileUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

/**
 * 处理unity发送的文件处理类message
 * File操作
 * 因为unity运行在unityMain thread线程中，不会阻塞主线程，所以这里的文件操作可以直接操作，不用开线程
 */
@RegisterUnityMethod({
MessageConstants.MESSAGE_IS_FILE_EXIST, MessageConstants.MESSAGE_RENAME_FILE,
MessageConstants.MESSAGE_READ_TEXT_FROM_FILE,MessageConstants.MESSAGE_GET_FILE_MD5,
MessageConstants.MESSAGE_MOVE_FILE_TO,MessageConstants.MESSAGE_DELETE_FILE_AT_PATH,
MessageConstants.MESSAGE_IS_FOLDER_EXIST,MessageConstants.MESSAGE_RENAME_FOLDER,
MessageConstants.MESSAGE_MOVE_FOLDER,MessageConstants.MESSAGE_CREATE_FOLDER,
MessageConstants.MESSAGE_DELETE_FOLDER,MessageConstants.MESSAGE_MOVE_ALL_FILES_TO_FOLDER,
MessageConstants.MESSAGE_GET_FOLDER_SIZE,MessageConstants.MESSAGE_COPY_FILE_TO,
MessageConstants.MESSAGE_GET_FILE_SIZE})
public class FileStrategy extends BaseAbstractStrategy {
    private static final String TAG = "FileStrategy";
    @Override
    protected String process(String method, JSONArray jsonArray, String blockId) throws JSONException, IndexOutOfBoundsException {
        String result = "";
        if (jsonArray.length() <= 0) {
            return result;
        }
        switch (method) {
            case MessageConstants.MESSAGE_IS_FILE_EXIST:
                result = isFileExist(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_RENAME_FILE:
                result = renameFile(jsonArray.getString(0), jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_READ_TEXT_FROM_FILE:
                String filename = jsonArray.getString(0);
                result = readTextFromFile(filename);
                break;
            case MessageConstants.MESSAGE_GET_FILE_MD5:
                result = getFileMd5(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_MOVE_FILE_TO:
                result = moveFileTo(jsonArray.getString(0), jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_DELETE_FILE_AT_PATH:
                result = deleteFileAtPath(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_IS_FOLDER_EXIST:
                result = isFolderExist(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_RENAME_FOLDER:
                result = renameFolder(jsonArray.getString(0), jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_MOVE_FOLDER:
                result = moveFolderTo(jsonArray.getString(0), jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_CREATE_FOLDER:
                result = createFolder(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_DELETE_FOLDER:
                result = deleteFolder(jsonArray.getString(0));
                break;
            case MessageConstants.MESSAGE_MOVE_ALL_FILES_TO_FOLDER:
                result = moveAllFilesToFolder(jsonArray.getString(0), jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_GET_FOLDER_SIZE:
                result = getFolderSize(jsonArray);
                break;
            case MessageConstants.MESSAGE_COPY_FILE_TO:
                result = copyFileTo(jsonArray.getString(0),jsonArray.getString(1));
                break;
            case MessageConstants.MESSAGE_GET_FILE_SIZE:
                result = getFileSize(jsonArray.getString(0));
                break;
            default:
                break;
        }
        return result;
    }

    private String getFileSize(String path) {
        return generateNumJson(IHFileUtil.getFileSize(path));
    }

    private String copyFileTo(String src, String dest) {
        boolean result ;
        if (checkNull(src, dest)) {
            result = false;
        } else {
            File srcFile = new File(src);
            File destFile = new File(dest);
            result = IHFileUtil.copyOrMoveFile(srcFile, destFile, false);
        }
        return  generateBooleanJson(result);
    }

    private String getFolderSize(JSONArray jsonArray) throws JSONException {
        long size = 0 ;
        JSONArray elements = jsonArray.getJSONArray(0);
        if(elements.length() > 0){
            for(int i=0;i<elements.length();i++){
                size+= IHFileUtil.getFolderSize(new File(elements.getString(i)));
            }
        }
        return generateNumJson(size);
    }

    private String moveAllFilesToFolder(String src, String dest) {
        boolean result ;
        if (checkNull(src, dest)) {
            result = false;
        } else {
        File srcFile = new File(src);
        File destFile = new File(dest);
            result = IHFileUtil.copyOrMoveAllFilesToDir(srcFile, destFile, true);
        }
        return  generateBooleanJson(result);
    }

    private String deleteFolder(String string) {
        boolean res;
        if (!checkParams(string)) {
            res = false;
        } else {
            File file = new File(string);
            res = IHFileUtil.deleteDir(file);
        }
        return  generateBooleanJson(res);
    }


    private String createFolder(String string) {
        return  generateBooleanJson(IHFileUtil.createFolder(string));
    }

    private String moveFolderTo(String src, String dest) {
        boolean res;
        File srcFile = new File(src);
        File destFile = new File(dest);
        if (checkNull(src, dest)) {
            res = false;
        } else {
            res = IHFileUtil.copyOrMoveDir(srcFile, destFile, true);
        }
        return  generateBooleanJson(res);
    }

    private String renameFolder(String oldPath, String newPath) {
        return  generateBooleanJson(IHFileUtil.renameFolder(oldPath, newPath));
    }

    private String isFolderExist(String dirPath) {
        return  generateBooleanJson(IHFileUtil.folderExist(dirPath));
    }

    private String deleteFileAtPath(String path) {
        boolean result;
        if (!checkParams(path)) {
            result = false;
        } else {
            result= IHFileUtil.deleteFile(new File(path));
        }
        return  generateBooleanJson(result);
    }

    private String moveFileTo(String src, String dest) {
        boolean result;
        File srcFile = new File(src);
        File destFile = new File(dest);

        if (checkNull(src, dest)) {
            result = false;
        } else {
            result = IHFileUtil.copyOrMoveFile(srcFile, destFile, true);
        }
        return  generateBooleanJson(result);
    }

    private String isFileExist(String fileName) {
        return  generateBooleanJson(IHFileUtil.fileExist(fileName));
    }

    private String renameFile(String oldPath, String newPath) {
        return  generateBooleanJson(IHFileUtil.renameFile(oldPath, newPath));
    }

    private String readTextFromFile(String filePath) {
        return IHFileUtil.readTextFromFile(filePath);
    }

    private String getFileMd5(String filePath) {
        return IHFileUtil.getFileMd5(filePath);
    }


    private boolean checkParams(String... params) {
        if (params == null) {
            return false;
        }
        for (String s : params) {
            if (TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNull(String... params) {
        if (!checkParams(params)) {
            return true;
        }
        for (String s : params) {
            if (!(new File(s)).exists()) {
                return false;
            }
        }
        return false;
    }

}

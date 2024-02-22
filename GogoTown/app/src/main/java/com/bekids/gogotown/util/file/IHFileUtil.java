package com.bekids.gogotown.util.file;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Author: LuckyFind
 * Date: 2021/1/29
 * Desc:
 */
public class IHFileUtil {

    private static int sBufferSize = 1024;

    private static boolean checkParamsNull(String... params) {
        if (params == null) {
            return true;
        }
        for (String s : params) {
            if (TextUtils.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    public static long getFolderSize(File file) {
        if (file == null || !file.isDirectory()) {
            return 0;
        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) {
                return 0;
            }
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static long getFileSize(String path) {
        long size = 0;
        if (TextUtils.isEmpty(path)) {
            return size;
        }
        File f = new File(path);
        if (f.exists() && f.isFile()) {
            size = f.length();
        }
        return size;
    }

    public static String copyFilesFromAssets(Context context, String assetsDirName, String sdCardPath) {

        if (sdCardPath == null || sdCardPath.length() == 0) {
            return "";
        }
        try {
            String[] list = context.getAssets().list(assetsDirName);
            if (list.length == 0) {
                InputStream inputStream = context.getAssets().open(assetsDirName);
                if (assetsDirName.lastIndexOf('/') != -1) {//根目录文件
                    assetsDirName = assetsDirName.substring(assetsDirName.lastIndexOf('/'));
                }
                File file = new File(sdCardPath + File.separator
                        + assetsDirName);
                if (file.exists()) {
                    if (file.delete()) {
                        writeFileFromIS(file, inputStream);
                    }
                } else {
                    if (file.createNewFile()) {
                        writeFileFromIS(file, inputStream);
                    }
                }
            } else {
                String subDirName = assetsDirName;
                if (assetsDirName.contains("/")) {
                    subDirName = assetsDirName.substring(assetsDirName.lastIndexOf('/') + 1);
                }
                sdCardPath = sdCardPath + File.separator + subDirName;
                File file = new File(sdCardPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                for (String s : list) {
                    copyFilesFromAssets(context, assetsDirName + File.separator + s, sdCardPath);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean writeFileFromIS(final File file,
                                          final InputStream is) {
        if (is == null || !createOrExistsDir(file.getParentFile())) {
            Log.e("FileIOUtils", "create file <" + file + "> failed.");
            return false;
        }
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, false), sBufferSize);
            byte[] data = new byte[sBufferSize];
            for (int len; (len = is.read(data)) != -1; ) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }


    public static boolean copyOrMoveAllFilesToDir(final File srcDir,
                                                  final File destDir,
                                                  final boolean isMove) {
        if (srcDir == null || destDir == null) return false;
        // destDir's path locate in srcDir's path then return false
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) return false;
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        if (!createOrExistsDir(destDir)) return false;
        File[] files = srcDir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                File oneDestFile = new File(destPath, file.getName());
                if (file.isFile()) {
                    if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
                } else if (file.isDirectory()) {
                    if (!copyOrMoveAllFilesToDir(file, oneDestFile, isMove)) return false;
                }
            }
        }
        return !isMove || deleteDir(srcDir);
    }

    public static boolean copyOrMoveDir(final File srcDir,
                                        final File destDir,
                                        final boolean isMove) {
        if (srcDir == null || destDir == null) return false;
        // destDir's path locate in srcDir's path then return false
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) return false;
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        if (!createOrExistsDir(destDir)) return false;
        File[] files = srcDir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                File oneDestFile = new File(destPath + file.getName());
                if (file.isFile()) {
                    if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
                } else if (file.isDirectory()) {
                    if (!copyOrMoveAllFilesToDir(file, oneDestFile, isMove)) return false;
                }
            }
        }
        return !isMove || deleteDir(srcDir);
    }

    public static boolean deleteDir(final File dir) {
        if (dir == null) return false;
        // dir doesn't exist then return true
        if (!dir.exists()) return true;
        // dir isn't a directory then return false
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean copyOrMoveFile(final File srcFile,
                                         final File destFile,
                                         final boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        // srcFile equals destFile then return false
        if (srcFile.equals(destFile)) return false;
        // srcFile doesn't exist or isn't a file then return false
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        if (destFile.exists()) {
            if (!destFile.delete()) {// unsuccessfully delete then return false
                return false;
            }
        }
        if (!createOrExistsDir(destFile.getParentFile())) return false;
        try {
            return writeFileFromIS(destFile, new FileInputStream(srcFile))
                    && !(isMove && !IHFileUtil.deleteFile(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static String getFileMd5(String filePath) {
        String result = "";
        File file = new File(filePath);
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        if (!file.isFile()) {
            return "";
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        result = bytesToHexString(digest.digest());
        return result;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String readTextFromFile(String filePath) {
        String str = "";
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return "";
        }
        try {
            FileInputStream in = new FileInputStream(file);
            int size = in.available();

            byte[] buffer = new byte[size];

            in.read(buffer);

            in.close();
            str = new String(buffer, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static boolean renameFile(String oldPath, String newPath) {
        if (checkParamsNull(oldPath, newPath)) {
            return false;
        }
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return oldFile.exists() && oldFile.isFile() && oldFile.renameTo(newFile);
    }


    public static boolean fileExist(String path) {
        if (checkParamsNull(path)) {
            return false;
        }
        File file = new File(path);
        return file != null && (file.exists() && file.isFile());
    }

    public static boolean folderExist(String path) {
        if (checkParamsNull(path)) {
            return false;
        }
        File file = new File(path);
        return file != null && (file.exists() && file.isDirectory());
    }

    public static boolean renameFolder(String oldPath, String newPath) {
        if (checkParamsNull(oldPath, newPath)) {
            return false;
        }
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        return oldFile.exists() && oldFile.isDirectory() && oldFile.renameTo(newFile);
    }

    public static boolean createFolder(String src) {
        if (checkParamsNull(src)) {
            return false;
        }
        File file = new File(src);
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    public static byte[] fileToByteArray(File file) {
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytesArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytesArray;
    }
}

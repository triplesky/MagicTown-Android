package com.bekids.gogotown.util.file;

import android.content.Context;
import android.util.Log;


import com.bekids.gogotown.util.string.StringUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Author: LuckyFind
 * Date: 2021/4/28
 * Desc:
 */
public class AssetsFileUtil {
    private static int sBufferSize = 1024;


    public static boolean fileExistsInAssets(Context context, String fileName) {

        InputStream inputStream = null;
        try {
            String[] list = context.getAssets().list(fileName);
            if (list.length == 0) {//可能是文件
                inputStream = context.getAssets().open(fileName);
                if (inputStream != null) {
                    return true;
                }
            } else {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }


    public static boolean fileExistsInAssets2(Context context, String fileName) {
        try {

            if (context.getAssets().list(fileName).length > 0) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
            if (inputStream != null) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String readTextFromAssets(Context context, String fileName) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            if (inputStream == null) {
                return "";
            }
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static boolean copyFilesFromAssets(Context context, String assetsDirName, String sdCardPath) {
        Log.i("lldebug path", assetsDirName + " " + sdCardPath);
        if (StringUtils.isTrimEmpty(assetsDirName, sdCardPath)) {
            return false;
        }

        try {
            String[] list = context.getAssets().list(assetsDirName);
            if (list.length == 0) {//可能是文件
                InputStream inputStream = context.getAssets().open(assetsDirName);
                if (inputStream == null) {//文件不存在
                    return false;
                }
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
                return true;
            } else {//是文件夹
                String subDirName = assetsDirName;
                if (assetsDirName.contains("/")) {
                    subDirName = assetsDirName.substring(assetsDirName.lastIndexOf('/') + 1);
                }
                sdCardPath = sdCardPath + File.separator + subDirName;
                File file = new File(sdCardPath);
                if (!file.exists() || !file.isDirectory()) {
                    if (file.mkdirs()) {
                        for (String s : list) {
                            copyFilesFromAssets(context, assetsDirName + File.separator + s, sdCardPath);
                        }
                    } else {
                        return false;
                    }
                } else {
                    for (String s : list) {
                        copyFilesFromAssets(context, assetsDirName + File.separator + s, sdCardPath);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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


    public static byte[] getByteFromAssets(final Context context, String fileName) {
        byte [] buffer = null;
        try  {
            InputStream in = context.getAssets().open(fileName);
            //  获取文件的字节数
            int  lenght = in.available();
            //  创建byte数组
            buffer = new byte [lenght];
            //  将文件中的数据读到byte数组中
            in.read(buffer);
            in.close();
        }  catch  (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] getFileByteFromAssets(final Context context, String fileName) {
        byte [] buffer = null;
        try  {
            InputStream in = context.getAssets().open(fileName);
            //  获取文件的字节数
            int  lenght = in.available();
            //  创建byte数组
            buffer = new byte [lenght];
            //  将文件中的数据读到byte数组中
            in.read(buffer);
            in.close();
        }  catch  (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }
}

package com.bekids.gogotown.unity.bridge.annotation;

import android.content.Context;

import com.bekids.gogotown.unity.bridge.interf.IMessageHandleStrategy;
import com.bekids.gogotown.util.string.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Author: LuckyFind
 * Date: 2021/1/29
 * Desc:
 */
public class AnnotationScannerUtils {

    /**
     * 在 "com.bekids.gogotown.unity.bridge.strategy"这个包下扫描所有的策略类
     * @param context
     * @return
     */
    public static List<String> scanClass(Context context) {
        // 第一个class类的集合
        List<String> classes = new ArrayList<>();
        // 获取包的名字 并进行替换
        String packageDirName = "com.bekids.gogotown.unity.bridge.strategy";
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<String> dirs;
        try {
            String packageCodePath = context.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            String regExp = "^" + packageDirName + ".\\w+$";
            dirs = df.entries();
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                String url = dirs.nextElement();
                // 如果是以文件的形式保存
                if (url.matches(regExp)) {
                    classes.add(url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static void findClassesInPackageByFile(final boolean recursive, List<Class<?>> classes) {
        String packageName = "com.bekids.gogotown.unity.bridge.strategy";
        // 获取此包的目录 建立一个File
        File dir = new File(packageName);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            String className = file.getName().substring(0, file.getName().length() - 6);
            try {
                classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据RegisterUnityMethod注解获取所有的策略类
     * 只有被打上 {@link RegisterUnityMethod}注解的类才会被认为是一个策略类
     * 如果注解的值的长度不大于0，说明是一个空的策略类，这样不会实例化该类
     * 在这里将策略类的{@link RegisterUnityMethod}注解的值转化为“/”连接的字符串，
     * 通过反射创建该类的实例后，将类实例存入HashMap中，key即为上面生成的字符串
     * @param context
     * @return
     */
    public static HashMap<String, IMessageHandleStrategy> getAllStrategy(Context context) {
        HashMap<String, IMessageHandleStrategy> strategyHashMap = new HashMap<>(25);
        List<String> classList = scanClass(context);
        if (classList.size() > 0) {
            for (int i = 0; i < classList.size(); i++) {
                try {

                    Class aClass = Class.forName(classList.get(i));
                    boolean exist = aClass.isAnnotationPresent(RegisterUnityMethod.class);
                    if(!exist){
                        continue;
                    }
                    RegisterUnityMethod unityMethod = (RegisterUnityMethod) aClass.getAnnotation(RegisterUnityMethod.class);
                    if (unityMethod != null && unityMethod.value().length>0) {
                        strategyHashMap.put("/" +StringUtils.arrayToString(unityMethod.value(),"/"), (IMessageHandleStrategy) aClass.newInstance());
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }

            }
        }
        return strategyHashMap;
    }
}

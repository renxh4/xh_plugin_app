package com.renxh.hook;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

public class PluginHelper {
    private static final String TAG = "mmm";


    public static void loadPluginClass(Context context, ClassLoader hostClassLoader) {
        // Step1. 获取到插件apk，通常都是从网络上下载，这里为了演示，直接将插件apk push到手机
        String pluginPath = copyFile("/sdcard/plugin1.apk", context);

        String dexopt = context.getDir("dexopt", 0).getAbsolutePath();
        DexClassLoader pluginClassLoader = new DexClassLoader(pluginPath, dexopt, null, hostClassLoader);

        try {
            //在这里测试是否真正加载了插件中的类
            Class<?> clz = pluginClassLoader.loadClass("com.renxh.pluginapp.PluginUtils");
            Object instance = clz.newInstance();
            Method showToast = clz.getMethod("add", int.class, int.class);
            int invoke = (int) showToast.invoke(instance, 1, 2);
            Log.d("mmm", "插件结果" + invoke);
        } catch (Exception e) {
            e.printStackTrace();
        }



        // Step3. 通过反射获取到pluginClassLoader中的pathList字段
        Field baseDexpathList = null;
        try {
            //获取插件中的类
            baseDexpathList = BaseDexClassLoader.class.getDeclaredField("pathList");
            baseDexpathList.setAccessible(true);
            Object pathlist = baseDexpathList.get(pluginClassLoader);
            Field dexElementsFiled = pathlist.getClass().getDeclaredField("dexElements");
            dexElementsFiled.setAccessible(true);
            Object[] dexElements = (Object[]) dexElementsFiled.get(pathlist);

            //获取应用内的类
            Field baseDexpathList1 = BaseDexClassLoader.class.getDeclaredField("pathList");
            baseDexpathList1.setAccessible(true);
            Object pathlist1 = baseDexpathList1.get(hostClassLoader);
            Field dexElementsFiled1 = pathlist1.getClass().getDeclaredField("dexElements");
            dexElementsFiled1.setAccessible(true);
            Object[] dexElements1 = (Object[]) dexElementsFiled1.get(pathlist1);


            //创建一个数组
            Object[] finalArray = (Object[]) Array.newInstance(dexElements.getClass().getComponentType(),
                    dexElements.length + dexElements1.length);
            //合并插件和应用内的类
            System.arraycopy(dexElements, 0, finalArray, 0, dexElements.length);
            System.arraycopy(dexElements1, 0, finalArray, dexElements.length, dexElements1.length);
            //把新数组替换掉原先的数组
            dexElementsFiled1.set(pathlist1, finalArray);
            Log.d("mmm","插件加载完成");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static String copyFile(String patchpath, Context context) {
        String src = context.getFilesDir().getAbsolutePath() + File.separator + "plugin.apk";
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(patchpath));
            outputStream = new BufferedOutputStream(new FileOutputStream(src));
            byte[] temp = new byte[1024];
            int len;
            while ((len = (inputStream.read(temp))) != -1) {
                outputStream.write(temp, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return src;
    }
}

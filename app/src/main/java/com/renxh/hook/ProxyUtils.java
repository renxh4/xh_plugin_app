package com.renxh.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ProxyUtils {

    public static void doInstrumentationHook(Activity activity){
        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = null;
        try {
            mInstrumentationField = Activity.class.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);

            // 创建代理对象
            Instrumentation originalInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
            mInstrumentationField.set(activity, new ProxyInstrumentation(originalInstrumentation));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static void doHandlerHook() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object activityThread = currentActivityThread.invoke(null);

            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(activityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new ProxyHandlerCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

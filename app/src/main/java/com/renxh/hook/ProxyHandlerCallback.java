package com.renxh.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;


public class ProxyHandlerCallback implements Handler.Callback {
    private Handler mBaseHandler;

    public ProxyHandlerCallback(Handler mBaseHandler) {
        this.mBaseHandler = mBaseHandler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d("mmm", "接受到消息了msg:" + msg);
        if (msg.what == 100) {
            try {
                Object obj = msg.obj;
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent intent = (Intent) intentField.get(obj);

                Intent targetIntent = intent.getParcelableExtra(TextActivity.TARGET_COMPONENT);
                intent.setComponent(targetIntent.getComponent());
                Log.e("mmmintentField", targetIntent.toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mBaseHandler.handleMessage(msg);
        return true;
    }
}

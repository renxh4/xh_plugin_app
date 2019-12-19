package com.renxh.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProxyUtils.doHandlerHook();
        ProxyUtils.doInstrumentationHook(this);

        initView();

        RequestPermissionsUtlis requestPermissionsUtlis = new RequestPermissionsUtlis(this);
        requestPermissionsUtlis.requestPermissions(99);
    }

    private void initView() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.renxh.pluginapp", "com.renxh.pluginapp.PluginActivity"));
                startActivity(intent);

            }
        });

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PluginHelper.loadPluginClass(MainActivity.this, MainActivity.class.getClassLoader());
                ResourceHelper.addResource(MainActivity.this, "/sdcard/plugin1.apk");
            }
        });


    }
}

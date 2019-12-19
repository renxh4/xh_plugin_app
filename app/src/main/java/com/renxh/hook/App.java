package com.renxh.hook;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public Resources getResources() {
        return ResourceHelper.sPluginResources == null ? super.getResources() : ResourceHelper.sPluginResources;
    }

    @Override
    public AssetManager getAssets() {
        return ResourceHelper.sNewAssetManager == null ? super.getAssets() : ResourceHelper.sNewAssetManager;
    }

}

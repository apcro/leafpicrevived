package com.alienpants.leafpic;

import androidx.multidex.MultiDexApplication;

import com.orhanobut.hawk.Hawk;

import com.alienpants.leafpic.util.ApplicationUtils;
import com.alienpants.leafpic.util.preferences.Prefs;

/**
 * Created by dnld on 28/04/16.
 */
public class App extends MultiDexApplication {

    private static App mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        ApplicationUtils.init(this);

        initialiseStorage();
    }

    public static App getInstance() {
        return mInstance;
    }

    private void initialiseStorage() {
        Prefs.init(this);
        Hawk.init(this).build();
    }
}

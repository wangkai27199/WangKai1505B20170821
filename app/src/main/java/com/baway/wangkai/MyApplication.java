package com.baway.wangkai;

import android.app.Application;

/**
 * Created by DELL on 2017/8/21.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
    }
}

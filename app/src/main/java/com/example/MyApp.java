package com.example;

import android.app.Application;

import com.xuexiang.xui.XUI;
import com.xuexiang.xutil.XUtil;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this);
        XUtil.init(this);
        XUI.debug(true);
    }
}

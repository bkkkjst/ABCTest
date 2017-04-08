package com.example.bkk.abctest;

import android.app.Application;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;


/**
 * Created by BKK on 7/4/2560.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

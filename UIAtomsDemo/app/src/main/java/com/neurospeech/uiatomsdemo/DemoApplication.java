package com.neurospeech.uiatomsdemo;

import android.app.Application;

import com.neurospeech.uiatoms.AtomNavigator;

/**
 * Created by ackav on 05-08-2017.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AtomNavigator.register(this,0, com.neurospeech.uiatomsdemo.BR.viewModel);

    }
}

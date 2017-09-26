package com.neurospeech.uiatomsdemo;

import android.app.Application;
import android.content.Context;

import com.neurospeech.uiatoms.AtomNavigator;
import com.neurospeech.uiatoms.DI;
import com.neurospeech.uiatomsdemo.services.LoginService;
import com.neurospeech.uiatomsdemo.services.LoginServiceAPI;

/**
 * Created by ackav on 05-08-2017.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        DI.put(Context.class, this);
        DI.put(LoginServiceAPI.class, (new LoginService()).api);

        AtomNavigator.register(this,
                com.neurospeech.uiatomsdemo.BR.model,
                com.neurospeech.uiatomsdemo.BR.viewModel);

    }
}

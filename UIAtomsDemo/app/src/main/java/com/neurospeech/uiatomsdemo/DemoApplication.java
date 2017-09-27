package com.neurospeech.uiatomsdemo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.neurospeech.uiatoms.DI;
import com.neurospeech.uiatomsdemo.services.LoginService;
import com.neurospeech.uiatomsdemo.services.LoginServiceAPI;

import br.com.zbra.androidlinq.Linq;

/**
 * Created by ackav on 05-08-2017.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        DI.put(Context.class, this);
        DI.put(LoginServiceAPI.class, (new LoginService()).api);
        DI.put(Handler.class, new Handler());

        //AtomNavigator.register(this);

        Log.d("",Linq.class.getCanonicalName());

    }
}

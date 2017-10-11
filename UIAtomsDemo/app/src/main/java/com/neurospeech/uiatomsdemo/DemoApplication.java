package com.neurospeech.uiatomsdemo;

import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.neurospeech.uiatoms.DI;
import com.neurospeech.uiatomsdemo.services.TaskService;
import com.neurospeech.uiatomsdemo.services.TaskServiceAPI;

import br.com.zbra.androidlinq.Linq;

/**
 * Created by ackav on 05-08-2017.
 */

public class DemoApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();


        DI.put(Context.class, this);
        DI.put(TaskServiceAPI.class, (new TaskService()).api);
        DI.put(Handler.class, new Handler());

        //AtomNavigator.register(this);

        Log.d("",Linq.class.getCanonicalName());

    }
}

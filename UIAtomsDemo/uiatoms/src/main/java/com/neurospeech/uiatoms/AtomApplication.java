package com.neurospeech.uiatoms;

import android.content.Context;
import android.os.Handler;

/**
 * Created by ackav on 28-09-2017.
 */

public class AtomApplication {


    private static Context context;
    private static Handler handler;

    public static void configure(Context context){
        AtomApplication.context = context;

        handler = new Handler();
        DI.put(Handler.class,handler);
    }

    public void post(Runnable runnable){
        handler.post(runnable);
    }

}

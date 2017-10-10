package com.neurospeech.uiatoms;

import android.util.Log;

/**
 * Created by ackav on 28-09-2017.
 */

public class AtomLogger {
    public void log(Throwable ex) {
        ex.printStackTrace();
    }

    public void log(String message){
        Log.d("Atom-Logger",message);
    }

    public static AtomLogger getInstance(){
        return DI.resolve(AtomLogger.class);
    }

}

package com.neurospeech.uiatoms;

/**
 * Created by ackav on 28-09-2017.
 */

public class AtomLogger {
    public void log(Throwable ex) {
        ex.printStackTrace();
    }

    public static AtomLogger getInstance(){
        return DI.resolve(AtomLogger.class);
    }

}

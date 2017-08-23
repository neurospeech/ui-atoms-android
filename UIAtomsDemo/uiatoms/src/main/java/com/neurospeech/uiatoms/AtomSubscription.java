package com.neurospeech.uiatoms;

/**
 * Created by ackav on 23-08-2017.
 */

public class AtomSubscription {

    public AtomSubscription(String message, Action action) {
        super();
        this.message = message;
        this.action = action;
    }

    public final String message;

    public final Action action;

    public Object tag;

}

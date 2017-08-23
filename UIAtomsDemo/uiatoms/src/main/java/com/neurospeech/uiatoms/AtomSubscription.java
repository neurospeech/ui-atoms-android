package com.neurospeech.uiatoms;

/**
 * Created by ackav on 23-08-2017.
 */

public class AtomSubscription {

    public AtomSubscription(String message, Action action, boolean background) {
        super();
        this.message = message;
        this.action = action;
        this.background = background;
    }

    public final String message;

    public final Action action;

    public final boolean background;


    public Object tag;

}

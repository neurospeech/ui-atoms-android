package com.neurospeech.uiatoms;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by ackav on 27-09-2017.
 */

public class ClosableAction implements Closeable {

    private final Runnable action;

    public ClosableAction(Runnable action){
        this.action = action;
    }

    @Override
    public void close() throws IOException {
        action.run();
    }
}

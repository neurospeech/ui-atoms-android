package com.neurospeech.uiatoms;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 27-09-2017.
 */

public class AtomWatcher implements Closeable {

    private List<Closeable> closables = new ArrayList<>();

    @Override
    public void close() throws IOException {

    }
}

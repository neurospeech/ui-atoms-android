package com.neurospeech.uiatoms;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by  on 10-08-2017.
 */

public class DIScope implements Closeable {

    HashMap<Class,Object> instances = new HashMap<>();



    @Override
    public void close() throws IOException {
        for (Object value: instances.values() ) {
            if(value instanceof Closeable){
                ((Closeable)value).close();
            }
        }
    }

}

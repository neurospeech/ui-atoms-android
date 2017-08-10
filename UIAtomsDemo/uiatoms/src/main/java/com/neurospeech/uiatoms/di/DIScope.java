package com.neurospeech.uiatoms.di;

import com.neurospeech.uiatoms.Func;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by  on 10-08-2017.
 */

public class DIScope implements Closeable {

    private HashMap<Class,Object> instances = new HashMap<>();


    static DIScope global = new DIScope();




    @Override
    public void close() throws IOException {
        for (Object value: instances.values() ) {
            if(value instanceof Closeable){
                ((Closeable)value).close();
            }
        }
    }

    <T> T get(Class c, Func<T> func) {
        if(instances.containsKey(c)){
            return (T)instances.get(c);
        }
        T item = func.func();
        instances.put(c,item);
        return item;
    }

    <T> void put(Class tx, T t) {
        instances.put(tx,t);
    }
}

package com.neurospeech.uiatoms.di;

import com.neurospeech.uiatoms.Func;

/**
 * Created by  on 10-08-2017.
 */

public class DINew<T> {

    private final Class c;
    private Func<? extends T> func;

    DINew(Class c, Func<? extends T> func){
        this.c = c;
        this.func = func;
    }

    public T get(){
        return func.func();
    }

    public void put(Func<? extends T> func){
        this.func = func;
    }

    public static <Tx> DINew<Tx> register(Class<Tx> c, Func<? extends Tx> func){
        return new DINew<Tx>(c,func);
    }
}

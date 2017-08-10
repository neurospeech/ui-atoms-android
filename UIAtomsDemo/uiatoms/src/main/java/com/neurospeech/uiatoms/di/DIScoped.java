package com.neurospeech.uiatoms.di;

import com.neurospeech.uiatoms.Func;

/**
 * Created by  on 10-08-2017.
 */

public class DIScoped<T> {

    private Func<? extends T> creator;
    private final Class c;

    public DIScoped(Class c, Func<? extends T> o) {
        this.creator = o;
        this.c = c;
    }

    public T get(DIScope scope){
        return scope.get(c,creator);
    }

    public void put(Func<? extends T> creator){
        this.creator = creator;
    }

    public static <Tx> DIScoped<Tx> register(Class<Tx> t, Func<? extends Tx> o) {
        return new DIScoped<Tx>(t, o);
    }
}

package com.neurospeech.uiatoms.di;

import com.neurospeech.uiatoms.Func;

/**
 * Created by  on 10-08-2017.
 */

public class DIGlobal<T> {


    private final Func<? extends T> func;
    private final Class<T> c;

    public DIGlobal(Class<T> c, Func<? extends T> func) {
        this.func = func;
        this.c = c;
    }

    public T get(){
        return DIScope.global.get(c,func);
    }

    public void put(T t){
        DIScope.global.put(c,t);
    }

    public static <Tx> DIGlobal<Tx> register(Class<Tx> t, Func<? extends  Tx> func) {
        return new DIGlobal<Tx>(t,func);
    }
}

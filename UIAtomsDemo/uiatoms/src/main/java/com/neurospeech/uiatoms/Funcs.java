package com.neurospeech.uiatoms;

/**
 * Created by  on 10-08-2017.
 */

public class Funcs {


    public interface Func0<T>{
        T call();
    }

    public interface Func1<T1,T>{
        T call(T1 item);
    }

    public interface Func2<T1,T2,T>{
        T call(T1 item,T2 item2);
    }

    public interface Func3<T1,T2,T3,T>{
        T call(T1 item,T2 item2,T3 item3);
    }

    public interface Func4<T1,T2,T3,T4,T>{
        T call(T1 item,T2 item2,T3 item3,T4 item4);
    }

}

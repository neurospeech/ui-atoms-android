package com.neurospeech.uiatoms;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by on 27-09-2017.
 */

public class AtomWatcher<T> implements Closeable {

    private List<Closeable> closables = new ArrayList<>();
    private Funcs.Func0<T> func;

    public AtomWatcher(BaseObservable... fields){
        for(BaseObservable field: fields){

            BaseObservable af = field;

            Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    run();
                }
            };

            field.addOnPropertyChangedCallback(callback);

            closables.add(new ClosableAction(()->{
                af.removeOnPropertyChangedCallback(callback);
            }));
        }
    }

    public AtomWatcher<T> setup(Funcs.Func0<T> func){
        this.func = func;
        return this;
    }


    AtomField<T> _result = new AtomField<T>();
    public AtomField<T> build(){
        return _result;
    }

    public void run() {
        try {
            _result.set(this.func.call());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void close() throws IOException {
        for(Closeable closeable: closables){
            try {
                closeable.close();
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
        closables.clear();
    }
}

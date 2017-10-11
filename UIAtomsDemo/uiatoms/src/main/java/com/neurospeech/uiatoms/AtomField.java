package com.neurospeech.uiatoms;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.os.Handler;

import java.util.Objects;

/**
 * Created by ackav on 27-09-2017.
 */

public class AtomField<T> extends BaseObservable {

    public T get() {
        return _value;
    }

    private boolean isChanging = false;

    public void set(T _value) {
        if(isChanging)
            return;

        isChanging = true;
            boolean changed = !Atom.equals(this._value,_value);
            this._value = _value;
            if(changed){
                DI.resolve(Handler.class).post(()-> {
                    this.notifyChange();
                    isChanging = false;
                });
            }
    }

    private T _value;

}

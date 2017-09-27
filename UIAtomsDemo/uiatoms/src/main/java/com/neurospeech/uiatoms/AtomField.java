package com.neurospeech.uiatoms;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

import java.util.Objects;

/**
 * Created by ackav on 27-09-2017.
 */

public class AtomField<T> extends BaseObservable {

    public T get() {
        return _value;
    }

    public void set(T _value) {
        boolean changed = !Objects.equals(this._value,_value);
        this._value = _value;
        if(changed){
            this.notifyChange();
        }
    }

    private T _value;

}

package com.neurospeech.uiatomsdemo.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;



/**
 * Created by akash.kava on 23-08-2017.
 */

public class Task {

    public Task() {
        super();
    }

    public Task(String label, String status){
        super();
        this.label = label;
        this.status = status;
    }

    public String label;

    public String status;

}

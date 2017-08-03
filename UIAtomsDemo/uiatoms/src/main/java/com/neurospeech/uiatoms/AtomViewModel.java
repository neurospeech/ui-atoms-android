package com.neurospeech.uiatoms;

import android.databinding.ObservableField;

import java.io.Closeable;
import java.util.ArrayList;

/**
 * Created by ackav on 04-08-2017.
 */

public class AtomViewModel<T>
{
    public final ObservableField<T> model = new ObservableField<T>(null);

    private ArrayList<Closeable> closables = new ArrayList<>();

    public void register(Closeable closeable){
        closables.add(closeable);
    }

    public void init(){

    }

    public void dispose(){
        for(Closeable closeable:closables){
            try{
                closeable.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}

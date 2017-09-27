package com.neurospeech.uiatoms;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by akash.kava on 23-08-2017.
 */

public class AtomActivity extends AppCompatActivity
    implements AtomLifeCycleProvider
{

    AtomLifeCycleElement lifeCycleProvider = new AtomLifeCycleElement();

    public AtomLifeCycleElement getLifeCycleElement() {
        return lifeCycleProvider;
    }

    public void disposeLifeCycleElement(){
        lifeCycleProvider.dispose();
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeCycleProvider.fireEvent(l -> l.onInit());
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeCycleProvider.fireEvent(l -> l.onResume());
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeCycleProvider.fireEvent(l -> l.onPause());
    }

    @Override
    protected void onDestroy() {
        lifeCycleProvider.fireEvent(l -> l.onDispose());
        super.onDestroy();
    }
}

package com.neurospeech.uiatoms;

import android.support.v4.app.Fragment;

/**
 * Created by ackav on 11-10-2017.
 */

public class AtomFragment extends
        Fragment
    implements
        AtomLifeCycleProvider
{

    AtomLifeCycleElement lifeCycleProvider = new AtomLifeCycleElement();

    @Override
    public AtomLifeCycleElement getLifeCycleElement() {
        return lifeCycleProvider;
    }

    @Override
    public void disposeLifeCycleElement() {
        lifeCycleProvider.dispose();
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeCycleProvider.fireEvent(l -> l.onInit());
    }

    @Override
    public void onResume() {
        super.onResume();
        lifeCycleProvider.fireEvent(l -> l.onResume());
    }

    @Override
    public void onPause() {
        super.onPause();
        lifeCycleProvider.fireEvent(l -> l.onPause());
    }

    @Override
    public void onDestroy() {
        lifeCycleProvider.fireEvent(l -> l.onDispose());
        super.onDestroy();
    }
}

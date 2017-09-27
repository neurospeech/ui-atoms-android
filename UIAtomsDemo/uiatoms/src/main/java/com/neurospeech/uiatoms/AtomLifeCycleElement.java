package com.neurospeech.uiatoms;

import java.io.Closeable;
import java.util.ArrayList;

/**
 * Created by ackav on 27-09-2017.
 */

public class AtomLifeCycleElement {

    private ArrayList<AtomLifeCycle> lifeCycles;

    public Closeable register(AtomLifeCycle lifeCycle){
        if(lifeCycles == null){
            lifeCycles = new ArrayList<>();
        }
        lifeCycles.add(lifeCycle);

        return new ClosableAction(()->{
            lifeCycles.remove(lifeCycle);
            if(lifeCycles.size()==0){
                lifeCycles = null;
            }
        });
    }

    public void fireEvent(Action<AtomLifeCycle> action){
        if(this.lifeCycles==null)
            return;
        for(AtomLifeCycle l: lifeCycles){
            action.run(l);
        }
    }

}

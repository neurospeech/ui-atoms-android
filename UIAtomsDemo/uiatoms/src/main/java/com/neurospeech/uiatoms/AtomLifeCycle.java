package com.neurospeech.uiatoms;

/**
 * Created by  on 27-09-2017.
 */

public interface AtomLifeCycle {

    void onInit();

    void onPause();

    void onResume();

    void onDispose();


}

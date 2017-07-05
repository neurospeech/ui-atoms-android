package com.neurospeech.uiatoms;

import android.databinding.ObservableBoolean;

/**
 * Created by  on 05-07-2017.
 */

public interface ListItem {

    Object getItem();

    ObservableBoolean getSelected();

    void toggleSelection();

}

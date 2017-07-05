package com.neurospeech.uiatoms;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by  on 05-07-2017.
 */

public class AtomForm extends LinearLayoutCompat {
    public AtomForm(Context context) {
        super(context);
        init();
    }

    public AtomForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AtomForm(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOrientation(VERTICAL);
    }
}

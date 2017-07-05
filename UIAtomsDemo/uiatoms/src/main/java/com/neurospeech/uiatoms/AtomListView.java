package com.neurospeech.uiatoms;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by on 05-07-2017.
 */

public class AtomListView extends RecyclerView {

    public ObservableList getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(@NonNull ObservableList selectedItems) {
        this.selectedItems = selectedItems;
    }

    private ObservableList selectedItems = new ObservableArrayList<Object>();

    public AtomListView(Context context) {
        super(context);
    }

    public AtomListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AtomListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @BindingAdapter("selectedItems")
    public static <T> void setSelectedItems(
            AtomListView view,
            @Nullable ObservableList<T> list){
        if(list==null){
            list = new ObservableArrayList<T>();
        }
        view.setSelectedItems(list);
    }

}

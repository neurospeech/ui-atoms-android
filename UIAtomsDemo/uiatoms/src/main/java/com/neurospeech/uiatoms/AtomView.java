package com.neurospeech.uiatoms;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by  on 05-08-2017.
 */

public class AtomView extends FrameLayout {

    private int layoutId;
    private ViewDataBinding binding;

    public AtomView(@NonNull Context context) {
        super(context);
    }

    public AtomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AtomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(21)
    public AtomView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @BindingAdapter( value = {"layout","model","viewModel"}, requireAll =  false)
    public static void setLayout(AtomView view, Object layout, Object model, Object viewModel) {
        view.setLayout(layout, model, viewModel);
    }

    public void setLayout(Object layout, Object model, Object viewModel) {
        int layoutId = 0;
        if(layout instanceof String){
            Context context = getContext();
            layoutId = context.getResources().getIdentifier(layout.toString(),"layout", context.getPackageName() );
        }else{
            layoutId = (int)layout;
        }
        if(binding == null || this.layoutId!=layoutId) {
            this.layoutId = layoutId;
            Context context = getContext();
            binding = DataBindingUtil
                    .inflate(LayoutInflater.from(context), layoutId, this, false);

            removeAllViews();
            addView(binding.getRoot());
        }

        if(viewModel==null){
            // try to inherit...
            viewModel = AtomViewModel.get((View)getParent());
        }

        if(viewModel!=null){
            binding.setVariable(AtomNavigator.getNavigator().viewModelId, viewModel);
        }

        if(model!=null){
            binding.setVariable(AtomNavigator.getNavigator().modelId,model);
        }

    }
}

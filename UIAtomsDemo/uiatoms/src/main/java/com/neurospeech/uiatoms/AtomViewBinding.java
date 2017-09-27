package com.neurospeech.uiatoms;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by ackav on 27-09-2017.
 */

public class AtomViewBinding {

    private Fragment fragment;
    private Activity activity;
    private AtomViewModel viewModel;
    private ViewDataBinding view;


    public AtomViewBinding(Activity activity) {
        this.activity = activity;
    }

    public AtomViewBinding(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public static AtomViewBinding from(Activity activity){
        return new AtomViewBinding(activity);
    }

    public static AtomViewBinding from(Fragment fragment){
        return new AtomViewBinding(fragment);
    }

    public <T extends AtomViewModel> AtomViewBinding attach(AtomViewModel viewModel){
        //this.viewModel = DI.compose(viewModelClass,DI.global);
        this.viewModel = viewModel;

        if(this.viewModel instanceof  AtomLifeCycle) {

            AtomLifeCycleElement e = null;

            if (this.fragment != null) {
                if (this.fragment instanceof AtomLifeCycleProvider) {
                    e = ((AtomLifeCycleProvider) fragment).getLifeCycleElement();
                }
            } else {
                if (this.activity instanceof AtomLifeCycleProvider) {
                    e = ((AtomLifeCycleProvider) fragment).getLifeCycleElement();
                }
            }
            if (e != null) {
                e.register((AtomLifeCycle) this.viewModel);
            }
        }
        return this;
    }

    public View inflate(@LayoutRes int layoutId){
        this.view = DataBindingUtil.inflate(LayoutInflater.from(activity), layoutId ,null,false);
        ReflectionHelper.run(this.view,"set", this.viewModel);

        if(this.fragment==null){
            this.activity.setContentView(this.view.getRoot());
        }

        return this.view.getRoot();
    }

}

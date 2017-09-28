package com.neurospeech.uiatoms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import java.io.Closeable;
import java.io.Serializable;

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
                    e = ((AtomLifeCycleProvider) activity).getLifeCycleElement();
                }
            }
            if (e != null) {
                e.register((AtomLifeCycle) this.viewModel);

                this.viewModel.setBroadcaster(new AtomViewModel.Broadcaster() {
                    @Override
                    public void broadcast(String message, Object item) {
                        Intent bi = new Intent(message);
                        bi.putExtra("parameter", (Serializable) item);
                        activity.sendBroadcast(bi);
                    }
                });

                if (this.viewModel.subscriptions.size() > 0) {
                    for (AtomSubscription s : this.viewModel.subscriptions) {
                        if (!s.background)
                            continue;
                        register(s);
                    }
                }


                e.register(new AtomLifeCycle() {
                    @Override
                    public void onInit() {

                    }

                    @Override
                    public void onPause() {
                        for(AtomSubscription s: viewModel.subscriptions){
                            if(s.background)
                                continue;
                            unregister(s);
                        }

                    }

                    @Override
                    public void onResume() {
                        for(AtomSubscription s: viewModel.subscriptions){
                            if(s.background)
                                continue;
                            register(s);
                        }
                    }

                    @Override
                    public void onDispose() {
                        for(AtomSubscription s: viewModel.subscriptions){
                            if(!s.background)
                                continue;
                            unregister(s);
                        }
                    }
                });

            }
        }
        return this;
    }

    private void unregister(AtomSubscription s) {
        activity.unregisterReceiver((BroadcastReceiver)s.tag);
    }

    private void register(final AtomSubscription s) {
        s.tag = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Object parameter = intent.getSerializableExtra("parameter");
                s.action.run(parameter);
            }
        };

        activity.registerReceiver((BroadcastReceiver) s.tag, new IntentFilter(s.message));
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

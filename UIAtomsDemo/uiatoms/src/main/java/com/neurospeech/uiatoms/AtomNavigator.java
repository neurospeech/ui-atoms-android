package com.neurospeech.uiatoms;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import java.io.Serializable;

/**
 * Created by ackav on 04-08-2017.
 */

public class AtomNavigator {

    public static final String ATOM_LAYOUT_ID = "atom-layout-id";
    public static final String ATOM_VIEW_MODEL = "atom-view-model";
    public static final String ATOM_MODEL_PARCELABLE = "atom-model-parcelable";
    public static final String ATOM_MODEL_SERIALIZABLE = "atom-model-serializable";

    /**
     *
     */
    private static AtomNavigator navigator;

    private final Context context;
    public final int viewModelId;
    public final int modelId;

    private AtomNavigator(Application context, int modelId, int viewModelId){
        this.context = context;
        this.viewModelId = viewModelId;
        this.modelId = modelId;
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                initializeActivity(activity,savedInstanceState);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                registerBroadcast(activity,AtomViewModel.get(activity));
            }

            @Override
            public void onActivityPaused(Activity activity) {
                unregisterBroadcast(activity,AtomViewModel.get(activity));
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static void register(Application context, int modelId, int viewModelId){
        navigator = new AtomNavigator(context, modelId, viewModelId);
    }



    public static AtomNavigator getNavigator(){
        if(navigator==null)
            throw new RuntimeException("AtomNavigator.register must be called before accessing" +
                    "navigator");
        return navigator;
    }


    public NavigationBuilder activityIntent(){
        return new NavigationBuilder(context);
    }



    public void initializeActivity(Activity activity, Bundle savedInstanceState) {
        Intent intent = activity.getIntent();

        int layoutId = intent.getIntExtra(ATOM_LAYOUT_ID,0);
        if(layoutId==0)
            return;
        try {

            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), layoutId, null, false);
            Class<AtomViewModel> viewModelClass = (Class<AtomViewModel>) intent.getSerializableExtra(ATOM_VIEW_MODEL);
            AtomViewModel viewModel = viewModelClass.newInstance();
            binding.setVariable(viewModelId,viewModel);


            binding.getRoot().setTag(R.id.viewModel,viewModel);


            if(intent.hasExtra(ATOM_MODEL_PARCELABLE)){
                Parcelable model = intent.getParcelableExtra(ATOM_MODEL_PARCELABLE);
                binding.setVariable(modelId, model);
            }
            if(intent.hasExtra(ATOM_MODEL_SERIALIZABLE)){
                Serializable model = intent.getParcelableExtra(ATOM_MODEL_SERIALIZABLE);
                binding.setVariable(modelId, model);
            }

            activity.setContentView(binding.getRoot());
            registerBroadcast(activity, viewModel);


        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void registerBroadcast(Context activity, AtomViewModel viewModel) {
        // setup broadcaster...
        if(viewModel==null)
            return;

        viewModel.setBroadcaster((message,item)->{
            Intent bi = new Intent(message);
            bi.putExtra("parameter", (Serializable) item);
            activity.sendBroadcast(bi);
        });

        for (Object item : viewModel.subscriptions) {
            AtomSubscription subscription = (AtomSubscription)item;
            subscription.tag = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Object parameter = intent.getSerializableExtra("parameter");
                    subscription.action.run(parameter);
                }
            };

            activity.registerReceiver((BroadcastReceiver)subscription.tag, new IntentFilter(((AtomSubscription) item).message));
        }
    }

    public void unregisterBroadcast(Context context, AtomViewModel viewModel){
        if(viewModel==null)
            return;
        for(Object item: viewModel.subscriptions){
            AtomSubscription subscription = (AtomSubscription) item;
            if(subscription.tag != null) {
                BroadcastReceiver br = (BroadcastReceiver) subscription.tag;
                context.unregisterReceiver(br);
                subscription.tag = null;
            }
        }
    }


    public static class NavigationBuilder{


        private final Context context;
        private int layoutId;
        private Class<? extends Activity> activityClass;
        private Object modelValue;
        private Class<? extends AtomViewModel> viewModelClass;

        public NavigationBuilder(Context context) {
            this.context = context;
        }

        public NavigationBuilder layout(@LayoutRes int layoutId){
            this.layoutId = layoutId;
            return this;
        }

        public NavigationBuilder activity(Class<? extends Activity> activityClass){
            this.activityClass = activityClass;
            return this;
        }

        public <T> NavigationBuilder model(T modelValue){
            this.modelValue = modelValue;
            return this;
        }

        public NavigationBuilder viewModel(Class<? extends AtomViewModel> viewModelClass){
            this.viewModelClass = viewModelClass;
            return  this;
        }

        public void start(){
            Intent intent = createIntent(context);
            context.startActivity(intent);
        }

        public void startForResult(Activity activity, int requestCode){
            Intent intent = createIntent(activity);
            activity.startActivityForResult(intent, requestCode);
        }

        @NonNull
        private Intent createIntent(Context activity) {
            Intent intent = new Intent(activity,activityClass);
            intent.putExtra(ATOM_LAYOUT_ID,layoutId);
            intent.putExtra(ATOM_VIEW_MODEL,viewModelClass);
            if(modelValue != null) {
                if(modelValue instanceof Parcelable) {
                    intent.putExtra(ATOM_MODEL_PARCELABLE, (Parcelable) modelValue);
                }
                if(modelValue instanceof Serializable){
                    intent.putExtra(ATOM_MODEL_SERIALIZABLE,(Serializable)modelValue);
                }
            }
            return intent;
        }
    }
}

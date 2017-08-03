package com.neurospeech.uiatoms;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
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

public class AtomNavigator implements Application.ActivityLifecycleCallbacks {

    public static final String ATOM_LAYOUT_ID = "atom-layout-id";
    public static final String ATOM_VIEW_MODEL = "atom-view-model";
    public static final String ATOM_MODEL_PARCELABLE = "atom-model-parcelable";
    public static final String ATOM_MODEL_SERIALIZABLE = "atom-model-serializable";
    private static AtomNavigator navigator;
    private final Context context;
    private int modelId;
    private int viewModelId;

    private AtomNavigator(Context context){
        this.context = context;
    }

    public static void register(Application context, int modelId, int viewModelId){
        navigator = new AtomNavigator(context);

        context.registerActivityLifecycleCallbacks(navigator);
        navigator.modelId = modelId;
        navigator.viewModelId = viewModelId;
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

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Intent intent = activity.getIntent();

        int layoutId = intent.getIntExtra(ATOM_LAYOUT_ID,0);
        if(layoutId==0)
            return;
        try {

            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), layoutId, null, false);
            Class<AtomViewModel> viewModelClass = (Class<AtomViewModel>) intent.getSerializableExtra(ATOM_VIEW_MODEL);
            AtomViewModel viewModel = viewModelClass.newInstance();
            binding.setVariable(viewModelId,viewModel);

            if(intent.hasExtra(ATOM_MODEL_PARCELABLE)){
                Parcelable model = intent.getParcelableExtra(ATOM_MODEL_PARCELABLE);
                binding.setVariable(modelId, model);
            }
            if(intent.hasExtra(ATOM_MODEL_SERIALIZABLE)){
                Serializable model = intent.getParcelableExtra(ATOM_MODEL_SERIALIZABLE);
                binding.setVariable(modelId, model);
            }

            activity.setContentView(binding.getRoot());

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

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

    public static class NavigationBuilder{


        private final Context context;
        private int layoutId;
        private Class<Activity> activityClass;
        private Object modelValue;
        private Class<AtomViewModel> viewModelClass;

        public NavigationBuilder(Context context) {
            this.context = context;
        }

        public NavigationBuilder layout(@LayoutRes int layoutId){
            this.layoutId = layoutId;
            return this;
        }

        public NavigationBuilder activity(Class<Activity> activityClass){
            this.activityClass = activityClass;
            return this;
        }

        public <T> NavigationBuilder model(T modelValue){
            this.modelValue = modelValue;
            return this;
        }

        public NavigationBuilder viewModel(Class<AtomViewModel> viewModelClass){
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

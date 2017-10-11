package com.neurospeech.uiatoms;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.view.View;

import java.io.Closeable;
import java.util.ArrayList;

/**
 * Created by ackav on 04-08-2017.
 */

public class AtomViewModel implements AtomLifeCycle
{





    public static AtomViewModel get(View view){
        if(view==null)
            return null;
        Object vm = view.getTag(R.id.viewModel);
        if(vm==null){
            return get((View)view.getParent());
        }
        return (AtomViewModel)vm;
    }

    public static AtomViewModel get(Activity activity){
        View view = activity.findViewById(android.R.id.content);
        if(view==null)
            return null;
        return (AtomViewModel)view.getTag(R.id.viewModel);
    }

    private ArrayList<Closeable> closables = new ArrayList<>();

    public final ArrayList<AtomSubscription> subscriptions = new ArrayList<AtomSubscription>();

    /***
     * Register any resource that implements closeable, it will be closed
     * when viewModel is disposed
     * @param closeable
     */
    public void register(Closeable closeable){
        closables.add(closeable);
    }



    /***
     * Subscribe for channel when activity/fragment only when active.
     * This method must be called in constructor only
     * @param channel
     * @param action
     * @param <Tx>
     */
    public <Tx> void sunscribe(String channel, Action<Tx> action){
        subscriptions.add(new AtomSubscription(channel,action, false));
    }

    /***
     * Subscribe for channel when activity/fragment is visible and also
     * in background.
     * This method must be called in constructor only
     * @param channel
     * @param action
     * @param <Tx>
     */
    public <Tx> void sunscribeEvenInBackground(String channel, Action<Tx> action){
        subscriptions.add(new AtomSubscription(channel,action, true));
    }

    /***
     * Broadcast item to channel
     * @param channel
     * @param item
     * @param <Tx>
     */
    public <Tx> void broadcast(String channel, Tx item){
        if(this.broadcaster != null){
            this.broadcaster.broadcast(channel,item);
        }
    }

    public void dispose(){
        for(Closeable closeable:closables){
            try{
                closeable.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDispose() {
        dispose();
    }

    public interface Broadcaster{
        void broadcast(String message, Object item);
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(Broadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    private Broadcaster broadcaster;


    public <T> AtomWatcher<T> watch(BaseObservable... fields){
        AtomWatcher<T> watcher = new AtomWatcher<T>(fields);
        register(watcher);
        return watcher;
    }


    public void runAsync(Runnable runnable){
        (new AsyncTask<Object,Object,Object>(){

            @Override
            protected Object doInBackground(Object... objects) {
                runnable.run();
                return null;
            }
        }).execute(this);
    }

}

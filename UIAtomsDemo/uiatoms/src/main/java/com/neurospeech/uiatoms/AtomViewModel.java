package com.neurospeech.uiatoms;

import android.app.Activity;
import android.databinding.ObservableField;
import android.view.View;

import java.io.Closeable;
import java.util.ArrayList;

/**
 * Created by ackav on 04-08-2017.
 */

public class AtomViewModel<T>
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

    public final ObservableField<T> model = new ObservableField<T>(null);

    private ArrayList<Closeable> closables = new ArrayList<>();

    public final ArrayList<AtomSubscription> subscriptions = new ArrayList<AtomSubscription>();

    public void register(Closeable closeable){
        closables.add(closeable);
    }

    public void init(){

    }

    public <Tx> void onMessage(String message, Action<Tx> action){
        subscriptions.add(new AtomSubscription(message,action));
    }

    public <Tx> void broadcast(String message, Tx item){
        if(this.broadcaster != null){
            this.broadcaster.broadcast(message,item);
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
}

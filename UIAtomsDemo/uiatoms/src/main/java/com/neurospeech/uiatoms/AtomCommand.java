package com.neurospeech.uiatoms;

import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Button;

/**
 * Created by  on 05-08-2017.
 */

public class AtomCommand<T> {

    public final ObservableBoolean enabled = new ObservableBoolean(true);

    public final ObservableField<T> parameter = new ObservableField<>(null);

    public  Action<T> action;

    public AtomCommand(Action<T> action) {
        super();
        this.action = action;
    }

    public void execute(T p){
        parameter.set(p);
        if(enabled.get()){
            action.run(p);
        }
    }

    @BindingAdapter(value = {"clickCommand", "commandParameter"}, requireAll = false)
    public static void setAdapter(View button, AtomCommand command, Object parameter){

        Observable.OnPropertyChangedCallback pc;
        AtomCommand oldCommand = (AtomCommand)button.getTag(R.id.atomCommand);
        if(oldCommand != command) {
            if(button instanceof Button) {
                if(oldCommand!=null) {
                    pc = (Observable.OnPropertyChangedCallback)
                        button.getTag(R.id.atomCommandEnabled);
                    if (pc != null) {
                        oldCommand.enabled.removeOnPropertyChangedCallback(pc);
                    }
                }

                pc = new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        ((Button)button).setEnabled(command.enabled.get());
                    }
                };
                command.enabled.addOnPropertyChangedCallback(pc);
            }

            button.setTag(R.id.atomCommand, command);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    command.execute(parameter);
                }
            });
        }

        command.parameter.set(parameter);
    }
}

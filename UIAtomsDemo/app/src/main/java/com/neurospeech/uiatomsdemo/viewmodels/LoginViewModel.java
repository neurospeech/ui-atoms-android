package com.neurospeech.uiatomsdemo.viewmodels;

import android.databinding.ObservableField;

import com.neurospeech.uiatoms.AtomCommand;
import com.neurospeech.uiatoms.AtomViewModel;

/**
 * Created by ackav on 05-08-2017.
 */

public class LoginViewModel extends AtomViewModel {

    public final ObservableField<String> userName = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> error = new ObservableField<>();

    public final AtomCommand loginCommand;

    public LoginViewModel(){
        loginCommand = new AtomCommand(a -> onLoginCommand(a));
    }

    public void onLoginCommand(Object object){
        error.set(null);
        if(userName.get() == null || userName.get().isEmpty()){
            error.set("Username is required");
            return;
        }
        if(password.get() == null || password.get().isEmpty()){
            error.set("Password is required");
            return;
        }
    }

}

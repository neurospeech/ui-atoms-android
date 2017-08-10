package com.neurospeech.uiatomsdemo;

import android.content.Context;

import com.neurospeech.uiatoms.di.DIGlobal;
import com.neurospeech.uiatoms.di.DIScoped;
import com.neurospeech.uiatomsdemo.services.LoginService;
import com.neurospeech.uiatomsdemo.services.LoginServiceAPI;

/**
 * Created by  on 10-08-2017.
 */

public class DI {

    public final static DIGlobal<Context> context =
            DIGlobal.register(Context.class,() -> null);


    public final static DIGlobal<LoginServiceAPI> loginService =

            DIGlobal.register(
                    LoginServiceAPI.class,
                    ()-> (new LoginService()).api);

}

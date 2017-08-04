package com.neurospeech.uiatomsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neurospeech.uiatoms.AtomNavigator;
import com.neurospeech.uiatomsdemo.viewmodels.LoginViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AtomNavigator.getNavigator()
                .activityIntent()
                .activity(LoginActivity.class)
                .layout(R.layout.activity_login)
                .viewModel(LoginViewModel.class)
                .start();

    }
}

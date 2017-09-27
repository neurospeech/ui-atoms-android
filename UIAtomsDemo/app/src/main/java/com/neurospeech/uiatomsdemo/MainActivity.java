package com.neurospeech.uiatomsdemo;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.neurospeech.uiatoms.AtomActivity;
import com.neurospeech.uiatoms.AtomNavigator;
import com.neurospeech.uiatoms.AtomViewBinding;
import com.neurospeech.uiatoms.ReflectionHelper;
import com.neurospeech.uiatomsdemo.viewmodels.TaskEditorViewModel;

public class MainActivity extends AtomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);


        TaskEditorViewModel viewModel = new TaskEditorViewModel();

        AtomViewBinding.from(this)
                .attach(viewModel)
                .inflate(R.layout.activity_task_list);

//        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this)
//                ,R.layout.activity_task_list
//                ,null,false);
//
//        ReflectionHelper.run(binding,"set",viewModel);
//
//        setContentView(binding.getRoot());



//        AtomNavigator.getNavigator()
//                .activityIntent()
//                .layout(R.layout.activity_task_list)
//                .viewModel(TaskEditorViewModel.class)
//                .start();

        //finish();

        

    }
}

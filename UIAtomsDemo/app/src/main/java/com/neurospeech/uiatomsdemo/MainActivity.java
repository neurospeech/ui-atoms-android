package com.neurospeech.uiatomsdemo;

import android.os.Bundle;

import com.neurospeech.uiatoms.AtomActivity;
import com.neurospeech.uiatoms.AtomViewBinding;
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

package com.neurospeech.uiatomsdemo.viewmodels;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.neurospeech.uiatoms.AtomCommand;
import com.neurospeech.uiatoms.AtomList;
import com.neurospeech.uiatoms.AtomViewModel;
import com.neurospeech.uiatomsdemo.models.Task;

/**
 * Created by akash.kava on 23-08-2017.
 */

public class TaskEditorViewModel extends AtomViewModel {


    /***
     * Represents error with the view model
     */
    public final ObservableField<String> error = new ObservableField<>();


    /**
     * Stores list of task
     */
    public final AtomList<Task> tasks = new AtomList<>();

    /**
     * Model for storing new task to be added
     */
    public final ObservableField<Task> newTask = new ObservableField<>(new Task());

    /**
     * Add Command to add newTask in the list if validation was successful
     */
    public final AtomCommand addCommand;

    /***
     * Remove Command, to remove task (passed as parameter) from the list
     */
    public final AtomCommand<Task> removeCommand;

    /***
     * Constructor,
     *      All commands and subscriptions must be set in constructor only
     */
    public TaskEditorViewModel(){

        this.addCommand = new AtomCommand( c-> onAddCommand());

        this.removeCommand =new AtomCommand<Task>( task -> onRemoveCommand(task));
    }

    /**
     * Method executed when addCommand is invoked
     */
    private void onAddCommand() {
        Task task = newTask.get();
        error.set(null);
        if(task == null || task.label == null || task.label.isEmpty()){
            error.set("Task cannot be empty");
            return;
        }

        tasks.add(task);
        newTask.set(new Task());
    }

    /***
     * Method executed when removeCommand is invoked with paramter
     * @param task selected task passed here
     */
    private void onRemoveCommand(Task task) {
        tasks.remove(task);
    }


}

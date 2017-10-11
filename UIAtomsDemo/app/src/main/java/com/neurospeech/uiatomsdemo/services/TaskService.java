package com.neurospeech.uiatomsdemo.services;

import com.neurospeech.uiatoms.Funcs;
import com.neurospeech.uiatomsdemo.models.Task;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by  on 10-08-2017.
 */

public class TaskService {

    public final TaskServiceAPI api;

    private <T> Call<T> sendResult(Funcs.Func0<T> factory){
        return new Call<T>() {

            private boolean isExecuted;

            @Override
            public Response<T> execute() throws IOException {
                Response r = Response.success(factory.call());
                isExecuted = true;
                return r;
            }

            @Override
            public void enqueue(Callback<T> callback) {

            }

            @Override
            public boolean isExecuted() {
                return isExecuted;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<T> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };
    }

    public TaskService() {
        super();

        api = new TaskServiceAPI() {
            @Override
            public Call<Task[]> getTasks() {
                return sendResult(() -> new Task[] {
                        new Task("Task 1", "Pending"),
                        new Task("Task 2", "Pending"),
                        new Task("Task 3", "Pending"),
                        new Task("Task 4", "Resolved"),
                        new Task("Task 5", "Resolved"),
                        new Task("Task 6", "Resolved"),
                });
            }
        };
    }
}

package com.neurospeech.uiatomsdemo.services;

import com.neurospeech.uiatomsdemo.models.Task;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by akash.kava on 10-08-2017.
 */

public interface TaskServiceAPI {


    @GET
    Call<Task[]> getTasks();

}

package com.neurospeech.uiatoms.rest;


import android.support.annotation.NonNull;

import com.neurospeech.uiatoms.rest.json.PromiseConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Gigster on 30-12-2015.
 */
public abstract class RestService {

    public OkHttpClient getClient() {
        return client;
    }

    private OkHttpClient client;

    private Retrofit retrofit;

    protected RestService(){

        this.client = createHttpClient();

    }

    protected OkHttpClient createHttpClient() {
        return new OkHttpClient();
    }

    protected <T> T createAPIClient(String baseUrl, Class<T> apiClass){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.client(client);
        builder.addConverterFactory(createConverterFactory());
        builder.addCallAdapterFactory(createCallAdapterFactory());

        retrofit = builder.build();

        return retrofit.create(apiClass);

    }

    @NonNull
    protected PromiseConverterFactory createCallAdapterFactory() {
        return PromiseConverterFactory.create();
    }

    @NonNull
    protected JacksonConverterFactory createConverterFactory() {
        return JacksonConverterFactory.create();
    }


}
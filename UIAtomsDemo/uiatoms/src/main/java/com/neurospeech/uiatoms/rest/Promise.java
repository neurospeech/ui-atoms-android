package com.neurospeech.uiatoms.rest;


import android.os.AsyncTask;
import android.os.Handler;

import com.neurospeech.uiatoms.AtomLogger;
import com.neurospeech.uiatoms.DI;
import com.neurospeech.uiatoms.Funcs;
import com.neurospeech.uiatoms.ProgressIndicator;
import com.neurospeech.uiatoms.rest.json.PromiseConverterFactory;
import com.neurospeech.uiatoms.rest.json.StringConverter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by .kava on 10/20/2015.
 */
public class Promise<T> {



    private boolean showProgress = true;
    public Promise<T> setShowProgress(boolean v){
        showProgress = v;
        return this;
    }

    private List<IResultListener<T>> next;

    public Promise(){
        next = new ArrayList<IResultListener<T>>();
    }

    public T getResult() {
        return result;
    }

    public Callback<T> callback(final PromiseConverterFactory factory, final Converter<ResponseBody,T> converter) {


        return new Callback<T>() {

            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                try {

                    boolean isString = (converter instanceof StringConverter);

                    boolean isSuccess = response.isSuccessful() ||
                            (factory.isResponseSuccess(call.request(),response.raw()) && isString);

                    if (isSuccess) {
                        if(!response.isSuccessful()){
                            String msg = response.errorBody().string();
                            StringConverter<T> stringConverter = (StringConverter<T>)converter;
                            T value = stringConverter.convert(msg);
                            onResult(value,null);
                            //onResult(converter.convert(response.raw().body()),null);

                        }else {
                            onResult(response.body(), null);
                        }
                    } else {
                        String message = response.errorBody().string();
                        if(message.isEmpty())
                        {
                            message = response.message();
                        }
                        Exception error = new RuntimeException(message);
                        onResult(null, error);
                        //HyperCubeApplication.current.logError(message);
                        AtomLogger.getInstance().log(error);
                    }
                } catch (Exception ex) {
                    //Log.e("Error Response:",AndroidApplication.toString(ex));
                    //String message = HyperCubeApplication.toString(ex);
                    //HyperCubeApplication.current.logError(message);
                    AtomLogger.getInstance().log(error);
                    onResult(null, ex);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                //String e = HyperCubeApplication.toString(t);
                //Log.e("Error Response:", e);
                //HyperCubeApplication.current.logError(e);
                AtomLogger.getInstance().log(t);
                onResult(null, t );
            }
        };
    }
    private T result;
    private Throwable error;

    public Throwable getError(){
        return error;
    }

    public Promise<T> then(IResultListener<T> action){
        next.add(action);
        return this;
    }

    public Promise<T> then(final Promise<T> p){
        next.add(new IResultListener<T>() {
            @Override
            public void onResult(Promise<T> promise) {
                p.onResult(promise.getResult(),promise.getError());
            }
        });
        return this;
    }

    public void onStarted(){
        if(!showProgress)
            return;
        DI.resolve(ProgressIndicator.class).start();
    }

    /*
    * 0 = Pending
    * 1 = Complete
    * -1 = Failed
    * */
    private int status = 0;


    public T synchronousResult() throws Exception{

        final CountDownLatch latch = new CountDownLatch(1);

        this.then(new IResultListener<T>() {
            @Override
            public void onResult(Promise<T> promise) {
                latch.countDown();
            }
        });

        latch.await();

        if(error!=null)
            throw new Exception(error);
        return result;
    }

    public void onError(Exception error){
        onResult(null,error);
    }

    public void onResult(T r,Throwable error){


        if(showProgress){
            DI.resolve(ProgressIndicator.class).stop();
        }

        result = r;
        this.error = error;

        DI.resolve(Handler.class).post(()->{
            for (IResultListener<T> resultListener : next) {
                try {
                    resultListener.onResult(Promise.this);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }


    /**
     *
     * @param promises
     * @return
     */
    public static Promise<String> whenAll(Promise... promises){
        final Promise<String> promise =new Promise<String>();
        final StringBuilder sb = new StringBuilder();
        final List<Throwable> throwables = new ArrayList<>();
        final List<Object> pending = new ArrayList<Object>(promises.length);
        for (Promise p :promises) {
            pending.add(p);
            p.then(new IResultListener() {
                @Override
                public void onResult(Promise result) {
                    if(result.getError()!=null){
                        throwables.add(result.getError());
                    }
                    pending.remove(result);
                    if(pending.isEmpty()){
                        if(throwables.size()>0){
                            promise.onResult(null,new RuntimeException("Internal promises failed\r\n" + toErrorString(throwables)));
                        }else {
                            promise.onResult(null,null);
                        }
                    }
                }
            });
        }
        return promise;
    }

    private static String toErrorString(List<Throwable> throwables) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for(Throwable throwable: throwables){
            throwable.printStackTrace(pw);
        }
        return sw.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;


//    public <RT> Promise<RT> toPromise(final Funcs.Func1<T,RT> converter){
//        final Promise<RT> resultPromise = new Promise<>();
//        this.then(new IResultListener<T>() {
//            @Override
//            public void onResult(Promise<T> promise) {
//                T result = promise.result;
//                RT rt = null;
//                if (result != null) {
//                    //rt = converter.convert(result);
//                    runAsync(result, converter).then(new IResultListener<RT>() {
//                        @Override
//                        public void onResult(Promise<RT> promise) {
//                            resultPromise.onResult(promise.getResult(),promise.getError());
//                        }
//                    });
//                } else {
//                    resultPromise.onResult(rt, promise.getError());
//                }
//            }
//        });
//        return resultPromise;
//    }


    public static <RT> Promise<RT> withResult(final RT result){
        final Promise<RT> promise = new Promise<>();
        DI.resolve(Handler.class).post(new Runnable() {
            @Override
            public void run() {
                promise.onResult(result, null);
            }
        });
        return promise;
    }


    public static <RT> Promise<RT> withError(final Exception ex){
        final Promise<RT> promise = new Promise<>();
        DI.resolve(Handler.class).post(new Runnable() {
            @Override
            public void run() {
                promise.onError(ex);
            }
        });
        return promise;
    }


    public void setResult(T result) {
        this.result = result;
    }

    public static <T,RT> Promise<RT> runAsync(final T input,Funcs.Func1<T,RT> converter){
        final Promise<RT> promise = new Promise<>();
        (new AsyncTask<Funcs.Func1<T,RT>,Integer,RT>(){

            private Exception lastException = null;

            @Override
            protected RT doInBackground(Funcs.Func1<T,RT>... params) {
                try {
                    return params[0].call(input);
                }catch (Exception ex){
                    lastException = ex;
                }
                return null;
            }

            @Override
            protected void onPostExecute(RT rt) {
                super.onPostExecute(rt);

                promise.onResult(rt, lastException == null ? null : lastException);
                if(lastException!=null){
                    //Log.e("Error",HyperCubeApplication.toString(lastException));
                    AtomLogger.getInstance().log(lastException);
                }
            }
        }).execute(converter);
        return promise;
    }
}
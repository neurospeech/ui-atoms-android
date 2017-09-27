package com.neurospeech.uiatoms.rest;

/**
 * Created by akash.kava on 21-03-2016.
 */
public interface IResultListener<T> {

    void onResult(Promise<T> promise);

}
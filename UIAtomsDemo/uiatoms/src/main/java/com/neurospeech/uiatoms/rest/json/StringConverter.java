package com.neurospeech.uiatoms.rest.json;

/**
 * Created by ackav on 17-06-2016.
 */
public interface StringConverter<T> {

    T convert(String text) throws  Exception;

}
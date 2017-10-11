package com.neurospeech.uiatoms;

public interface ModelFilter<T> {

    boolean filter(T item);

}
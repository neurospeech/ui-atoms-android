package com.neurospeech.uiatoms.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.RequestBody;

/**
 * Created by ackav on 28-09-2017.
 */

public abstract class JacksonConfig {

    public static JacksonConfig defaultConfig;

    public abstract void setup(ObjectMapper up);

    public RequestBody convertPostData(byte[] bytes) {
        return null;
    }
}

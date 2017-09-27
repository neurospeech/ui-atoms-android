package com.neurospeech.uiatoms.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.neurospeech.uiatoms.AtomLogger;
import com.neurospeech.uiatoms.DI;
import com.neurospeech.uiatoms.rest.json.JacksonConfig;
import com.neurospeech.uiatoms.rest.json.StringConverter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by ackav on 28-09-2017.
 */

public final class JacksonConverterFactory extends Converter.Factory {
    /** Create an instance using a default {@link ObjectMapper} instance for conversion. */
    public static JacksonConverterFactory create() {
        return create(new ObjectMapper());
    }

    /** Create an instance using {@code mapper} for conversion. */
    public static JacksonConverterFactory create(ObjectMapper mapper) {
        return new JacksonConverterFactory(mapper);
    }

    private final ObjectMapper mapper;

    private JacksonConverterFactory(ObjectMapper mapper) {
        if (mapper == null) throw new NullPointerException("mapper == null");
        this.mapper = mapper;
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        if(JacksonConfig.defaultConfig!=null){
            JacksonConfig.defaultConfig.setup(mapper);
        }

    }

    @Override
    public Converter<okhttp3.ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        ObjectReader reader = mapper.reader(javaType);
        return new JacksonResponseBodyConverter<>(reader);
    }

    @Override
    public Converter<?, okhttp3.RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        ObjectWriter writer = mapper.writerWithType(javaType);
        return new JacksonRequestBodyConverter<>(writer);
    }

    static class JacksonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

        private final ObjectWriter adapter;

        JacksonRequestBodyConverter(ObjectWriter adapter) {
            this.adapter = adapter;
        }

        @Override public RequestBody convert(T value) throws IOException {
            byte[] bytes = adapter.writeValueAsBytes(value);

            if(JacksonConfig.defaultConfig != null){
                RequestBody rb  = JacksonConfig.defaultConfig.convertPostData(bytes);
                if(rb!=null)
                    return rb;
            }

            return RequestBody.create(MEDIA_TYPE, bytes);
        }
    }

    static class JacksonResponseBodyConverter<T>
            implements Converter<ResponseBody, T>
            ,StringConverter<T> {
        private final ObjectReader adapter;

        JacksonResponseBodyConverter(ObjectReader adapter) {
            this.adapter = adapter;
        }

        @Override public T convert(ResponseBody value) throws IOException {

            String response = value.string();
            try {
                return adapter.readValue(response);
            }catch (Exception ex){
                //String error = HyperCubeApplication.toString(ex);
                //HyperCubeApplication.current.logError(response + "\r\n" + error);
                DI.resolve(AtomLogger.class).log(ex);
                throw  new JacksonParserException(response,ex);
            }
        }

        @Override
        public T convert(String text) throws Exception {
            return adapter.readValue(text);
        }
    }

    static class JacksonParserException extends IOException{

        public JacksonParserException(String response, Exception ex) {
            super(ex.getMessage() + "\r\n" + response, ex);
        }
    }
}

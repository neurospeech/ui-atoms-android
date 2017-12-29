package com.neurospeech.uiatoms;

import android.os.Handler;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by on 26-09-2017.
 */

public class DI {

    static HashMap<Class,Registration> registry = new HashMap<>();

    static{
        DI.register(AtomLogger.class);
        DI.register(ProgressIndicator.class);
        DI.put(Handler.class,new Handler());
    }


    public static DIScope global = new DIScope();

    public static <T> void register(Class<T> forClass){
        register(forClass,forClass);
    }

    public static <T> void register(Class<T> forClass, Class<? extends T> tClass){
        registry.put(forClass,new Registration(tClass, (scope) -> createScoped(forClass,tClass, global) ));
    }

    public static <T> void registerScoped(Class<T> forClass, Class<? extends T> tClass){
        registry.put(forClass,new Registration(tClass, (scope) -> createScoped(forClass,tClass, (DIScope) scope) ));
    }

    public static <T> void registerTransient(Class<T> forClass, Class<? extends T> tClass){
        registry.put(forClass,new Registration(tClass,(s) -> create(tClass,null)));
    }

    public static <T> void put(Class<T> tClass, T value){
        registry.put(tClass, new Registration(tClass, s-> value));
    }

    public static <T> void put(Class<T> tClass, Funcs.Func0<? extends T> factory){
        registry.put(tClass, new Registration(tClass, s -> factory.call()));
    }

    public static <T> T resolve(Class<T> tClass){
        Registration r = registry.get(tClass);
        return (T)r.factory.call(global);
    }

    public static <T> T resolve(Class<T> tClass, DIScope scope){
        Registration r = registry.get(tClass);
        return (T)r.factory.call(scope);
    }

    public static <T> T compose(Class<T> tClass, DIScope scope){
        return (T)create(tClass,scope);
    }

    private static Object createScoped(Class key, Class creator, DIScope scope)
    {
        Object result = HashMapEx.getOrDefaultValue(scope.instances,key,null);
        if(result == null){
            result = create(creator,scope);
        }
        scope.instances.put(key,result);
        return result;
    }


    static private Object create(Class factory, DIScope scope)
    {

        try {
            // find first public constructor...

            Constructor c = factory.getDeclaredConstructors()[0];

            ArrayList<Object> parameters = new ArrayList<>();

            for (Class pc : c.getParameterTypes()) {
                parameters.add(DI.resolve(pc));
            }

            return c.newInstance(parameters.toArray(new Object[parameters.size()]));
        }catch (Exception ex){
            throw new RuntimeException("Creation of object failed for " + factory.getCanonicalName(), ex);
        }
    }


    static class Registration{
        private final Class key;
        private final Funcs.Func1 factory;

        Registration(Class key,
                     Funcs.Func1 factory
                     ){
            this.key = key;
            this.factory = factory;
        }


    }


}

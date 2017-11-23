package com.neurospeech.uiatoms;

import android.os.Build;
import android.util.DebugUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.zbra.androidlinq.Linq;
import br.com.zbra.androidlinq.Stream;

/**
 * Created by ackav on 26-09-2017.
 */

public class ReflectionHelper {

    static HashMap<String,ReflectionMethod> members = new HashMap<>();

    static class ReflectionMethod {
        Method method;
    }

    public static void run(Object object, String prefix, Object... parameters)
    {
        String key = object.getClass().getCanonicalName();

        for(Object obj: parameters){
            if(obj==null)
                return;
            key += ":" + obj.getClass().getCanonicalName();
        }

        Class c = object.getClass();

        ReflectionMethod method = getOrDefaultValue(members,key,null);
        if(method==null){
            ArrayList<Class> paramTypes = new ArrayList<>();
            Method[] methods = object.getClass().getMethods();
            for(Method m: methods){
                if(m.getName().startsWith(prefix)){
                    if(isMatch(m.getParameterTypes(),parameters)){
                        method = new ReflectionMethod();
                        method.method = m;
                        break;
                    }

                }
            }
            if(method==null){
                method = new ReflectionMethod();

                Log.w("ViewBinding",
                        "WARNING !! No method found with prefix "
                                + prefix
                                + " for given parameters in class "
                                + object.getClass().getCanonicalName()
                                + " for key\r\n"
                                + key);
//                throw new RuntimeException("No method found with prefix "
//                        + prefix + " for given parameters");
            }else{
                members.put(key,method);
            }
        }
        try {
            if(method.method != null)
                method.method.invoke(object,parameters);
        } catch (Exception e) {
            throw new RuntimeException("Failed running",e);
        }
    }

    private static <TKey,TR> TR getOrDefaultValue(HashMap<TKey, TR> members, TKey key, TR o) {
        TR r = members.get(key);
        if(r==null)
            return o;
        return r;
    }

    private static boolean isMatch(Class<?>[] parameterTypes, Object[] parameters) {


        if(parameterTypes.length != parameters.length)
            return false;

        for(int i=0;i<parameterTypes.length;i++){
            Class pt = parameterTypes[i];
            Object v = parameters[i];
            if(v==null)
                return false;
            Class p = v.getClass();

            if(p != pt){
                if(!pt.isAssignableFrom(p)){
                    return false;
                }
            }
        }

        return true;
    }


}

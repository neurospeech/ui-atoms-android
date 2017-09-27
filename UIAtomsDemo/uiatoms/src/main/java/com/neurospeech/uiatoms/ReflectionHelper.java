package com.neurospeech.uiatoms;

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

    static HashMap<String,Method> members = new HashMap<>();

    public static void run(Object object, String prefix, Object... parameters)
    {
        String key = object.getClass().getCanonicalName();

        for(Object obj: parameters){
            if(obj==null)
                return;
            key += ":" + obj.getClass().getCanonicalName();
        }

        Class c = object.getClass();

        Method method = members.getOrDefault(key,null);
        if(method==null){
            ArrayList<Class> paramTypes = new ArrayList<>();
            Method[] methods = object.getClass().getMethods();
            for(Method m: methods){
                if(m.getName().startsWith(prefix)){
                    if(isMatch(m.getParameterTypes(),parameters)){
                        method = m;
                        break;
                    }

                }
            }
            if(method==null){
                throw new RuntimeException("No method found with prefix "
                        + prefix + " for given parameters");
            }else{
                members.put(key,method);
            }
        }
        try {
            method.invoke(object,parameters);
        } catch (Exception e) {
            throw new RuntimeException("Failed running",e);
        }
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

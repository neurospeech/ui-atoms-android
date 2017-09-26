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

        Method method = members.getOrDefault(key,null);
        if(method==null){
            ArrayList<Class> paramTypes = new ArrayList<>();
            for(Method m: object.getClass().getMethods()){
                if(m.getName().startsWith(prefix)){
                    if(isMatch(m.getParameterTypes(),parameters)){
                        method = m;
                        break;
                    }

                }
            }
            if(method!=null){
                throw new RuntimeException("No method found with prefix "
                        + prefix + " for given parameters");
            }
        }
        try {
            method.invoke(object,parameters);
        } catch (Exception e) {
            throw new RuntimeException("Failed running",e);
        }
    }

    private static boolean isMatch(Class<?>[] parameterTypes, Object[] parameters) {

        Stream<Class> ptypes = Linq.stream(parameters).select(c -> c.getClass());

        for(Class pt: parameterTypes){
            if(!ptypes.any( ptype -> ptype == pt )){
                return false;
            }
        }

        return true;
    }


}

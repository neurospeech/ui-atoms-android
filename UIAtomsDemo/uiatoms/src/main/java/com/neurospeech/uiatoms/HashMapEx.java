package com.neurospeech.uiatoms;

import java.util.HashMap;

/**
 * Created by akash.kava on 29-12-2017.
 */

public class HashMapEx {

    public static <TKey,TR> TR getOrDefaultValue(HashMap<TKey, TR> members, TKey key, TR o) {
        TR r = members.get(key);
        if(r==null)
            return o;
        return r;
    }

}

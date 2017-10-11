package com.neurospeech.uiatoms;

/**
 * Created by ackav on 11-10-2017.
 */

public class Atom {

    public static boolean equals(Object left, Object right) {
        if(left == null)
            return right == null;
        if(right==null)
            return false;
        return left.equals(right);
    }

}

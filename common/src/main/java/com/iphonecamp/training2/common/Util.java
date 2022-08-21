package com.iphonecamp.training2.common;


import androidx.annotation.NonNull;

/**
 * Misc utilities
 */
public class Util {
    public static final @NonNull String LOG_TAG = "TRN";


    /**
     * Add cause to a given throwable and return it
     *
     * @param t Exception to add the given cause to
     * @param c Suppressed cause exception
     * @param <T> Exception type
     * @param <C> Cause exception type
     *
     * @return T The given exception
     */
    public static <T extends Throwable, C extends Throwable> T withCause(T t, C c) throws T {
        t.addSuppressed(c);
        return t;
    }
}

// IMPORTANT: This module was copied from 'training1'.

package com.iphonecamp.training2.common;


/**
 * Unexpected null object exception
 */
public class UnexpectedNullException extends Exception {
    /**
     * Construct from message
     *
     * @param message An optional descriptive error message
     */
    public UnexpectedNullException(String message) {
        super(message);
    }


    /**
     * Default constructor
     */
    public UnexpectedNullException() {
        super();
    }


    /**
     * Assert that given objects are not null
     * @param objects Objects to check
     * @throws UnexpectedNullException if one of the given objects is null
     */
    public static void nonNull(Object... objects) throws UnexpectedNullException {
        for (Object obj : objects) {
            if (null == obj) {
                throw new UnexpectedNullException();
            }
        }
    }


    /**
     * Assert that the given object is not null
     *
     * @param obj Object to check
     * @param <T> Type of object to check
     * @return The given object
     * @throws UnexpectedNullException if given object is null
     */
    public static <T> T nonNull(T obj) throws UnexpectedNullException {
        if (null == obj) {
            throw new UnexpectedNullException();
        }
        return obj;
    }
}

package com.fsc.cicerone.functional_interfaces;

/**
 * An interface that emulates Java's
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html">java.util.function.Consumer&lt;Boolean&gt;</a>.
 * It can be implemented by a lambda function.
 * It was created due to the original class not being available in the chosen Android API.
 */
public interface BooleanRunnable {
    /**
     * The method that will be called by the interface's users.
     *
     * @param success Whether the previous operations were executed with success or not.
     */
    void accept(boolean success);
}
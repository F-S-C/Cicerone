package com.fsc.cicerone.functional_interfaces;

/**
 * An interface that emulates Java's
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html">java.util.function.Consumer&lt;T&gt;</a>.
 * It can be implemented by a lambda function.
 * It was created due to the original class not being available in the chosen Android API.
 */
public interface Consumer<T> {
    /**
     * The method that will be called by the interface's users.
     *
     * @param value The value to be accepted.
     * @see java.util.function.Consumer#accept(Object)
     */
    void accept(T value);
}

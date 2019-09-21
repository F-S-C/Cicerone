package com.fsc.cicerone.functional_interfaces;

import com.fsc.cicerone.model.BusinessEntity;

/**
 * An interface that emulates Java's
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/BiConsumer.html">java.util.function.BiConsumer&lt;BusinessEntity, boolean&gt;</a>.
 * It can be implemented by a lambda function.
 * It was created due to the original class not being available in the chosen Android API.
 */
public interface RunnableUsingBusinessEntity {
    /**
     * The method that will be called by the interface's users.
     *
     * @param result  The JSON Object to be used in the body.
     * @param success Whether the previous operations were executed with success or not.
     */
    void run(BusinessEntity result, boolean success);
}
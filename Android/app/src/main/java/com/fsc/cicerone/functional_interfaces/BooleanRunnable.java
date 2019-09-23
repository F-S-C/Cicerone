/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
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

package com.fsc.cicerone.model;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * A factory for a BusinessEntity's object.
 */
public class BusinessEntityBuilder<T extends BusinessEntity> {

    private Class<T> type;

    private BusinessEntityBuilder(Class<T> type) {
        this.type = type;
    }

    /**
     * Instantiate a BusinessEntity (T) using a JSONObject.
     *
     * @param jsonObject The JSONObject.
     * @return The BusinessEntity's object with the data from the JSONObject.
     * @throws NoSuchMethodException     If the constructor from JSONObject isn't found.
     * @throws IllegalAccessException    If the constructor from JSONObject can't be accessed.
     * @throws InvocationTargetException If the constructor form JSONObject throws an exception.
     * @throws InstantiationException    If the object can't be instantiated.
     * @see BusinessEntity#BusinessEntity(JSONObject)
     */
    public T fromJSONObject(JSONObject jsonObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return type.getConstructor(JSONObject.class).newInstance(jsonObject);
    }

    /**
     * Create a new BusinessEntityBuilder.
     *
     * @param type The type of BusinessEntity.
     * @param <T>  The type of BusinessEntity.
     * @return The factory.
     */
    public static <T extends BusinessEntity> BusinessEntityBuilder<T> getFactory(Class<T> type) {
        return new BusinessEntityBuilder<>(type);
    }
}

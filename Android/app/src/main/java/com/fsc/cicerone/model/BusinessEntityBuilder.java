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
import java.util.Map;

public class BusinessEntityBuilder<T extends BusinessEntity> {

    private Class<T> type;

    private BusinessEntityBuilder(Class<T> type){
        this.type = type;
    }

    public T fromJSONObject(String json) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return type.getConstructor(String.class).newInstance(json);
    }

    public static <T extends BusinessEntity> BusinessEntityBuilder<T> getFactory(Class<T> type){
        return new BusinessEntityBuilder<>(type);
    }
}

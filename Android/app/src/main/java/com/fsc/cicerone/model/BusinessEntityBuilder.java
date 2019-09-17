package com.fsc.cicerone.model;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class BusinessEntityBuilder<T extends BusinessEntity> {

    private Class<T> type;

    private BusinessEntityBuilder(Class<T> type){
        this.type = type;
    }

    public T fromJSONObject(JSONObject jsonObject) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return type.getConstructor(JSONObject.class).newInstance(jsonObject);
    }

    public static <T extends BusinessEntity> BusinessEntityBuilder<T> getFactory(Class<T> type){
        return new BusinessEntityBuilder<>(type);
    }
}

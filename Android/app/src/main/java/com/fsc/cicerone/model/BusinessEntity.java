package com.fsc.cicerone.model;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public abstract class BusinessEntity {

    protected BusinessEntity(){

    }

    BusinessEntity(JSONObject jsonObject){
        throw new UnsupportedOperationException("This method must be implemented");
    }

    public abstract JSONObject toJSONObject();

    @NonNull
    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}
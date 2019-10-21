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

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The base class of all Cicerone's business entities.
 */
public abstract class BusinessEntity {

    /**
     * Default empty constructor.
     */
    protected BusinessEntity() {
    }

    /**
     * BusinessEntity constructor's. Convert a JSONObject to BusinessEntity.
     *
     * @param jsonObject The JSONObject.
     */
    public BusinessEntity(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * BusinessEntity constructor's. Convert a json string to BusinessEntity.
     *
     * @param json The json string.
     */
    public BusinessEntity(String json) {
        this.loadFromJSONObject(getJSONObject(json));
    }

    /**
     * Convert a BusinessEntity to JSONObject.
     *
     * @return The JSONObject.
     */
    public abstract JSONObject toJSONObject();

    /**
     * Convert a json string to JSONObject.
     *
     * @param json The json string.
     * @return The JSONObject.
     */
    protected static JSONObject getJSONObject(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            String errorMessage = e.getMessage() + " --- Trying to parse: \"" + json + "\"";
            Log.e("JSON_PARSING_EXCEPTION", errorMessage);
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    /**
     * Set the BusinessEntity's properties loading them from a JSONObject.
     *
     * @param jsonObject The JSONObject.
     */
    protected abstract void loadFromJSONObject(JSONObject jsonObject);

    /**
     * @see Object#toString()
     */
    @NonNull
    @Override
    public String toString() {
        return toJSONObject().toString();
    }
}
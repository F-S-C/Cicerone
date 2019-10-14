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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class BusinessEntity {

    protected BusinessEntity() {
    }

    public BusinessEntity(Map<String, Object> jsonObject) {
        loadFromMap(jsonObject);
    }

    public BusinessEntity(String json) {
        this.loadFromMap(getMapFromJson(json));
    }

    public abstract Map<String, Object> toMap();

    protected static Map<String, Object> getMapFromJson(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            String errorMessage = e.getMessage() + " --- Trying to parse: \"" + json + "\"";
            Log.e("JSON_PARSING_EXCEPTION", errorMessage);
            jsonObject = new JSONObject();
        }

        Map<String, Object> map = new HashMap<>(jsonObject.length());
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                map.put(key, jsonObject.get(key));
            } catch (JSONException e) {
                Log.e("JSON_READING_EXCEPTION", "key: " + key + ", message: " + e.getMessage());
            }
        }

        return map;
    }

    protected abstract void loadFromMap(Map<String, Object> jsonObject);

    @NonNull
    @Override
    public String toString() {
        Map<String, Object> map = toMap();
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                jsonObject.put(entry.getKey(), entry.getValue().toString());
            } catch (JSONException e) {
                Log.e("ERROR_CREATING_JSON", "error in inserting " + entry.getKey() + "/" + entry.getValue());
            }
        }
        return jsonObject.toString();
    }
}
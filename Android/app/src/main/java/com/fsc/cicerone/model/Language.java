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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A language as represented in the system Cicerone. A language is composed by a code and a name and
 * it's identified by its code.
 */
public class Language extends BusinessEntity {
    private String code;
    private String name;

    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String LANGUAGE_CODE_KEY = "language_code";
        public static final String LANGUAGE_NAME_KEY = "language_name";
    }

    /**
     * Default empty constructor.
     */
    public Language() {
    }

    /**
     * Create a new language by specifying its values.
     *
     * @param code The language's code.
     * @param name The language's name.
     */
    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Language(Map<String, Object> jsonObject) {
        loadFromMap(jsonObject);
    }

    public Language(String json) {
        this(getMapFromJson(json));
    }

    public static Set<Language> getSetFromJSONArray(String array) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(array);
        } catch (JSONException e) {
            jsonArray = new JSONArray();
        }

        Set<Language> toReturn = new HashSet<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                toReturn.add(new Language(jsonArray.getString(i)));
            } catch (JSONException e) {
                Log.e("LANGUAGE_ERROR", "Error while parsing JSON. Error: " + e.getMessage());
            }
        }

        return toReturn;
    }

    /**
     * Create a new language by using a JSONObject.
     *
     * @param language The language's code.
     */
    @Override
    protected void loadFromMap(Map<String, Object> language) {
        this.code = (String) language.get(Columns.LANGUAGE_CODE_KEY);
        this.name = (String) language.get(Columns.LANGUAGE_NAME_KEY);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> toReturn = new HashMap<>();
        toReturn.put(Columns.LANGUAGE_CODE_KEY, this.code);
        toReturn.put(Columns.LANGUAGE_NAME_KEY, this.name);
        return toReturn;
    }

    /**
     * Get the language's code.
     *
     * @return The language's code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the language's code.
     *
     * @param code The new language's code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get the language's name.
     *
     * @return The language's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the language's name.
     *
     * @param name The new language's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if two languages are the same language. The check is made by the two languages' codes.
     *
     * @param o The other language Object.
     * @return true if the two languages are equals (by code), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(code, language.code) &&
                Objects.equals(name, language.name);
    }

    /**
     * Generate an hash code for the object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}

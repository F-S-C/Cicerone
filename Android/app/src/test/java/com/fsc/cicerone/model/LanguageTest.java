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
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class LanguageTest {

    @Test
    public void getSetFromJSONArray() throws JSONException {

        final String string = "[{\"language_code\":\"fr\",\"language_name\":\"French\"},{\"language_code\":\"de\",\"language_name\":\"German\"},{\"language_code\":\"es\",\"language_name\":\"Spanish\"},{\"language_code\":\"uk\",\"language_name\":\"English (UK)\"},{\"language_code\":\"us\",\"language_name\":\"English (US)\"},{\"language_code\":\"it\",\"language_name\":\"Italian\"}]";
        JSONArray jsonArray = new JSONArray(string);
        Set<Language> languages = Language.getSetFromJSONArray(jsonArray.toString());

        assertEquals("Size is different", languages.size(), jsonArray.length());
        for (Language language : languages) {
            JSONObject obj = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("language_code").equals(language.getCode()))
                    obj = jsonArray.getJSONObject(i);
            }

            assertNotNull("Language not found", obj);
            assertEquals("Code is not equals", language.getCode(), obj.getString("language_code"));
            assertEquals("Name is not equals", language.getName(), obj.getString("language_name"));
        }
    }

    @Test
    public void loadFromJSONObject() throws JSONException {
        final String string = "{\"language_code\":\"IT\",\"language_name\":\"Italian\"}";

        final JSONObject jsonObject = new JSONObject(string);
        final Language language = new Language(jsonObject.toString());

        final String TAG = "Fields not setted properly";
        assertEquals(TAG, language.getCode(), jsonObject.getString("language_code"));
        assertEquals(TAG, language.getName(), jsonObject.getString("language_name"));
    }

    @Test
    public void toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"language_code\":\"IT\",\"language_name\":\"Italian\"}");

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

        Language language = new Language(jsonObject.toString());

        assertEquals("Fields didn't match", language.toMap(), map);
    }

    @Test
    public void getCode() throws NoSuchFieldException, IllegalAccessException {
        final Language language = new Language();
        final Field field = language.getClass().getDeclaredField("code");
        field.setAccessible(true);
        field.set(language, "code_test");

        //when
        final String result = language.getCode();

        //then
        assertEquals("field wasn't retrieved properly", result, "code_test");
    }

    @Test
    public void setCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Language language = new Language();

        //when
        language.setCode("code_test");

        //then
        final Field field = language.getClass().getDeclaredField("code");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(language), "code_test");
    }

    @Test
    public void getName() throws NoSuchFieldException, IllegalAccessException {
        final Language language = new Language();
        final Field field = language.getClass().getDeclaredField("name");
        field.setAccessible(true);
        field.set(language, "name_test");

        //when
        final String result = language.getName();

        //then
        assertEquals("field wasn't retrieved properly", result, "name_test");
    }

    @Test
    public void setName() throws NoSuchFieldException, IllegalAccessException {
        //given
        final Language language = new Language();

        //when
        language.setName("name_test");

        //then
        final Field field = language.getClass().getDeclaredField("name");
        field.setAccessible(true);
        assertEquals("field wasn't retrieved properly", field.get(language), "name_test");
    }

    @Test
    public void equals1() {
        final String string = "{\"language_code\":\"IT\",\"language_name\":\"Italian\"}";
        final String string1 = "{\"language_code\":\"EN\",\"language_name\":\"English\"}";
        Language l1 = new Language(string);
        Language l2 = new Language(string);
        Language l3 = new Language(string1);

        assertEquals("field wasn't retrieved properly", l1, l2);
        assertNotEquals("field wasn't retrieved properly", l1, l3);
        assertEquals("field wasn't retrieved properly", l1, l1);
    }

    @Test
    public void hashCode1() {
        final String string = "{\"language_code\":\"IT\",\"language_name\":\"Italian\"}";

        Language l1 = new Language(string);
        Language l2 = new Language(string);

        assertEquals("field wasn't retrieved properly", l1.hashCode(), l2.hashCode());
        assertEquals("field wasn't retrieved properly", l1.hashCode(), l1.hashCode());

    }
}
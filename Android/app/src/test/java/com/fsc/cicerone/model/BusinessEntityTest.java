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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class BusinessEntityTest {

    @Test
    public void getMapFromJson() throws JSONException {
        final String string = "{\"itinerary_code\":19,\"username\":{\"username\":\"utente2\",\"tax_code\":\"ut200000\",\"name\":\"Utente2\",\"surname\":\"Utente2\",\"password\":\"$2y$10$BZvdUkj41uVSK6chVnkh\\/uHoIOMNxpVigdC6mlnVoSb\\/0IgclUh0a\",\"email\":\"a.esposito39@studenti.uniba.it\",\"user_type\":\"1\",\"cellphone\":\"1245637897\",\"birth_date\":\"1998-02-06\",\"sex\":\"female\",\"document\":{\"document_number\":\"ut0009\",\"document_type\":\"identity card\",\"expiry_date\":\"2019-09-27\"},\"languages\":[]},\"title\":\"Prova notifiche\",\"description\":\"Proviamo le notifiche.\",\"beginning_date\":\"2019-09-27\",\"ending_date\":\"2019-10-31\",\"end_reservations_date\":\"2019-10-29\",\"maximum_participants_number\":100,\"minimum_participants_number\":1,\"location\":\"ovunque\",\"repetitions_per_day\":5,\"duration\":\"01:00:00\",\"image_url\":\"https:\\/\\/fscgroup.ddns.net\\/images\\/1569368557-5d8aa9ed1ca0c.jpg\",\"full_price\":\"10.00\",\"reduced_price\":\"0.00\",\"languages\":[{\"language_code\":\"EN\",\"language_name\":\"English\"},{\"language_code\":\"IT\",\"language_name\":\"Italian\"}]}";

        JSONObject jsonObject = new JSONObject(string);
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

        assertEquals("Field wasn't retrieved properly", map, BusinessEntity.getMapFromJson(string));
    }

}
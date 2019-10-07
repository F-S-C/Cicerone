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

public class Wishlist extends BusinessEntity {

    private User user;
    private Itinerary itinerary;

    private static final String ERROR_TAG = "WISHLIST_ERROR";

    public static class Columns{
        private Columns() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ITINERARY_IN_WISHLIST_KEY = "itinerary_in_wishlist";
    }

    public Wishlist(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    public Wishlist(String json) {
        this(getJSONObject(json));
    }

    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        User tempUser;
        Itinerary tempItinerary;

        try {
            tempUser = new User(jsonObject.getJSONObject(User.Columns.USERNAME_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempUser = new User();
        }

        try {
            tempItinerary = new Itinerary(jsonObject.getJSONObject(Columns.ITINERARY_IN_WISHLIST_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempItinerary = new Itinerary();
        }

        user = tempUser;
        itinerary = tempItinerary;
    }

    public User getUser() {
        return user;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(User.Columns.USERNAME_KEY, user.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.ITINERARY_IN_WISHLIST_KEY, itinerary.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        return jsonObject;
    }
}

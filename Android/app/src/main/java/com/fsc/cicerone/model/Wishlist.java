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

/**
 * A wish list has represented in Cicerone.
 */
public class Wishlist extends BusinessEntity {

    private User user;
    private Itinerary itinerary;

    private static final String ERROR_TAG = "WISHLIST_ERROR";

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String OWNER_KEY = "username";
        public static final String ITINERARY_IN_WISHLIST_KEY = "itinerary_in_wishlist";
    }

    /**
     * Wishlist's constructor. Convert a JSONObject to Wishlist.
     *
     * @param jsonObject The JSONObject.
     */
    public Wishlist(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * Wishlist's constructor. Convert a json string to Wishlist.
     *
     * @param json The json string.
     */
    public Wishlist(String json) {
        this(getJSONObject(json));
    }

    /**
     * @see BusinessEntity#loadFromJSONObject(JSONObject)
     */
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

    /**
     * Get the wishlist's user.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Get the wishlist's itinerary.
     *
     * @return The itinerary.
     */
    public Itinerary getItinerary() {
        return itinerary;
    }

    /**
     * @see BusinessEntity#toJSONObject()
     */
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

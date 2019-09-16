package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Wishlist extends BusinessEntity {

    private final User user;
    private final Itinerary itinerary;

    private static final String ERROR_TAG = "WISHLIST_ERROR";

    public Wishlist(JSONObject jsonObject) {
        User tempUser;
        Itinerary tempItinerary;

        try {
            tempUser = new User(jsonObject.getJSONObject("username"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempUser = new User();
        }

        try {
            tempItinerary = new Itinerary(jsonObject.getJSONObject("itinerary_in_wishlist"));
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
            jsonObject.put("username", user.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("itinerary_in_wishlist", itinerary.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        return jsonObject;
    }
}

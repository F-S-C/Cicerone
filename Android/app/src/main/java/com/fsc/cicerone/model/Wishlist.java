package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Wishlist extends BusinessEntity {

    private final User user;
    private final Itinerary itinerary;

    public Wishlist(JSONObject jsonObject){
        User tempUser;
        Itinerary tempItinerary;

        try{
            tempUser = new User(jsonObject.getJSONObject("username"));
        } catch (JSONException e) {
            Log.e("WISHLIST_ERROR", e.getMessage());
            tempUser = new User();
        }

        try {
            tempItinerary = new Itinerary(jsonObject.getJSONObject("itinerary_in_wishlist"));
        } catch (JSONException e) {
            Log.e("WISHLIST_ERROR", e.getMessage());
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
            jsonObject.put("username", user.getUsername());
        } catch (JSONException e) {
            Log.e("WISHLIST_ERROR", e.getMessage());
        }
        try {
            jsonObject.put("itinerary_in_wishlist", itinerary.getCode());
        } catch (JSONException e) {
            Log.e("WISHLIST_ERROR", e.getMessage());
        }

        return jsonObject;
    }
}

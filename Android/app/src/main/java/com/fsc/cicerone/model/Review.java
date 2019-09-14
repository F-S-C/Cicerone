package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Review extends BusinessEntity {
    private final User author;
    private int feedback;
    private String description;
    
    static final String ERROR_TAG = "REVIEW_ERROR";

    Review(JSONObject jsonObject) {
        User tempAuthor;
        try {
            tempAuthor = new User(jsonObject.getJSONObject("username"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempAuthor = new User();
        }
        author = tempAuthor;

        try {
            feedback = jsonObject.getInt("feedback");
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            feedback = 0;
        }

        try {
            description=jsonObject.getString("description");
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            description = "Error loading description";
        }
    }

    public User getAuthor() {
        return author;
    }

    public int getFeedback() {
        return feedback;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", author.getUsername());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("description", description);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("feedback", feedback);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return  jsonObject;
    }
}

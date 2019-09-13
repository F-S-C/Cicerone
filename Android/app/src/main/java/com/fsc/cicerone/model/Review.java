package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Review extends BusinessEntity {
    protected final User author;
    protected int feedback;
    protected String description;

    Review(JSONObject jsonObject) {
        User tempAuthor;
        try {
            tempAuthor = new User(jsonObject.getJSONObject("username"));
        } catch (JSONException e) {
            Log.e("REVIEW_ERROR", e.getMessage());
            tempAuthor = new User();
        }
        author = tempAuthor;

        try {
            feedback = jsonObject.getInt("feedback");
        } catch (JSONException e) {
            Log.e("REVIEW_ERROR", e.getMessage());
            feedback = 0;
        }

        try {
            description=jsonObject.getString("description");
        } catch (JSONException e) {
            Log.e("REVIEW_ERROR", e.getMessage());
            description = "<i>Error loading description</i>";
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
            Log.e("REVIEW_ERROR", e.getMessage());
        }
        try {
            jsonObject.put("description", description);
        } catch (JSONException e) {
            Log.e("REVIEW_ERROR", e.getMessage());
        }
        try {
            jsonObject.put("feedback", feedback);
        } catch (JSONException e) {
            Log.e("REVIEW_ERROR", e.getMessage());
        }
        return  jsonObject;
    }
}

package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Review extends BusinessEntity {
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
            jsonObject.put("username", author.toJSONObject());
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    private Review(Builder builder) {
        this.author = builder.author;
        this.feedback = builder.feedback;
        this.description = builder.description;
    }

    public static abstract class Builder {
        private final User author;
        private int feedback;
        private String description;

        public Builder(User author) {
            this.author = author;
        }

        public Builder feedback(int feedback) {
            this.feedback = feedback >= 1 && feedback <= 5 ? feedback : 0;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

       public abstract Review build();
    }
}

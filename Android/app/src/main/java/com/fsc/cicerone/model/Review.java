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
 * A review has represented in Cicerone.
 */
public abstract class Review extends BusinessEntity {

    private User author;
    private int feedback;
    private String description;

    static final String ERROR_TAG = "REVIEW_ERROR";

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String AUTHOR_KEY = "username";
        public static final String FEEDBACK_KEY = "feedback";
        public static final String DESCRIPTION_KEY = "description";
    }

    /**
     * The empty constructor.
     */
    Review() {
        // Do nothing
    }

    /**
     * Review's constructor. Convert a JSONObject to Review.
     *
     * @param jsonObject The JSONObject.
     */
    Review(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * Review's constructor. Convert a json string to Review.
     *
     * @param json The json string.
     */
    Review(String json) {
        this(getJSONObject(json));
    }

    /**
     * @see BusinessEntity#loadFromJSONObject(JSONObject)
     */
    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        User tempAuthor;
        try {
            tempAuthor = new User(jsonObject.getJSONObject(User.Columns.USERNAME_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempAuthor = new User();
        }
        author = tempAuthor;

        try {
            feedback = jsonObject.getInt(Columns.FEEDBACK_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            feedback = 0;
        }

        try {
            description = jsonObject.getString(Columns.DESCRIPTION_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            description = "Error loading description";
        }
    }

    /**
     * Get the review's author.
     *
     * @return The author.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Get the review's feedback.
     *
     * @return The feedback.
     */
    public int getFeedback() {
        return feedback;
    }

    /**
     * Get the review's description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the review's feedback.
     *
     * @param feedback The feedback.
     * @return The Review itself.
     */
    public Review setFeedback(int feedback) {
        this.feedback = feedback;
        return this;
    }

    /**
     * Set the review's description.
     *
     * @param description The description.
     * @return The Review itself.
     */
    public Review setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @see BusinessEntity#toJSONObject()
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(User.Columns.USERNAME_KEY, author.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.DESCRIPTION_KEY, description);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.FEEDBACK_KEY, feedback);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Review's constructor. Create a Review from a Builder.
     *
     * @param builder The factory for a review's object.
     */
    Review(Builder builder) {
        this.author = builder.author;
        this.feedback = builder.feedback;
        this.description = builder.description;
    }

    /**
     * A factory for a review's object.
     */
    public abstract static class Builder {
        private final User author;
        private int feedback;
        private String description;

        /**
         * Builder's constructor.
         *
         * @param author The author.
         */
        Builder(User author) {
            this.author = author;
        }

        /**
         * Set the review's feedback.
         *
         * @param feedback The feedback.
         * @return The Builder itself.
         */
        public Builder setFeedback(int feedback) {
            this.feedback = feedback;
            return this;
        }

        /**
         * Set the review's description.
         *
         * @param description The description.
         * @return The Builder itself.
         */
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Build the review.
         *
         * @return The review.
         */
        public abstract Review build();
    }
}


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

import java.util.Map;

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

        public static final String FEEDBACK_KEY = "feedback";
        public static final String DESCRIPTION_KEY = "description";
    }

    Review() {
        // Do nothing
    }

    Review(Map<String, Object> jsonObject) {
        loadFromMap(jsonObject);
    }

    Review(String json) {
        this(getMapFromJson(json));
    }

    @Override
    protected void loadFromMap(Map<String, Object> jsonObject) {
        author = new User((String) jsonObject.get(User.Columns.USERNAME_KEY));
        feedback = (int) jsonObject.get(Columns.FEEDBACK_KEY);
        description = (String) jsonObject.get(Columns.DESCRIPTION_KEY);
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

    public Review setFeedback(int feedback) {
        this.feedback = feedback;
        return this;
    }

    public Review setDescription(String description) {
        this.description = description;
        return this;
    }

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

    Review(Builder builder) {
        this.author = builder.author;
        this.feedback = builder.feedback;
        this.description = builder.description;
    }

    public abstract static class Builder {
        private final User author;
        private int feedback;
        private String description;

        Builder(User author) {
            this.author = author;
        }

        public Builder setFeedback(int feedback) {
            this.feedback = feedback;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public abstract Review build();
    }
}


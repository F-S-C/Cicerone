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
 * A user review has represented in Cicerone.
 */
public class UserReview extends Review {
    private User reviewedUser;

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String REVIEWED_USER_KEY = "reviewed_user";
    }

    /**
     * UserReview's constructor. Convert a JSONObject to UserReview.
     *
     * @param jsonObject The JSONObject.
     */
    public UserReview(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * UserReview's constructor. Convert a json string to UserReview.
     *
     * @param json
     */
    public UserReview(String json) {
        this(getJSONObject(json));
    }

    /**
     * @see BusinessEntity#loadFromJSONObject(JSONObject)
     */
    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        super.loadFromJSONObject(jsonObject);

        User tempReviewedUser;
        try {
            tempReviewedUser = new User(jsonObject.getJSONObject(Columns.REVIEWED_USER_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempReviewedUser = new User();
        }
        reviewedUser = tempReviewedUser;
    }

    /**
     * Get the reviewed user.
     *
     * @return The reviewed user.
     */
    public User getReviewedUser() {
        return reviewedUser;
    }

    /**
     * @see BusinessEntity#toJSONObject()
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject object = super.toJSONObject();
        try {
            object.put(Columns.REVIEWED_USER_KEY, reviewedUser.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return object;
    }

    private UserReview(Builder builder) {
        super(builder);
        reviewedUser = builder.reviewedUser;
    }

    /**
     * A factory for a UserReview's object.
     */
    public static class Builder extends Review.Builder {
        private final User reviewedUser;

        /**
         * Builder's constructor.
         *
         * @param author       The author.
         * @param reviewedUser The reviewed user.
         */
        public Builder(User author, User reviewedUser) {
            super(author);
            this.reviewedUser = reviewedUser;
        }

        /**
         * @see Review.Builder#setFeedback(int)
         */
        @Override
        public Builder setFeedback(int feedback) {
            return (Builder) super.setFeedback(feedback);
        }

        /**
         * @see Review.Builder#setDescription(String)
         */
        @Override
        public Builder setDescription(String description) {
            return (Builder) super.setDescription(description);
        }

        /**
         * @see Review.Builder#build()
         */
        @Override
        public UserReview build() {
            return new UserReview(this);
        }
    }
}

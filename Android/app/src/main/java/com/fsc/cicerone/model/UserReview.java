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

public class UserReview extends Review {
    private final User reviewedUser;

    public UserReview(JSONObject jsonObject) {
        super(jsonObject);

        User tempReviewedUser;
        try {
            tempReviewedUser = new User(jsonObject.getJSONObject("reviewed_user"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempReviewedUser = new User();
        }
        reviewedUser = tempReviewedUser;
    }

    public User getReviewedUser() {
        return reviewedUser;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject object = super.toJSONObject();
        try {
            object.put("reviewed_user", reviewedUser.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return object;
    }

    private UserReview(Builder builder) {
        super(builder);
        reviewedUser = builder.reviewedUser;
    }

    public static class Builder extends Review.Builder {
        private final User reviewedUser;

        public Builder(User author, User reviewedUser) {
            super(author);
            this.reviewedUser = reviewedUser;
        }

        @Override
        public Builder setFeedback(int feedback) {
            return (Builder) super.setFeedback(feedback);
        }

        @Override
        public Builder setDescription(String description) {
            return (Builder) super.setDescription(description);
        }

        @Override
        public UserReview build() {
            return new UserReview(this);
        }
    }
}

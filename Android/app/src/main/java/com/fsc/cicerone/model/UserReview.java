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

import java.util.Map;

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

    public UserReview(Map<String, Object> jsonObject) {
        loadFromMap(jsonObject);
    }

    public UserReview(String json) {
        this(getMapFromJson(json));
    }

    @Override
    protected void loadFromMap(Map<String, Object> jsonObject) {
        super.loadFromMap(jsonObject);

        reviewedUser = new User(jsonObject.get(Columns.REVIEWED_USER_KEY).toString());
    }

    public User getReviewedUser() {
        return reviewedUser;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> object = super.toMap();
        object.put(Columns.REVIEWED_USER_KEY, reviewedUser.toString());
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

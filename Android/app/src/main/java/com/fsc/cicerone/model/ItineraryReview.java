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
 * A itinerary review represented in the system Cicerone.
 */
public class ItineraryReview extends Review {
    private Itinerary reviewedItinerary;

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String REVIEWED_ITINERARY_KEY = "reviewed_itinerary";
    }

    /**
     * ItineraryReview's constructor. Convert a JSONObject to ItineraryReview.
     *
     * @param jsonObject The JSONObject.
     */
    public ItineraryReview(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * ItineraryReview's constructor. Convert a json string to Itinerary.
     *
     * @param json The json string.
     */
    public ItineraryReview(String json) {
        this(getJSONObject(json));
    }

    /**
     * @see BusinessEntity#loadFromJSONObject(JSONObject)
     */
    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        super.loadFromJSONObject(jsonObject);

        Itinerary tempReviewedUser;
        try {
            tempReviewedUser = new Itinerary(jsonObject.getJSONObject(Columns.REVIEWED_ITINERARY_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempReviewedUser = new Itinerary();
        }
        reviewedItinerary = tempReviewedUser;
    }

    public Itinerary getReviewedItinerary() {
        return reviewedItinerary;
    }

    /**
     * @see BusinessEntity#toJSONObject()
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject object = super.toJSONObject();
        try {
            object.put(Columns.REVIEWED_ITINERARY_KEY, reviewedItinerary.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return object;
    }

    private ItineraryReview(Builder builder) {
        super(builder);
        reviewedItinerary = builder.reviewedItinerary;
    }

    /**
     * A factory for a ItineraryReview's object.
     */
    public static class Builder extends Review.Builder {
        private final Itinerary reviewedItinerary;

        /**
         * Builder's constructor.
         *
         * @param author            The review author.
         * @param reviewedItinerary The reviewed itinerary.
         */
        public Builder(User author, Itinerary reviewedItinerary) {
            super(author);
            this.reviewedItinerary = reviewedItinerary;
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
        public ItineraryReview build() {
            return new ItineraryReview(this);
        }
    }
}

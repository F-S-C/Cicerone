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

public class ItineraryReview extends Review {
    private Itinerary reviewedItinerary;

    public ItineraryReview(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    public ItineraryReview(String json) {
        this(getJSONObject(json));
    }

    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        super.loadFromJSONObject(jsonObject);

        Itinerary tempReviewedUser;
        try {
            tempReviewedUser = new Itinerary(jsonObject.getJSONObject("reviewed_itinerary"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempReviewedUser = new Itinerary();
        }
        reviewedItinerary = tempReviewedUser;
    }

    public Itinerary getReviewedItinerary() {
        return reviewedItinerary;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject object = super.toJSONObject();
        try {
            object.put("reviewed_itinerary", reviewedItinerary.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return object;
    }

    private ItineraryReview(Builder builder) {
        super(builder);
        reviewedItinerary = builder.reviewedItinerary;
    }

    public static class Builder extends Review.Builder {
        private final Itinerary reviewedItinerary;

        public Builder(User author, Itinerary reviewedItinerary) {
            super(author);
            this.reviewedItinerary = reviewedItinerary;
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
        public ItineraryReview build() {
            return new ItineraryReview(this);
        }
    }
}

package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ItineraryReview extends Review {
    private final Itinerary reviewedItinerary;

    public ItineraryReview(JSONObject jsonObject) {
        super(jsonObject);

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

    public class Builder extends Review.Builder {
        private final Itinerary reviewedItinerary;

        public Builder(User author, Itinerary reviewedItinerary) {
            super(author);
            this.reviewedItinerary = reviewedItinerary;
        }

        @Override
        public ItineraryReview build() {
            return new ItineraryReview(this);
        }
    }
}

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
            object.put("reviewed_user", reviewedUser.getUsername());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        return object;
    }
}

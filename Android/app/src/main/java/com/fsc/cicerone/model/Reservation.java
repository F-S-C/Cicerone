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

import com.fsc.cicerone.app_connector.ConnectorConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Reservation extends BusinessEntity {

    private User client;
    private Itinerary itinerary;
    private int numberOfAdults;
    private int numberOfChildren;
    private float total;
    private Date requestedDate;
    private Date forwardingDate;
    private Date confirmationDate = null;

    private static class Columns{
        private static final String CLIENT_KEY = "username";
        private static final String ITINERARY_KEY = "booked_itinerary";
        private static final String NUMBER_OF_CHILDREN_KEY = "number_of_children";
        private static final String NUMBER_OF_ADULTS_KEY = "number_of_adults";
        private static final String TOTAL_KEY = "total";
        private static final String REQUESTED_DATE_KEY = "requested_date";
        private static final String FORWARDING_DATE_KEY = "forwarding_date";
        private static final String CONFIRMATION_DATE_KEY = "confirm_date";
    }

    public Reservation(User client, Itinerary itinerary) {
        this.client = client;
        this.itinerary = itinerary;
        // Automatically set everything to a default value
    }

    public User getClient() {
        return client;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public Date getForwardingDate() {
        return forwardingDate;
    }

    public Date getConfirmationDate() {
        return confirmationDate;
    }

    public float getTotal() {
        return total;
    }

    public boolean isConfirmed() {
        return confirmationDate != null;
    }

    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    /**
     * Convert the askForReservation to a JSON Object.
     *
     * @return A JSON Object containing the data that were stored in the object.
     */
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            result.put(Columns.CLIENT_KEY, this.client.toJSONObject());
            result.put(Columns.ITINERARY_KEY, this.itinerary.toJSONObject());
            result.put(Columns.NUMBER_OF_CHILDREN_KEY, this.numberOfChildren);
            result.put(Columns.NUMBER_OF_ADULTS_KEY, this.numberOfAdults);
            result.put(Columns.TOTAL_KEY, this.total);
            if (this.requestedDate != null) {
                result.put(Columns.REQUESTED_DATE_KEY, new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.requestedDate));
            }
            if (this.forwardingDate != null) {
                result.put(Columns.FORWARDING_DATE_KEY, new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.forwardingDate));
            }
            if (this.confirmationDate != null) {
                result.put(Columns.CONFIRMATION_DATE_KEY, new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.confirmationDate));
            }
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    public Reservation(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    public Reservation(String json) {
        this(getJSONObject(json));
    }

    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        final String ERROR_TAG = "ERR_CREATE_RESERVATION";

        User tempClient;
        try {
            tempClient = new User(jsonObject.getJSONObject(Columns.CLIENT_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempClient = new User();
        }
        this.client = tempClient;

        Itinerary tempItinerary;
        try {
            tempItinerary = new Itinerary(jsonObject.getJSONObject(Columns.ITINERARY_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempItinerary = new Itinerary();
        }
        this.itinerary = tempItinerary;

        try {
            this.numberOfChildren = jsonObject.getInt(Columns.NUMBER_OF_CHILDREN_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            this.numberOfChildren = 0;
        }

        try {
            this.numberOfAdults = jsonObject.getInt(Columns.NUMBER_OF_ADULTS_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            this.numberOfAdults = 0;
        }

        try {
            this.requestedDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString(Columns.REQUESTED_DATE_KEY));
        } catch (JSONException | ParseException e) {
            Log.e(ERROR_TAG, e.getMessage());
            this.requestedDate = null;
        }

        try {
            this.forwardingDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString("forwading_date"));
        } catch (JSONException | ParseException e) {
            Log.e(ERROR_TAG, e.getMessage());
            this.forwardingDate = null;
        }

        try {
            if (jsonObject.has(Columns.CONFIRMATION_DATE_KEY) && jsonObject.get(Columns.CONFIRMATION_DATE_KEY) != null && !jsonObject.getString(Columns.CONFIRMATION_DATE_KEY).equals("0000-00-00"))
                this.confirmationDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString(Columns.CONFIRMATION_DATE_KEY));
            else
                this.confirmationDate = null;
        } catch (JSONException | ParseException e) {
            Log.d(ERROR_TAG, e.getMessage());
            this.confirmationDate = null;
        }

        total = numberOfAdults * itinerary.getFullPrice() + numberOfChildren * itinerary.getReducedPrice();
    }

    private Reservation(Builder builder) {
        this.client = builder.client;
        this.itinerary = builder.itinerary;
        this.numberOfAdults = builder.numberOfAdults;
        this.numberOfChildren = builder.numberOfChildren;
        this.requestedDate = builder.requestedDate;
        this.forwardingDate = builder.forwardingDate;
        this.confirmationDate = builder.confirmationDate;
        this.total = this.numberOfAdults * this.itinerary.getFullPrice() + this.numberOfChildren * this.itinerary.getReducedPrice();
    }

    public static class Builder {
        private final User client;
        private final Itinerary itinerary;
        private int numberOfAdults;
        private int numberOfChildren;
        private Date requestedDate;
        private Date forwardingDate;
        private Date confirmationDate = null;

        public Builder(User client, Itinerary itinerary) {
            this.client = client;
            this.itinerary = itinerary;
        }

        public Builder numberOfAdults(int numberOfAdults) {
            this.numberOfAdults = numberOfAdults >= 0 ? numberOfAdults : 0;
            return this;
        }

        public Builder numberOfChildren(int numberOfChildren) {
            this.numberOfChildren = numberOfChildren >= 0 ? numberOfChildren : 0;
            return this;
        }

        public Builder requestedDate(Date requestedDate) {
            this.requestedDate = requestedDate;
            return this;
        }

        public Builder forwardingDate(Date forwardingDate) {
            this.forwardingDate = forwardingDate;
            return this;
        }

        public Builder confirmationDate(Date confirmationDate) {
            this.confirmationDate = confirmationDate;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}

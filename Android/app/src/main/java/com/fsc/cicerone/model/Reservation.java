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

/**
 * A reservation as represented in Cicerone.
 */
public class Reservation extends BusinessEntity {

    private User client;
    private Itinerary itinerary;
    private int numberOfAdults;
    private int numberOfChildren;
    private float total;
    private Date requestedDate;
    private Date forwardingDate;
    private Date confirmationDate = null;

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String CLIENT_KEY = "username";
        public static final String ITINERARY_KEY = "itinerary";
        public static final String BOOKED_ITINERARY_KEY = "booked_itinerary";
        public static final String NUMBER_OF_CHILDREN_KEY = "number_of_children";
        public static final String NUMBER_OF_ADULTS_KEY = "number_of_adults";
        public static final String TOTAL_KEY = "total";
        public static final String REQUESTED_DATE_KEY = "requested_date";
        public static final String FORWARDING_DATE_KEY = "forwarding_date";
        public static final String CONFIRMATION_DATE_KEY = "confirm_date";
    }

    /**
     * Reservation's constructor.
     *
     * @param client    The client.
     * @param itinerary The itinerary.
     */
    public Reservation(User client, Itinerary itinerary) {
        this.client = client;
        this.itinerary = itinerary;
        // Automatically set everything to a default value
    }

    /**
     * Get the Reservation's client.
     *
     * @return The client.
     */
    public User getClient() {
        return client;
    }

    /**
     * Get the Reservation's itinerary.
     *
     * @return The itinerary.
     */
    public Itinerary getItinerary() {
        return itinerary;
    }

    /**
     * Get the Reservation's number of adults.
     *
     * @return The number of adults.
     */
    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    /**
     * Get the Reservation's number of children.
     *
     * @return The number of children.
     */
    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    /**
     * Get the Reservation's requested date.
     *
     * @return The requested date.
     */
    public Date getRequestedDate() {
        return requestedDate;
    }

    /**
     * Get the Reservation's forward date.
     *
     * @return The forward date.
     */
    public Date getForwardingDate() {
        return forwardingDate;
    }

    /**
     * Get the Reservation's confirmation date.
     *
     * @return The confirmation date.
     */
    public Date getConfirmationDate() {
        return confirmationDate;
    }

    /**
     * Get the Reservation's total.
     *
     * @return The total.
     */
    public float getTotal() {
        return total;
    }

    /**
     * Get the value that indicates whether or not the reservation is confirmed.
     *
     * @return true if the reservation is confirmed, false otherwise.
     */
    public boolean isConfirmed() {
        return confirmationDate != null;
    }

    /**
     * Set the confirmation date.
     *
     * @param confirmationDate The confirmation date.
     */
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
            result.put(User.Columns.USERNAME_KEY, this.client.toJSONObject());
            result.put(Columns.BOOKED_ITINERARY_KEY, this.itinerary.toJSONObject());
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

    /**
     * Construct a Reservation from a JSON Object.
     *
     * @param jsonObject The JSON Object.
     */
    public Reservation(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * Construct a Reservation from a JSON string.
     *
     * @param json The JSON string.
     */
    public Reservation(String json) {
        this(getJSONObject(json));
    }

    /**
     * @see BusinessEntity#loadFromJSONObject(JSONObject)
     */
    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        final String ERROR_TAG = "ERR_CREATE_RESERVATION";

        User tempClient;
        try {
            tempClient = new User(jsonObject.getJSONObject(User.Columns.USERNAME_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempClient = new User();
        }
        this.client = tempClient;

        Itinerary tempItinerary;
        try {
            tempItinerary = new Itinerary(jsonObject.getJSONObject(Columns.BOOKED_ITINERARY_KEY));
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
            this.forwardingDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString(Columns.FORWARDING_DATE_KEY));
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

    /**
     * Construct a Reservation from a Builder.
     *
     * @param builder The builder.
     */
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

    /**
     * A factory for a reservation's object.
     */
    public static class Builder {
        private final User client;
        private final Itinerary itinerary;
        private int numberOfAdults;
        private int numberOfChildren;
        private Date requestedDate;
        private Date forwardingDate;
        private Date confirmationDate = null;

        /**
         * Construct a Builder.
         *
         * @param client    The reservation's client.
         * @param itinerary The requested itinerary.
         */
        public Builder(User client, Itinerary itinerary) {
            this.client = client;
            this.itinerary = itinerary;
        }

        /**
         * Set the number of adults.
         *
         * @param numberOfAdults The number of adults.
         * @return The builder itself.
         */
        public Builder numberOfAdults(int numberOfAdults) {
            this.numberOfAdults = numberOfAdults >= 0 ? numberOfAdults : 0;
            return this;
        }

        /**
         * Set the number of children.
         *
         * @param numberOfChildren The number of children.
         * @return The builder itself.
         */
        public Builder numberOfChildren(int numberOfChildren) {
            this.numberOfChildren = numberOfChildren >= 0 ? numberOfChildren : 0;
            return this;
        }

        /**
         * Set the requested date.
         *
         * @param requestedDate The requested date.
         * @return The builder itself.
         */
        public Builder requestedDate(Date requestedDate) {
            this.requestedDate = requestedDate;
            return this;
        }

        /**
         * Set the forwarding date.
         *
         * @param forwardingDate the forwarding date.
         * @return The builder itself.
         */
        public Builder forwardingDate(Date forwardingDate) {
            this.forwardingDate = forwardingDate;
            return this;
        }

        /**
         * Set the confirmation date.
         *
         * @param confirmationDate The confirmation date.
         * @return The builder itself.
         */
        public Builder confirmationDate(Date confirmationDate) {
            this.confirmationDate = confirmationDate;
            return this;
        }

        /**
         * Build the Reservation object.
         *
         * @return The Reservation object.
         */
        public Reservation build() {
            return new Reservation(this);
        }
    }
}

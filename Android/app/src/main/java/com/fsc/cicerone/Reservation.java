package com.fsc.cicerone;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app_connector.ConnectorConstants;

public class Reservation {

    private final User client;
    private final Itinerary itinerary;
    private int numberOfAdults;
    private int numberOfChildren;
    private float total;
    private Date requestedDate;
    private Date forwardingDate;
    private Date confirmationDate = null;

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
            result.put("username", this.client.getUsername());
            result.put("booked_itinerary", this.itinerary.getCode());
            result.put("number_of_children", this.numberOfChildren);
            result.put("number_of_adults", this.numberOfAdults);
            result.put("total", this.total);
            if (this.requestedDate != null) {
                result.put("requested_date", new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.requestedDate));
            }
            if (this.forwardingDate != null) {
                result.put("forwading_date", new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.forwardingDate));
            }
            if (this.confirmationDate != null) {
                result.put("confirm_date", new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.confirmationDate));
            }
        } catch (JSONException e) {
            result = null;
        }
        return result;
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
        private Date confirmationDate;

        public Builder(User client, Itinerary itinerary) {
            this.client = client;
            this.itinerary = itinerary;
        }

        public Builder(JSONObject jsonObject) {
            final String ERROR_TAG = "ERR_CREATE_RESERVATION";

            User tempClient;
            try {
                tempClient = new User(jsonObject.getJSONObject("username"));
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
                tempClient = new User();
            }
            this.client = tempClient;

            Itinerary tempItinerary;
            try {
                tempItinerary = new Itinerary(jsonObject.getJSONObject("booked_itinerary"));
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
                tempItinerary = new Itinerary();
            }
            this.itinerary = tempItinerary;

            try {
                this.numberOfChildren = jsonObject.getInt("number_of_children");
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
                this.numberOfChildren = 0;
            }

            try {
                this.numberOfAdults = jsonObject.getInt("number_of_adults");
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
                this.numberOfAdults = 0;
            }

            try {
                this.requestedDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString("requested_date"));
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
                this.confirmationDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString("confirm_date"));
            } catch (JSONException | ParseException e) {
                Log.e(ERROR_TAG, e.getMessage());
                this.confirmationDate = null;
            }
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

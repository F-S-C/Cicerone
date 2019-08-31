package com.fsc.cicerone;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
            result.put("requested_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.requestedDate));
            result.put("forwading_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.forwardingDate));
            if (this.confirmationDate != null) {
                result.put("confirm_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.confirmationDate));
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

        public Builder numberOfAudults(int numberOfAdults) {
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

        public Builder forwadingDate(Date forwardingDate) {
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

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * An <i>entity</i> class that stores the data of an itinerary.
 */
public class Itinerary extends BusinessEntity {

    private int code;
    private User cicerone;
    private String title;
    private String description;

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }
        public static final String ITINERARY_CODE_KEY = "itinerary_code";
        public static final String TITLE_KEY = "title";
        public static final String DESCRIPTION_KEY = "description";
        public static final String BEGINNING_DATE_KEY = "beginning_date";
        public static final String ENDING_DATE_KEY = "ending_date";
        public static final String END_RESERVATIONS_DATE_KEY = "end_reservations_date";
        public static final String LOCATION_KEY = "location";
        public static final String DURATION_KEY = "duration";
        public static final String REPETITIONS_PER_DAY_KEY = "repetitions_per_day";
        public static final String MINIMUM_PARTICIPANTS_KEY = "minimum_participants_number";
        public static final String MAXIMUM_PARTICIPANTS_KEY = "maximum_participants_number";
        public static final String FULL_PRICE_KEY = "full_price";
        public static final String REDUCED_PRICE_KEY = "reduced_price";
        public static final String IMG_URL_KEY = "image_url";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Itinerary itinerary = (Itinerary) o;
        return code == itinerary.code &&
                minParticipants == itinerary.minParticipants &&
                maxParticipants == itinerary.maxParticipants &&
                repetitions == itinerary.repetitions &&
                Float.compare(itinerary.fullPrice, fullPrice) == 0 &&
                Float.compare(itinerary.reducedPrice, reducedPrice) == 0 &&
                Objects.equals(cicerone, itinerary.cicerone) &&
                Objects.equals(title, itinerary.title) &&
                Objects.equals(description, itinerary.description) &&
                Objects.equals(beginningDate, itinerary.beginningDate) &&
                Objects.equals(endingDate, itinerary.endingDate) &&
                Objects.equals(reservationDate, itinerary.reservationDate) &&
                Objects.equals(location, itinerary.location) &&
                Objects.equals(duration, itinerary.duration) &&
                Objects.equals(imageUrl, itinerary.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, cicerone, title, description, beginningDate, endingDate, reservationDate, minParticipants, maxParticipants, location, repetitions, duration, fullPrice, reducedPrice, imageUrl);
    }

    private Date beginningDate;
    private Date endingDate;
    private Date reservationDate;
    private int minParticipants;
    private int maxParticipants;
    private String location;
    private int repetitions;
    private String duration;
    private float fullPrice;
    private float reducedPrice;
    private String imageUrl;
    private final SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    /**
     * Default empty constructor.
     */
    public Itinerary() {
        // Automatically set everything to a default value
    }

    public Itinerary(String json) {
        this(getJSONObject(json));
    }


    /**
     * Create a new Itinerary object based on a JSON Object. The JSON Object <i>must</i> contain
     * <i>at least</i> the following values:
     * <pre>
     * {@code
     * {
     *     "itinerary_code": "-1",
     *     "itineraryname": "test",
     *     "title": "Titolo",
     *     "description": "Descrizione",
     *     "beginningDate": "01-01-2000",
     *     "endingDate": "01-01-2000",
     *     "reservationDate": "01-01-2000",
     *     "location": "Location",
     *     "duration": "00:00:00",
     *     "repetitions": "1",
     *     "minParticipants": "0",
     *     "maxParticipants": "0",
     *     "fullPrice": "0",
     *     "reducedPrice": "0"
     * }
     * }
     * </pre>
     *
     * @param jsonObject The JSON object from which data will be fetched.
     */
    public Itinerary(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    @Override
    protected void loadFromJSONObject(JSONObject itinerary) {
        try {
            code = itinerary.getInt(Columns.ITINERARY_CODE_KEY);
        } catch (JSONException e) {
            code = -1;
        }

        try {
            cicerone = new User(itinerary.getJSONObject(User.Columns.USERNAME_KEY));
        } catch (JSONException e) {
            cicerone = null;
        }

        try {
            title = itinerary.getString(Columns.TITLE_KEY);
        } catch (JSONException e) {
            title = null;
        }

        try {
            description = itinerary.getString(Columns.DESCRIPTION_KEY);
        } catch (JSONException e) {
            description = null;
        }

        try {

            beginningDate = in.parse((itinerary.getString(Columns.BEGINNING_DATE_KEY)));
        } catch (JSONException | ParseException e) {
            beginningDate = null;
        }

        try {
            endingDate = in.parse((itinerary.getString(Columns.ENDING_DATE_KEY)));
        } catch (JSONException | ParseException e) {
            endingDate = null;
        }

        try {
            reservationDate = in.parse((itinerary.getString(Columns.END_RESERVATIONS_DATE_KEY)));
        } catch (JSONException | ParseException e) {
            reservationDate = null;
        }

        try {
            location = itinerary.getString(Columns.LOCATION_KEY);
        } catch (JSONException e) {
            location = null;
        }

        try {
            duration = itinerary.getString(Columns.DURATION_KEY);
        } catch (JSONException e) {
            duration = null;
        }

        try {
            repetitions = itinerary.getInt(Columns.REPETITIONS_PER_DAY_KEY);
        } catch (JSONException e) {
            repetitions = 0;
        }

        try {
            minParticipants = itinerary.getInt(Columns.MINIMUM_PARTICIPANTS_KEY);
        } catch (JSONException e) {
            minParticipants = 0;
        }
        try {
            maxParticipants = itinerary.getInt(Columns.MAXIMUM_PARTICIPANTS_KEY);
        } catch (JSONException e) {
            maxParticipants = 0;
        }

        try {
            fullPrice = Float.parseFloat(itinerary.getString(Columns.FULL_PRICE_KEY));
        } catch (JSONException e) {
            fullPrice = 0;
        }

        try {
            reducedPrice = Float.parseFloat(itinerary.getString(Columns.REDUCED_PRICE_KEY));
        } catch (JSONException e) {
            reducedPrice = 0;
        }
        try {
            imageUrl = itinerary.getString(Columns.IMG_URL_KEY);
        } catch (JSONException e) {
            imageUrl = null;
        }
    }

    /**
     * Get the itinerary's code.
     *
     * @return The itinerary's code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the itinerary's author.
     *
     * @return The itinerary's author.
     */
    public User getCicerone() {
        return cicerone;
    }

    /**
     * Set the itinerary's author.
     *
     * @param cicerone The new itinerary's author.
     */
    public void setCicerone(User cicerone) {
        this.cicerone = cicerone;
    }

    /**
     * Set the itinerary's code.
     *
     * @param code The new itinerary's code.
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Get the itinerary's title.
     *
     * @return The itinerary's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the itinerary's title.
     *
     * @param title The new itinerary's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the itinerary's description.
     *
     * @return The itinerary's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the itinerary's description.
     *
     * @param description The new itinerary's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the itinerary's beginning date.
     *
     * @return The itinerary's beginning date.
     */
    public Date getBeginningDate() {
        return beginningDate;
    }

    /**
     * Set the itinerary's beginning date.
     *
     * @param beginningDate The new itinerary's beginning date .
     */
    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    /**
     * Get the itinerary's ending date.
     *
     * @return The itinerary's ending date.
     */
    public Date getEndingDate() {
        return endingDate;
    }

    /**
     * Set the itinerary's endingDate.
     *
     * @param endingDate The new itinerary's ending date.
     */
    public void setEndingDate(Date endingDate) {
        this.endingDate = endingDate;
    }

    /**
     * Get the itinerary's askForReservation date.
     *
     * @return The itinerary's askForReservation date.
     */
    public Date getReservationDate() {
        return reservationDate;
    }

    /**
     * Set the itinerary's askForReservation Date.
     *
     * @param reservationDate The new itinerary's askForReservation date.
     */
    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * Get the itinerary's location.
     *
     * @return The itinerary's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the itinerary's location.
     *
     * @param location The new itinerary's location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the itinerary's minimum participants number.
     *
     * @return The itinerary's minimum participants number.
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Set the itinerary's minimum participants number.
     *
     * @param minParticipants The new itinerary's mininum participants number.
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Get the itinerary's maximum participants number.
     *
     * @return The itinerary's maximum partcipants number.
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Set the itinerary's maximum participants number.
     *
     * @param maxParticipants The new itinerary's maximum participants number.
     */
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    /**
     * Get the itinerary's repetitions per day.
     *
     * @return The itinerary's repetitions per day.
     */
    public int getRepetitions() {
        return repetitions;
    }

    /**
     * Set the itinerary's repetitions per day.
     *
     * @param repetitions The new itinerary's repetitions per day.
     */
    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    /**
     * Get  the itinerary's duration.
     *
     * @return The itinerary's duration.
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Set the itinerary's duration.
     *
     * @param duration The new itinerary's duration.
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }


    /**
     * Get  the itinerary's full price.
     *
     * @return The itinerary's full price.
     */
    public float getFullPrice() {
        return fullPrice;
    }

    /**
     * Set the itinerary's full price.
     *
     * @param fullPrice price The new itinerary's full price.
     */
    public void setFullPrice(float fullPrice) {
        this.fullPrice = fullPrice;
    }


    /**
     * Get  the itinerary's reduced price.
     *
     * @return The itinerary's reduced price.
     */
    public float getReducedPrice() {
        return reducedPrice;
    }

    /**
     * Set the itinerary's reduced price.
     *
     * @param reducedPrice price The new itinerary's reduced price.
     */
    public void setReducedPrice(float reducedPrice) {
        this.reducedPrice = reducedPrice;
    }


    /**
     * Get  the itinerary's image url.
     *
     * @return The itinerary's image url.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Set the itinerary's image url.
     *
     * @param imageUrl price The new itinerary's image url.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Convert the itinerary to a JSON Object.
     *
     * @return A JSON Object containing the data that were stored in the object.
     */
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            result.put(Columns.ITINERARY_CODE_KEY, this.code);
            result.put(Columns.TITLE_KEY, this.title);
            result.put(User.Columns.USERNAME_KEY, this.cicerone.toJSONObject());
            result.put(Columns.DESCRIPTION_KEY, this.description);
            result.put(Columns.BEGINNING_DATE_KEY, in.format(this.beginningDate));
            result.put(Columns.ENDING_DATE_KEY, in.format(this.endingDate));
            result.put(Columns.END_RESERVATIONS_DATE_KEY, in.format(this.reservationDate));
            result.put(Columns.DURATION_KEY, this.duration);
            result.put(Columns.LOCATION_KEY, this.location);
            result.put(Columns.MINIMUM_PARTICIPANTS_KEY, String.valueOf(this.minParticipants));
            result.put(Columns.MAXIMUM_PARTICIPANTS_KEY, String.valueOf(this.maxParticipants));
            result.put(Columns.REPETITIONS_PER_DAY_KEY, String.valueOf(this.repetitions));
            result.put(Columns.FULL_PRICE_KEY, this.fullPrice);
            result.put(Columns.REDUCED_PRICE_KEY, this.reducedPrice);
            result.put(Columns.IMG_URL_KEY, this.imageUrl);
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    private Itinerary(Builder builder) {
        code = 0;
        cicerone = builder.cicerone;
        title = builder.title;
        description = builder.description;
        beginningDate = builder.beginningDate;
        endingDate = builder.endingDate;
        reservationDate = builder.reservationDate;
        minParticipants = builder.minParticipants;
        maxParticipants = builder.maxParticipants;
        location = builder.location;
        repetitions = builder.repetitions;
        duration = builder.duration;
        fullPrice = builder.fullPrice;
        reducedPrice = builder.reducedPrice;
        imageUrl = builder.imageUrl;
    }

    public static class Builder {
        private final User cicerone; // TODO: Update class diagram
        private String title;
        private String description;
        private Date beginningDate;
        private Date endingDate;
        private Date reservationDate;
        private int minParticipants;
        private int maxParticipants;
        private String location;
        private int repetitions;
        private String duration;
        private float fullPrice;
        private float reducedPrice;
        private String imageUrl;

        public Builder(User cicerone) {
            this.cicerone = cicerone;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder beginningDate(Date beginningDate) {
            this.beginningDate = beginningDate;
            return this;
        }

        public Builder endingDate(Date endingDate) {
            this.endingDate = endingDate;
            return this;
        }

        public Builder reservationDate(Date reservationDate) {
            this.reservationDate = reservationDate;
            return this;
        }

        public Builder minParticipants(int minParticipants) {
            this.minParticipants = minParticipants;
            return this;
        }

        public Builder maxParticipants(int maxParticipants) {
            this.maxParticipants = maxParticipants;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder repetitions(int repetitions) {
            this.repetitions = repetitions;
            return this;
        }

        public Builder duration(String duration) {
            this.duration = duration;
            return this;
        }

        public Builder fullPrice(float fullPrice) {
            this.fullPrice = fullPrice;
            return this;
        }

        public Builder reducedPrice(float reducedPrice) {
            this.reducedPrice = reducedPrice;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Itinerary build() {
            return new Itinerary(this);
        }
    }

}

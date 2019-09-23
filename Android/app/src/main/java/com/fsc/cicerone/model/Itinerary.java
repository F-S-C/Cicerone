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

/**
 * An <i>entity</i> class that stores the data of an itinerary.
 */
public class Itinerary extends BusinessEntity {

    private int code;
    private User cicerone;
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
     * Create a new Itinerary object based on a JSON Object.
     * The JSON Object <i>must</i> contain <i>at least</i> the following values:
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
            code = itinerary.getInt("itinerary_code");
        } catch (JSONException e) {
            code = -1;
        }

        try {
            cicerone = new User(itinerary.getJSONObject("username"));
        } catch (JSONException e) {
            cicerone = null;
        }

        try {
            title = itinerary.getString("title");
        } catch (JSONException e) {
            title = null;
        }

        try {
            description = itinerary.getString("description");
        } catch (JSONException e) {
            description = null;
        }

        try {

            beginningDate = in.parse((itinerary.getString("beginning_date")));
        } catch (JSONException | ParseException e) {
            beginningDate = null;
        }

        try {
            endingDate = in.parse((itinerary.getString("ending_date")));
        } catch (JSONException | ParseException e) {
            endingDate = null;
        }

        try {
            reservationDate = in.parse((itinerary.getString("end_reservations_date")));
        } catch (JSONException | ParseException e) {
            reservationDate = null;
        }

        try {
            location = itinerary.getString("location");
        } catch (JSONException e) {
            location = null;
        }

        try {
            duration = itinerary.getString("duration");
        } catch (JSONException e) {
            duration = null;
        }

        try {
            repetitions = itinerary.getInt("repetitions_per_day");
        } catch (JSONException e) {
            repetitions = 0;
        }

        try {
            minParticipants = itinerary.getInt("minimum_participants_number");
        } catch (JSONException e) {
            minParticipants = 0;
        }
        try {
            maxParticipants = itinerary.getInt("maximum_participants_number");
        } catch (JSONException e) {
            maxParticipants = 0;
        }

        try {
            fullPrice = Float.parseFloat(itinerary.getString("full_price"));
        } catch (JSONException e) {
            fullPrice = 0;
        }

        try {
            reducedPrice = Float.parseFloat(itinerary.getString("reduced_price"));
        } catch (JSONException e) {
            reducedPrice = 0;
        }
        try {
            imageUrl = itinerary.getString("image_url");
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
            result.put("itinerary_code", this.code);
            result.put("title", this.title);
            result.put("username", this.cicerone.toJSONObject());
            result.put("description", this.description);
            result.put("beginning_date", in.format(this.beginningDate));
            result.put("ending_date", in.format(this.endingDate));
            result.put("end_reservations_date", in.format(this.reservationDate));
            result.put("duration", this.duration);
            result.put("location", this.location);
            result.put("minimum_participants_number", String.valueOf(this.minParticipants));
            result.put("maximum_participants_number", String.valueOf(this.maxParticipants));
            result.put("repetitions_per_day", String.valueOf(this.repetitions));
            result.put("full_price", this.fullPrice);
            result.put("reduced_price", this.reducedPrice);
            result.put("image_url", this.imageUrl);
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

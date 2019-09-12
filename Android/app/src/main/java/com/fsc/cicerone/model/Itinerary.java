package com.fsc.cicerone.model;

import com.fsc.cicerone.model.BusinessEntity;

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

    private int itineraryCode;
    private String username;
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
     *
     * @param title The title of the itinerary.
     * @param description The description of the itinerary.
     * @param beginningDate  The beginning date of the itinerary.
     * @param endingDate  The ending date of the itinerary.
     * @param reducedPrice The reservation date of the itinerary.
     * @param location The location of the itinerary.
     * @param duration The duration of the itinerary.
     * @param repetitions The repetitions per day of the itinerayr.
     * @param minParticipants The mininum participanrs of the itinerary.
     * @param maxParticipants The maximum participants of the itinerary.
     * @param fullPrice The full price of the itinerary per globetrotter.
     * @param reducedPrice The reduced price of the itinerary per globetrotter.
     * @param imageUrl The image url of the itinerary.
     * @return
     */
    public Itinerary(String username, String title, String description, String beginningDate, String endingDate, String reservationDate, int minParticipants, int maxParticipants, String location, int repetitions, String duration, float fullPrice, float reducedPrice, String imageUrl) {
        this.itineraryCode = -1;
        this.username = username;
        this.title = title;
        this.description = description;
        try {
            this.beginningDate = in.parse(beginningDate);
        } catch (ParseException e) {
            this.beginningDate = new Date();
        }
        try {
            this.endingDate = in.parse(endingDate);
        } catch (ParseException e) {
            this.endingDate = new Date();
        }
        try {
            this.reservationDate = in.parse(reservationDate);
        } catch (ParseException e) {
            this.reservationDate = new Date();
        }
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.location = location;
        this.repetitions = repetitions;
        this.duration = duration;
        this.fullPrice = fullPrice;
        this.reducedPrice = reducedPrice;
        this.imageUrl = imageUrl;
    }

    /**
     * Default empty constructor.
     */
    public Itinerary() {
        // Automatically set everything to a default value
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
     * @param itinerary The JSON object from which data will be fetched.
     */
    public Itinerary(JSONObject itinerary) {
        try {
            itineraryCode = itinerary.getInt("itinerary_code");
        } catch (JSONException e) {
            itineraryCode = -1;
        }

        try {
            username = itinerary.getString("username");
        } catch (JSONException e) {
            username = null;
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
        return itineraryCode;
    }

    /**
     * Set the itinerary's code
     *
     * @param code The new itinerary's code.
     */
    public void setName(int code) {
        this.itineraryCode = code;
    }

    /**
     * Get the itinerary's author.
     *
     * @return The itinerary's author.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the itinerary's author.
     *
     * @param username The new itinerary's author.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the itinerary's code.
     *
     * @return The itinerary's code.
     */
    public int getItineraryCode() {
        return itineraryCode;
    }

    /**
     * Set the itinerary's code.
     *
     * @param itineraryCode The new itinerary's code.
     */
    public void setItineraryCode(int itineraryCode) {
        this.itineraryCode = itineraryCode;
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
            result.put("itinerary_code", this.itineraryCode);
            result.put("title", this.title);
            result.put("username", this.username);
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


}

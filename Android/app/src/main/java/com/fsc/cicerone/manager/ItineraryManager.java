package com.fsc.cicerone.manager;

import android.util.Log;

import com.fsc.cicerone.model.Itinerary;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;

/**
 * A <i>control</i> class that manages the itineraries.
 */
public abstract class ItineraryManager {

    private ItineraryManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create and upload a new itinerary to the server.
     *
     * @param title       The title of the new itinerary.
     * @param description The description of the itinerary.
     * @param bDate       The beginning date of the itinerary (format "yyyy-MM-dd").
     * @param eDate       The ending date of the itinerary (format "yyyy-MM-dd").
     * @param rDate       The reservation's ending date of the itinerary (format "yyyy-MM-dd").
     * @param location    The location of the itinerary.
     * @param duration    The duration of the itinerary.
     * @param repetitions The number of repetitions per day if the itinerary.
     * @param minP        The minimum number of participants.
     * @param maxP        The maximum number of participants.
     * @param fPrice      The full price of the itinerary.
     * @param rPrice      The reduced price of the itinerary.
     * @param url         The URL of the image of the itinerary.
     * @return The new itinerary.
     */
    public static Itinerary uploadItinerary(String title, String description, String bDate, String eDate, String rDate, String location, String duration, int repetitions, int minP, int maxP, float fPrice, float rPrice, String url, AccountManager.BooleanRunnable result) {
        SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date beginningDate;
        Date endingDate;
        Date reservationDate;
        try {
            beginningDate = in.parse(bDate);
        } catch (ParseException e) {
            beginningDate = new Date();
        }
        try {
            endingDate = in.parse(eDate);
        } catch (ParseException e) {
            endingDate = new Date();
        }
        try {
            reservationDate = in.parse(rDate);
        } catch (ParseException e) {
            reservationDate = new Date();
        }

        Itinerary itinerary = new Itinerary.Builder(AccountManager.getCurrentLoggedUser())
                .title(title)
                .description(description)
                .beginningDate(beginningDate)
                .endingDate(endingDate)
                .reservationDate(reservationDate)
                .minParticipants(minP)
                .maxParticipants(maxP)
                .location(location)
                .repetitions(repetitions)
                .duration(duration)
                .fullPrice(fPrice)
                .reducedPrice(rPrice)
                .imageUrl(url)
                .build();

        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_ITINERARY,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult booleanResult) throws JSONException {
                        Log.d("uploadItinerary", booleanResult.getResult() + ": " + booleanResult.getMessage());
                        result.accept(booleanResult.getResult());
                    }
                },
                itinerary.toJSONObject());

        connector.execute();
        return itinerary;
    }


}

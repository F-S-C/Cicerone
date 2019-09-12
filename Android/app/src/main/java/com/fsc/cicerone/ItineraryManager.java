package com.fsc.cicerone;

import android.util.Log;

import com.fsc.cicerone.model.Itinerary;

import org.json.JSONArray;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * A <i>control</i> class that manages the itineraries.
 */
public abstract class ItineraryManager {


    /**
     * @param title       The title of the itinerary.
     * @param description The description of the itinerary.
     * @param bDate       The beginning date of the itinerary.
     * @param eDate       The ending date of the itinerary.
     * @param rDate       The reservation date of the itinerary.
     * @param location    The location of the itinerary.
     * @param duration    The duration of the itinerary.
     * @param repetitions The repetitions per day of the itinerayr.
     * @param minP        The mininum participanrs of the itinerary.
     * @param maxP        The maximum participants of the itinerary.
     * @param fPrice      The full price of the itinerary per globetrotter.
     * @param rPrice      The reduced price of the itinerary per globetrotter.
     * @param url         The image url of the itinerary.
     * @return
     */
    public static Itinerary uploadItinerary(String title, String description, String bDate, String eDate, String rDate, String location, String duration, int repetitions, int minP, int maxP, float fPrice, float rPrice, String url) {
        Itinerary itinerary = new Itinerary(AccountManager.getCurrentLoggedUser().getUsername(), title, description, bDate, eDate, rDate, minP, maxP, location, repetitions, duration, fPrice, rPrice, url);
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_ITINERARY,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        Log.d("uploadItinerary", String.valueOf(result.getResult()));
                    }
                },
                itinerary.toJSONObject());

        connector.execute();
        return itinerary;
    }


}

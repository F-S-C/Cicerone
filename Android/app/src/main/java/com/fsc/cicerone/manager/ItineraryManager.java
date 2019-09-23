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

package com.fsc.cicerone.manager;

import android.app.Activity;

import com.fsc.cicerone.model.Itinerary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

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
     * @param beginDate       The beginning date of the itinerary (format "yyyy-MM-dd").
     * @param endDate       The ending date of the itinerary (format "yyyy-MM-dd").
     * @param endReservationDate       The reservation's ending date of the itinerary (format "yyyy-MM-dd").
     * @param location    The location of the itinerary.
     * @param duration    The duration of the itinerary.
     * @param repetitions The number of repetitions per day if the itinerary.
     * @param minParticipants        The minimum number of participants.
     * @param maxParticipants        The maximum number of participants.
     * @param fullPrice      The full price of the itinerary.
     * @param reducedPrice      The reduced price of the itinerary.
     * @param imageUrl         The URL of the image of the itinerary.
     * @return The new itinerary.
     */

    public static Itinerary uploadItinerary(String title, String description, String beginDate, String endDate, String endReservationDate, String location, String duration, int repetitions, int minParticipants, int maxParticipants, float fullPrice, float reducedPrice, String imageUrl, BooleanConnector.OnEndConnectionListener callback) {
        SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date beginningDate;
        Date endingDate;
        Date reservationDate;
        try {
            beginningDate = in.parse(beginDate);
        } catch (ParseException e) {
            beginningDate = new Date();
        }
        try {
            endingDate = in.parse(endDate);
        } catch (ParseException e) {
            endingDate = new Date();
        }
        try {
            reservationDate = in.parse(endReservationDate);
        } catch (ParseException e) {
            reservationDate = new Date();
        }

        Itinerary itinerary = new Itinerary.Builder(AccountManager.getCurrentLoggedUser())
                .title(title)
                .description(description)
                .beginningDate(beginningDate)
                .endingDate(endingDate)
                .reservationDate(reservationDate)
                .minParticipants(minParticipants)
                .maxParticipants(maxParticipants)
                .location(location)
                .repetitions(repetitions)
                .duration(duration)
                .fullPrice(fullPrice)
                .reducedPrice(reducedPrice)
                .imageUrl(imageUrl)
                .build();

        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_ITINERARY)
                .setContext(null)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(SendInPostConnector.paramsFromObject(itinerary))
                .build();

        connector.execute();
        return itinerary;
    }

    public static void deleteItinerary(Activity context, int code){
        Map<String, Object> params = new HashMap<>(1);
        params.put("itinerary_code", code);
        new BooleanConnector.Builder(ConnectorConstants.DELETE_ITINERARY)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) context.finish();
                })
                .setObjectToSend(params)
                .build()
                .execute();
    }

}

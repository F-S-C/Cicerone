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
import android.util.Log;

import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.DatabaseConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.functional_interfaces.Consumer;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
     * @param title              The title of the new itinerary.
     * @param description        The description of the itinerary.
     * @param beginDate          The beginning date of the itinerary (format "yyyy-MM-dd").
     * @param endDate            The ending date of the itinerary (format "yyyy-MM-dd").
     * @param endReservationDate The reservation's ending date of the itinerary (format "yyyy-MM-dd").
     * @param location           The location of the itinerary.
     * @param duration           The duration of the itinerary.
     * @param repetitions        The number of repetitions per day if the itinerary.
     * @param minParticipants    The minimum number of participants.
     * @param maxParticipants    The maximum number of participants.
     * @param fullPrice          The full price of the itinerary.
     * @param reducedPrice       The reduced price of the itinerary.
     * @param imageUrl           The URL of the image of the itinerary.
     * @param callback           A callback to be executed after the operation is completed.
     * @return The new itinerary.
     */

    public static Itinerary uploadItinerary(String title, String description, String beginDate, String endDate, String endReservationDate, String location, String duration, int repetitions, int minParticipants, int maxParticipants, float fullPrice, float reducedPrice, String imageUrl, @Nullable BooleanConnector.OnEndConnectionListener callback) {
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

    /**
     * Delete an itinerary from the server.
     *
     * @param context   The context of the activity
     * @param itinerary The code of the itinerary to delete.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void deleteItinerary(Activity context, Itinerary itinerary, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("itinerary_code", itinerary.getCode());
        new BooleanConnector.Builder(ConnectorConstants.DELETE_ITINERARY)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(params)
                .build()
                .execute();
    }

    /**
     * Update an itinerary with new information
     *
     * @param context   The context of the activity.
     * @param itinerary The itinerary to update
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void updateItinerary(Activity context, Itinerary itinerary, @Nullable Consumer<Boolean> callback) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_ITINERARY)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                    Log.e("result", String.valueOf(result.getResult()));
                    Log.e("message", result.getMessage());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(itinerary))
                .build();
        connector.execute();
    }


    /**
     * Request itinerary.
     *
     * @param context                   The context of the caller.
     * @param parameters                The parameters of the request.
     * @param onStartConnectionListener On start connection callback.
     * @param callback                  A callback to be executed after the operation is completed.
     */
    public static void requestItinerary(Activity context, Map<String, Object> parameters, @Nullable DatabaseConnector.OnStartConnectionListener onStartConnectionListener, @Nullable DatabaseConnector.OnEndConnectionListener<Itinerary> callback) {
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY, BusinessEntityBuilder.getFactory(Itinerary.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

}

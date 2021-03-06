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

import com.fsc.cicerone.app_connector.AsyncDatabaseConnector;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.functional_interfaces.Consumer;
import com.fsc.cicerone.mailer.Mailer;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.notifications.Config;
import com.fsc.cicerone.notifications.NotificationUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A class that provides useful functions for the management of the reservations.
 */
public class ReservationManager {

    private ReservationManager() {
        throw new IllegalStateException("Utility class");
    }

    private static final String DELETED_BY_CICERONE_KEY = "deleted_by_cicerone";

    public static void getReservations(Activity context, Itinerary itinerary, @Nullable AsyncDatabaseConnector.OnEndConnectionListener<Reservation> callback) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put(Reservation.Columns.BOOKED_ITINERARY_KEY, itinerary.getCode());

        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(null)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .getData();
    }

    /**
     * Remove a reservation from an itinerary. This operation is performed by a globetrotter.
     *
     * @param itinerary The itinerary from which the reservation will be removed.
     */
    public static void removeReservation(Itinerary itinerary) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary).build();
        deleteReservationFromServer(reservation, v -> {
            if (v.getResult()) {
                Mailer.sendReservationRemoveEmail(null, itinerary, null);
            }
        });
    }

    /**
     * Add a reservation's request to an itinerary. This operation is performed by a globetrotter.
     *
     * @param itinerary        The itinerary to which the request will be added.
     * @param numberOfAdults   The number of adults of the reservation.
     * @param numberOfChildren The number of children of the reservation.
     * @param requestedDate    The requested date of the reservation.
     * @return The new reservation.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static Reservation addReservation(Activity context, Itinerary itinerary, int numberOfAdults, int numberOfChildren, Date requestedDate) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary)
                .numberOfAdults(numberOfAdults)
                .numberOfChildren(numberOfChildren)
                .requestedDate(requestedDate)
                .forwardingDate(new Date())
                .build();

        new BooleanConnector.Builder(ConnectorConstants.INSERT_RESERVATION)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (!result.getResult())
                        Log.e("INSERT_RESERVATION_ERR", result.getMessage());
                    else {
                        NotificationUtils.subscribeToTopic(context, Config.TOPIC_ITINERARY(itinerary));
                        Mailer.sendItineraryRequestEmail(context, reservation, null);
                    }
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(reservation))
                .build()
                .getData();

        return reservation;
    }

    /**
     * Confirm a reservation's request and send an email notification to the globetrotter. This
     * action is performed by a cicerone.
     *
     * @param reservation The reservation to be confirmed.
     */
    public static void confirmReservation(Reservation reservation) {
        reservation.setConfirmationDate(new Date());
        new BooleanConnector.Builder(ConnectorConstants.UPDATE_RESERVATION)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.d("CONFIRM_RESERVATION", result.getResult() + ": " + result.getMessage());
                    Mailer.sendReservationConfirmationEmail(null, reservation, null);
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(reservation))
                .build()
                .getData();
    }

    /**
     * Remove a reservation's request.
     *
     * @param reservation The reservation to be refused.
     * @param context     The application context.
     */
    public static void refuseReservation(Reservation reservation, Activity context) {
        Map<String, Object> additionalParams = new HashMap<>(1);
        additionalParams.put(DELETED_BY_CICERONE_KEY, true);
        deleteReservationFromServer(reservation, additionalParams, v -> {
            if (v.getResult()) {
                Mailer.sendReservationRefuseEmail(context, reservation, null);
            }
        });
    }

    /**
     * Remove a reservation's request.
     *
     * @param reservation The reservation to be deleted.
     */
    public static void deleteReservation(Reservation reservation) {
        deleteReservationFromServer(reservation, null);
    }

    /**
     * Delete a reservation from the server.
     *
     * @param reservation The reservation.
     * @param callback    A callback to be executed after the operation is completed.
     */
    private static void deleteReservationFromServer(Reservation reservation, @Nullable BooleanConnector.OnEndConnectionListener callback) {
        Map<String, Object> additionalParams = new HashMap<>(1);
        additionalParams.put(DELETED_BY_CICERONE_KEY, false);
        deleteReservationFromServer(reservation, additionalParams, callback);
    }

    /**
     * Delete a reservation from the server.
     *
     * @param reservation          The reservation.
     * @param additionalParameters A map of additional parameters that will be added to the
     *                             reservation. Note that if a key is duplicated between the
     *                             reservation and the additional parameters, the latter overrides
     *                             the first.
     * @param callback             A callback to be executed after the operation is completed.
     */
    private static void deleteReservationFromServer(Reservation reservation, @Nullable Map<String, Object> additionalParameters, @Nullable BooleanConnector.OnEndConnectionListener callback) {
        Map<String, Object> params = SendInPostConnector.paramsFromObject(reservation);
        if (additionalParameters != null) params.putAll(additionalParameters);

        new BooleanConnector.Builder(ConnectorConstants.DELETE_RESERVATION)
                .setContext(null)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(params)
                .build()
                .getData();
    }

    /**
     * Check if an itinerary is already reserved.
     *
     * @param context   The context of the caller.
     * @param itinerary The itinerary of the reservation.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void isReserved(Activity context, Itinerary itinerary, @Nullable Consumer<Boolean> callback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> params = new HashMap<>(2);
            params.put(User.Columns.USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());
            params.put(Reservation.Columns.BOOKED_ITINERARY_KEY, itinerary.getCode());

            new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                    .setContext(context)
                    .setOnEndConnectionListener(list -> {
                        if (callback != null) callback.accept(!list.isEmpty());
                    })
                    .setObjectToSend(params)
                    .build()
                    .getData();
        }
    }

    /**
     * Check if an reservation is already confirmed.
     *
     * @param context   The context of the caller.
     * @param itinerary The itinerary of the reservation.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void isConfirmed(Activity context, Itinerary itinerary, @Nullable Consumer<Boolean> callback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> params = new HashMap<>(2);
            params.put(User.Columns.USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());
            params.put(Reservation.Columns.BOOKED_ITINERARY_KEY, itinerary.getCode());

            new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                    .setContext(context)
                    .setOnEndConnectionListener(list -> {
                        if (callback != null) callback.accept(list.get(0).isConfirmed());
                    })
                    .setObjectToSend(params)
                    .build()
                    .getData();
        }
    }

    /**
     * Get investments'list of the user.
     *
     * @param context    The context of the caller.
     * @param parameters The user of the investments'list.
     * @param callback   A callback to be executed after the operation is completed.
     */
    public static void getListInvestments(Activity context, Map<String, Object> parameters, @Nullable AsyncDatabaseConnector.OnStartConnectionListener onStartConnectionListener, @Nullable AsyncDatabaseConnector.OnEndConnectionListener<Reservation> callback) {

        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .getData();
    }
}

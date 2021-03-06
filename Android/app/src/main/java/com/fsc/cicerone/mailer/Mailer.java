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

package com.fsc.cicerone.mailer;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.functional_interfaces.Consumer;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to send a request to send e-mail to the remote connector.
 */
public class Mailer {
    private static final String RECIPIENT_EMAIL_KEY = "recipient_email";
    private static final String ITINERARY_CODE_KEY = "itinerary_code";
    private static final String USERNAME_KEY = "username";

    private Mailer() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Send the reservation confirmation e-mail.
     *
     * @param context     The activity context.
     * @param reservation The confirmed reservation.
     * @param callback    A callback to be called after the e-mail has been sent.
     */
    public static void sendReservationConfirmationEmail(Activity context, Reservation reservation, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(4);
        data.put(USERNAME_KEY, reservation.getItinerary().getCicerone().getUsername());
        data.put(ITINERARY_CODE_KEY, reservation.getItinerary().getCode());
        data.put(RECIPIENT_EMAIL_KEY, reservation.getClient().getEmail());
        data.put("type", "reservationConfirmation");
        sendEmail(context, data, ConnectorConstants.EMAIL_SENDER, callback);
    }

    /**
     * Send the registration confirmation e-mail.
     *
     * @param context  The activity context.
     * @param user     The registered user.
     * @param callback A callback to be called after the e-mail has been sent.
     */
    public static void sendRegistrationConfirmationEmail(Activity context, User user, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(3);
        data.put(USERNAME_KEY, user.getUsername());
        data.put(RECIPIENT_EMAIL_KEY, user.getEmail());
        data.put("type", "registrationConfirmed");
        sendEmail(context, data, ConnectorConstants.EMAIL_SENDER, callback);
    }

    /**
     * Send an e-mail for the new reservation request.
     *
     * @param context     The activity context.
     * @param reservation The requested reservation.
     * @param callback    A callback to be called after the e-mail has been sent.
     */
    public static void sendItineraryRequestEmail(Activity context, Reservation reservation, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(4);
        data.put(USERNAME_KEY, reservation.getItinerary().getCicerone().getUsername());
        data.put(ITINERARY_CODE_KEY, reservation.getItinerary().getCode());
        data.put(RECIPIENT_EMAIL_KEY, reservation.getItinerary().getCicerone().getEmail());
        data.put("type", "newItineraryRequest");
        sendEmail(context, data, ConnectorConstants.EMAIL_SENDER, callback);
    }

    /**
     * Send the reservation refuse e-mail.
     *
     * @param context     The activity context.
     * @param reservation The refused reservation.
     * @param callback    A callback to be called after the e-mail has been sent.
     */
    public static void sendReservationRefuseEmail(Activity context, Reservation reservation, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(5);
        data.put(USERNAME_KEY, reservation.getItinerary().getCicerone().getUsername());
        data.put(ITINERARY_CODE_KEY, reservation.getItinerary().getCode());
        data.put(RECIPIENT_EMAIL_KEY, reservation.getClient().getEmail());
        data.put("cicerone_email", AccountManager.getCurrentLoggedUser().getEmail());
        data.put("type", "reservationRefuse");
        sendEmail(context, data, ConnectorConstants.EMAIL_SENDER, callback);
    }

    /**
     * Send the reservation remove e-mail.
     *
     * @param context   The activity context.
     * @param itinerary The itinerary where the reservation was removed.
     * @param callback  A callback to be called after the e-mail has been sent.
     */
    public static void sendReservationRemoveEmail(Activity context, Itinerary itinerary, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(7);
        data.put(USERNAME_KEY, itinerary.getCicerone().getUsername());
        data.put(ITINERARY_CODE_KEY, itinerary.getCode());
        data.put(RECIPIENT_EMAIL_KEY, itinerary.getCicerone().getEmail());
        data.put("globetrotter_email", AccountManager.getCurrentLoggedUser().getEmail());
        data.put("globetrotter_name", AccountManager.getCurrentLoggedUser().getName());
        data.put("globetrotter_surname", AccountManager.getCurrentLoggedUser().getSurname());
        data.put("type", "reservationRemove");
        sendEmail(context, data, ConnectorConstants.EMAIL_SENDER, callback);
    }

    /**
     * Send the account delete confirmation e-mail.
     *
     * @param callback A callback to be called after the e-mail has been sent.
     */
    public static void sendAccountDeleteConfirmationEmail(@Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(3);
        data.put(USERNAME_KEY, AccountManager.getCurrentLoggedUser().getName());
        data.put(RECIPIENT_EMAIL_KEY, AccountManager.getCurrentLoggedUser().getEmail());
        data.put("type", "accountDeleted");
        sendEmail(null, data, ConnectorConstants.EMAIL_SENDER, callback);
    }

    /**
     * Send the e-mail that contains the link for reset the user's password.
     *
     * @param context  The activity context.
     * @param email    The recipient e-mail.
     * @param callback A callback to be called after the e-mail has been sent.
     */
    public static void sendPasswordResetLink(Activity context, String email, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> data = new HashMap<>(1);
        data.put("email", email);
        sendEmail(context, data, ConnectorConstants.EMAIL_RESET_PASSWORD, callback);
    }

    /**
     * Send a request with data to a remote connector.
     *
     * @param context  The activity context.
     * @param data     The data to be sent.
     * @param url      The connector url.
     * @param callback A callback to be called after the e-mail has been sent.
     */
    private static void sendEmail(Activity context, Map<String, Object> data, String url, @Nullable Consumer<Boolean> callback) {
        new BooleanConnector.Builder(url)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                    if (result.getResult()) {
                        Log.i("SEND OK", "true");
                    } else {
                        Log.e("SEND ERROR", result.getMessage());
                    }
                })
                .setObjectToSend(data)
                .build()
                .getData();
    }
}

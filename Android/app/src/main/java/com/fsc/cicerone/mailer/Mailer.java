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

import com.fsc.cicerone.functional_interfaces.BooleanRunnable;
import com.fsc.cicerone.model.Reservation;

import java.util.HashMap;
import java.util.Map;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;

public class Mailer {

    private Mailer() {
        throw new IllegalStateException("Utility class");
    }

    public static void sendReservationConfirmationEmail(Activity context, Reservation reservation, @Nullable BooleanRunnable callback) {
        Map<String, Object> data = new HashMap<>(3);
        data.put("username", reservation.getItinerary().getCicerone().getUsername());
        data.put("itinerary_code", reservation.getItinerary().getCode());
        data.put("recipient_email", reservation.getClient().getEmail());
        sendEmail(context, data, callback);
    }

    private static void sendEmail(Activity context, Map<String, Object> data, @Nullable BooleanRunnable callback) {
        BooleanConnector sendEmailConnector = new BooleanConnector.Builder(ConnectorConstants.EMAIL_SENDER)
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
                .build();
        sendEmailConnector.execute();
    }
}

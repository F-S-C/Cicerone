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
    public enum EmailKind {

    }

    public static void sendReservationConfirmationEmail(Activity context, Reservation reservation, @Nullable BooleanRunnable callback){
        Map<String, Object> data = new HashMap<>(3);
        data.put("username", reservation.getItinerary().getCicerone().getUsername());
        data.put("itinerary_code", reservation.getItinerary().getCode());
        data.put("recipient_email", reservation.getClient().getEmail());
        sendEmail(context, data, callback);
    }

    private static void sendEmail(Activity context, Map<String, Object> data, @Nullable BooleanRunnable callback)
    {
        BooleanConnector sendEmailConnector = new BooleanConnector.Builder(ConnectorConstants.EMAIL_SENDER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if(callback != null) callback.accept(result.getResult());
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

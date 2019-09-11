package com.fsc.cicerone;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public abstract class ReservationManager {

    private interface RunnableUsingJSONArray {
        void run(JSONArray jsonArray) throws JSONException;
    }

    private ReservationManager(){
        throw new IllegalStateException("Utility class");
    }

    public static void removeReservation(Itinerary itinerary) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary).build();
        deleteReservationFromServer(reservation, jsonArray -> {
            // TODO: Check and send email to cicerone (IF-34)
        });
    }

    public static Reservation addReservation(Itinerary itinerary, int numberOfAdults, int numberOfChildren, Date requestedDate, Date forwardingDate) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary)
                .numberOfAdults(numberOfAdults)
                .numberOfChildren(numberOfChildren)
                .requestedDate(requestedDate)
                .forwardingDate(forwardingDate)
                .build();

        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_RESERVATION, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                if (!jsonArray.getJSONObject(0).getBoolean("result"))
                    Log.e("INSERT_RESERVATION_ERR", jsonArray.getJSONObject(0).getString("error"));
            }
        }, reservation.toJSONObject());
        connector.execute();

        return reservation;
    }

    public static void confirmReservation(Reservation reservation) {
        reservation.setConfirmationDate(new Date());
        AccountManager.sendEmailWithContacts(reservation.getItinerary(),reservation.getClient(), (result) -> {
            if(result) {
                SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.UPDATE_RESERVATION);
                connector.setObjectToSend(reservation.toJSONObject());
                connector.execute();
            }
        });
    }

    public static void refuseReservation(Reservation reservation) {
        deleteReservationFromServer(reservation, jsonArray -> {
            // TODO: Check and send email to globetrotter (IF-34)
        });
        // Garbage collector has to destroy 'reservation'.
    }

    private static void deleteReservationFromServer(Reservation reservation, RunnableUsingJSONArray callback){
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_RESERVATION, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                callback.run(jsonArray);
            }
        });
        connector.setObjectToSend(reservation.toJSONObject());
        connector.execute();
    }
}

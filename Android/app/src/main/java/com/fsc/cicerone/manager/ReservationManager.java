package com.fsc.cicerone.manager;

import android.util.Log;

import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;

public abstract class ReservationManager {

    private interface RunnableUsingJSONArray {
        void run(JSONArray jsonArray) throws JSONException;
    }

    private ReservationManager() {
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

        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_RESERVATION,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if (!result.getResult())
                            Log.e("INSERT_RESERVATION_ERR", result.getMessage());
                    }
                },
                reservation.toJSONObject());
        connector.execute();

        return reservation;
    }

    public static void confirmReservation(Reservation reservation) {
        reservation.setConfirmationDate(new Date());
        AccountManager.sendEmailWithContacts(reservation.getItinerary(), reservation.getClient(), result -> {
            if (result) {
                BooleanConnector connector = new BooleanConnector(ConnectorConstants.UPDATE_RESERVATION);
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

    private static void deleteReservationFromServer(Reservation reservation, RunnableUsingJSONArray callback) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.DELETE_RESERVATION,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        callback.run(new JSONArray("[" + result.toJSONObject().toString() + "]"));
                    }
                },
                reservation.toJSONObject());
        connector.execute();
    }
}

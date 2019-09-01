package com.fsc.cicerone;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public abstract class ReservationManager {

    public static void removeReservation(Itinerary itinerary){
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary).build();
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_RESERVATION, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                // TODO: Complete
            }
        }, reservation.toJSONObject());
        connector.execute();
    }

    public static Reservation addReservation(Itinerary itinerary, int numberOfAdults, int numberOfChildren, Date requestedDate, Date forwardingDate) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary)
                .numberOfAudults(numberOfAdults)
                .numberOfChildren(numberOfChildren)
                .requestedDate(requestedDate)
                .forwadingDate(forwardingDate)
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
}

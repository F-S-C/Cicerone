package com.fsc.cicerone.manager;

import android.util.Log;

import androidx.annotation.Nullable;

import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;

import java.util.Date;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * The manager class for the Reservation data-type.
 */
public abstract class ReservationManager {

    private interface RunnableUsingBooleanResult {
        void run(BooleanConnector.BooleanResult result);
    }

    private ReservationManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Remove a reservation from an itinerary. This operation is performed by a globetrotter.
     *
     * @param itinerary The itinerary from which the reservation will be removed.
     */
    public static void removeReservation(Itinerary itinerary) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary).build();
        deleteReservationFromServer(reservation, null); // TODO: Check and send email to cicerone (IF-34)?
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
    public static Reservation addReservation(Itinerary itinerary, int numberOfAdults, int numberOfChildren, Date requestedDate) {
        Reservation reservation = new Reservation.Builder(AccountManager.getCurrentLoggedUser(), itinerary)
                .numberOfAdults(numberOfAdults)
                .numberOfChildren(numberOfChildren)
                .requestedDate(requestedDate)
                .forwardingDate(new Date())
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
                            Log.e("INSERT_RESERVATION_ERR", result.getMessage()); //TODO: Send email to cicerone (IF-34)?
                    }
                },
                SendInPostConnector.paramsFromJSONObject(reservation.toJSONObject()));
        connector.execute();

        return reservation;
    }

    /**
     * Confirm a reservation's request and send an email notification to the globetrotter.
     * This action is performed by a cicerone.
     *
     * @param reservation The reservation to be confirmed.
     */
    public static void confirmReservation(Reservation reservation) {
        reservation.setConfirmationDate(new Date());
        AccountManager.sendEmailWithContacts(reservation.getItinerary(), reservation.getClient(), result -> {
            if (result) {
                BooleanConnector connector = new BooleanConnector(ConnectorConstants.UPDATE_RESERVATION);
                connector.setObjectToSend(SendInPostConnector.paramsFromJSONObject(reservation.toJSONObject()));
                connector.execute();
            }
        });
    }

    /**
     * Refuse a reservation's request. This action is performed by a cicerone.
     * @param reservation The reservation to be refused.
     */
    public static void refuseReservation(Reservation reservation) {
        deleteReservationFromServer(reservation, null); // TODO: Check and send email to globetrotter (IF-34)?
        // Garbage collector has to destroy 'reservation'.
    }

    /**
     * Delete a reservation from the server.
     * @param reservation The reservation.
     * @param callback A callback to be executed after the operation is completed.
     */
    private static void deleteReservationFromServer(Reservation reservation, @Nullable RunnableUsingBooleanResult callback) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.DELETE_RESERVATION,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if(callback != null) callback.run(result);
                    }
                },
                SendInPostConnector.paramsFromJSONObject(reservation.toJSONObject()));
        connector.execute();
    }
}
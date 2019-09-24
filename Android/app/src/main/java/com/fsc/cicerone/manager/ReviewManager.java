package com.fsc.cicerone.manager;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.DatabaseConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.functional_interfaces.BooleanRunnable;
import com.fsc.cicerone.functional_interfaces.FloatRunnable;
import com.fsc.cicerone.functional_interfaces.RunnableUsingBusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ReviewManager {

    private ReviewManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Set the feedback's average of the itinerary.
     *
     * @param context   The context of the caller.
     * @param itinerary The itinerary of the feedback's average.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void getAvgItineraryFeedback(Activity context, Itinerary itinerary, @Nullable FloatRunnable callback) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("reviewed_itinerary", itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    int sum = 0;
                    for (ItineraryReview review : list) {
                        sum += review.getFeedback();
                    }
                    if (callback != null)
                        callback.accept(!list.isEmpty() ? (float) sum / list.size() : 0);
                })
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

    /**
     * Check if user can review itinerary.
     *
     * @param context                   The context of the caller.
     * @param user                      The user reviewer.
     * @param itinerary                 The itinerary reviewed.
     * @param onStartConnectionListener On start connection callback.
     * @param callback                  A callback to be executed after the operation is completed.
     */
    public static void permissionReviewItinerary(Activity context, User user, Itinerary itinerary, @Nullable BooleanRunnable callback, @Nullable DatabaseConnector.OnStartConnectionListener onStartConnectionListener) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("booked_itinerary", itinerary.getCode());
        parameters.put("itinerary", itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(list -> {

                    if (callback != null)
                        callback.accept(!list.isEmpty() && new Date().compareTo(list.get(0).getRequestedDate()) > 0);

                })
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

    /**
     * Check if itinerary is reviewed by user.
     *
     * @param context   The context of the caller.
     * @param user      Reviewer of the itinerary.
     * @param itinerary The itinerary reviewed.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void isReviewedItinerary(Activity context, User user, Itinerary itinerary, @Nullable RunnableUsingBusinessEntity callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("booked_itinerary", itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    if (callback != null)
                        callback.run(!list.isEmpty() ? list.get(0) : null, !list.isEmpty());
                })
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

    /**
     * Update itinerary's review.
     *
     * @param context         The context of the caller.
     * @param itineraryReview Review of the itinerary.
     * @param callback        A callback to be executed after the operation is completed.
     */
    public static void updateReviewItinerary(Activity context, ItineraryReview itineraryReview, @Nullable BooleanRunnable callback) {

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_ITINERARY_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(itineraryReview))
                .build()
                .execute();
    }

    /**
     * Delete itinerary's review.
     *
     * @param context         The context of the caller.
     * @param itineraryReview Review of the itinerary.
     * @param callback        A callback to be executed after the operation is completed.
     */
    public static void deleteReviewItinerary(Activity context, ItineraryReview itineraryReview, @Nullable BooleanRunnable callback){
        new BooleanConnector.Builder(ConnectorConstants.DELETE_ITINERARY_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(itineraryReview))
                .build()
                .execute();
    }

    /**
     * Insert itinerary's review.
     *
     * @param context         The context of the caller.
     * @param itineraryReview Review of the itinerary.
     * @param callback        A callback to be executed after the operation is completed.
     */
    public static void addReviewItinerary(Activity context, ItineraryReview itineraryReview, @Nullable BooleanRunnable callback){
        new BooleanConnector.Builder(ConnectorConstants.INSERT_ITINERARY_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(itineraryReview))
                .build()
                .execute();
    }


}

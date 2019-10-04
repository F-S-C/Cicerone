package com.fsc.cicerone.manager;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.DatabaseConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.functional_interfaces.Consumer;
import com.fsc.cicerone.functional_interfaces.RunnableUsingBusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;

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
    public static void getAvgItineraryFeedback(Activity context, Itinerary itinerary, @Nullable Consumer<Float> callback) {
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
    public static void permissionReviewItinerary(Activity context, User user, Itinerary itinerary, @Nullable Consumer<Boolean> callback, @Nullable DatabaseConnector.OnStartConnectionListener onStartConnectionListener) {
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
    public static void updateReviewItinerary(Activity context, ItineraryReview itineraryReview, @Nullable Consumer<Boolean> callback) {

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
    public static void deleteReviewItinerary(Activity context, ItineraryReview itineraryReview, @Nullable Consumer<Boolean> callback) {
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
    public static void addReviewItinerary(Activity context, ItineraryReview itineraryReview, @Nullable Consumer<Boolean> callback) {
        new BooleanConnector.Builder(ConnectorConstants.INSERT_ITINERARY_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(itineraryReview))
                .build()
                .execute();
    }

    /**
     * Set the feedback's average of the user.
     *
     * @param context  The context of the caller.
     * @param user     The user of the feedback's avarage.
     * @param callback A callback to be executed after the operation is completed.
     */
    public static void getAvgUserFeedback(Activity context, User user, @Nullable Consumer<Float> callback) {
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("reviewed_user", user.getUsername());
        SendInPostConnector<UserReview> connectorReview = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    int sum = 0;
                    for (UserReview review : list) {
                        sum += review.getFeedback();
                    }
                    if (callback != null)
                        callback.accept(!list.isEmpty() ? (float) sum / list.size() : 0);
                })
                .setObjectToSend(parameter)
                .build();
        connectorReview.execute();
    }

    /**
     * Check if user can review user.
     *
     * @param context                   The context of the caller.
     * @param user                      The user reviewer.
     * @param reviewed_user             The user reviewed.
     * @param callback                  A callback to be executed after the operation is completed.
     * @param onStartConnectionListener On start connection callback.
     */
    public static void permissionReviewUser(Activity context, User reviewed_user, User user, @Nullable Consumer<Boolean> callback, @Nullable DatabaseConnector.OnStartConnectionListener onStartConnectionListener) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("reviewed_user", reviewed_user.getUsername());
        new BooleanConnector.Builder(ConnectorConstants.REQUEST_FOR_REVIEW)
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

    /**
     * Check if user is reviewed by user.
     *
     * @param context       The context of the caller.
     * @param user          Reviewer of the itinerary.
     * @param reviewed_user The user reviewed.
     * @param callback      A callback to be executed after the operation is completed.
     */
    public static void isReviewedUser(Activity context, User reviewed_user, User user, @Nullable RunnableUsingBusinessEntity callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getUsername());
        parameters.put("reviewed_user", reviewed_user.getUsername());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
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
     * Update user's review.
     *
     * @param context    The context of the caller.
     * @param userReview Review of the user.
     * @param callback   A callback to be executed after the operation is completed.
     */
    public static void updateReviewUser(Activity context, UserReview userReview, @Nullable Consumer<Boolean> callback) {

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_USER_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(userReview))
                .build()
                .execute();
    }

    /**
     * Delete user's review.
     *
     * @param context    The context of the caller.
     * @param userReview Review of the user.
     * @param callback   A callback to be executed after the operation is completed.
     */
    public static void deleteReviewUser(Activity context, UserReview userReview, @Nullable Consumer<Boolean> callback) {
        new BooleanConnector.Builder(ConnectorConstants.DELETE_USER_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(userReview))
                .build()
                .execute();
    }

    /**
     * Insert user's review.
     *
     * @param context    The context of the caller.
     * @param userReview Review of the user.
     * @param callback   A callback to be executed after the operation is completed.
     */
    public static void addReviewUser(Activity context, UserReview userReview, @Nullable Consumer<Boolean> callback) {
        new BooleanConnector.Builder(ConnectorConstants.INSERT_USER_REVIEW)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(userReview))
                .build()
                .execute();
    }

    /**
     * Get all the reviews written to a user.
     *
     * @param context      The context of the caller.
     * @param reviewedUser The user reviewed.
     * @param callback     A callback to be executed after the operation is completed.
     */
    public static void requestUserReviews(Activity context, User reviewedUser, @Nullable DatabaseConnector.OnStartConnectionListener onStartConnectionListener ,@Nullable DatabaseConnector.OnEndConnectionListener<UserReview> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reviewed_user", reviewedUser.getUsername());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

    /**
     * Get all the reviews written to a itinerary.
     *
     * @param context   The context of the caller.
     * @param itinerary The itinerary reviewed.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void requestItineraryReviews(Activity context, Itinerary itinerary, @Nullable DatabaseConnector.OnEndConnectionListener<UserReview> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("reviewed_itinerary", itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(context)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .execute();
    }


}

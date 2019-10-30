package com.fsc.cicerone.manager;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.AsyncDatabaseConnector;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
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

/**
 * A class that provides useful functions for the management of the reviews.
 */
public class ReviewManager {

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
        parameters.put(ItineraryReview.Columns.REVIEWED_ITINERARY_KEY, itinerary.getCode());
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
                .getData();
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
    public static void permissionReviewItinerary(Activity context, User user, Itinerary itinerary, @Nullable Consumer<Boolean> callback, @Nullable AsyncDatabaseConnector.OnStartConnectionListener onStartConnectionListener) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(User.Columns.USERNAME_KEY, user.getUsername());
        parameters.put(Reservation.Columns.BOOKED_ITINERARY_KEY, itinerary.getCode());
        parameters.put(Reservation.Columns.ITINERARY_KEY, itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(list -> {

                    if (callback != null)
                        callback.accept(!list.isEmpty() && new Date().compareTo(list.get(0).getRequestedDate()) > 0);

                })
                .setObjectToSend(parameters)
                .build()
                .getData();
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
        parameters.put(User.Columns.USERNAME_KEY, user.getUsername());
        parameters.put(Reservation.Columns.BOOKED_ITINERARY_KEY, itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    if (callback != null)
                        callback.run(!list.isEmpty() ? list.get(0) : null, !list.isEmpty());
                })
                .setObjectToSend(parameters)
                .build()
                .getData();
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
                .getData();
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
                .getData();
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
                .getData();
    }

    /**
     * Set the feedback's average of the user.
     *
     * @param context  The context of the caller.
     * @param user     The user of the feedback's avarage.
     * @param callback A callback to be executed after the operation is completed.
     */
    public static void getAvgUserFeedback(Activity context, User user, @Nullable Consumer<Float> callback) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put(UserReview.Columns.REVIEWED_USER_KEY, user.getUsername());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
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
                .build()
                .getData();
    }

    /**
     * Check if user can review user.
     *
     * @param context                   The context of the caller.
     * @param user                      The user reviewer.
     * @param reviewedUser              The user reviewed.
     * @param callback                  A callback to be executed after the operation is completed.
     * @param onStartConnectionListener On start connection callback.
     */
    public static void permissionReviewUser(Activity context, User reviewedUser, User user, @Nullable Consumer<Boolean> callback, @Nullable AsyncDatabaseConnector.OnStartConnectionListener onStartConnectionListener) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(User.Columns.USERNAME_KEY, user.getUsername());
        parameters.put(UserReview.Columns.REVIEWED_USER_KEY, reviewedUser.getUsername());
        new BooleanConnector.Builder(ConnectorConstants.REQUEST_FOR_REVIEW)
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(parameters)
                .build()
                .getData();
    }

    /**
     * Check if user is reviewed by user.
     *
     * @param context      The context of the caller.
     * @param user         Reviewer of the itinerary.
     * @param reviewedUser The user reviewed.
     * @param callback     A callback to be executed after the operation is completed.
     */
    public static void isReviewedUser(Activity context, User reviewedUser, User user, @Nullable RunnableUsingBusinessEntity callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(User.Columns.USERNAME_KEY, user.getUsername());
        parameters.put(UserReview.Columns.REVIEWED_USER_KEY, reviewedUser.getUsername());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    if (callback != null)
                        callback.run(!list.isEmpty() ? list.get(0) : null, !list.isEmpty());
                })
                .setObjectToSend(parameters)
                .build()
                .getData();
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
                .getData();
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
                .getData();
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
                .getData();
    }

    /**
     * Get all the reviews written to a user.
     *
     * @param context      The context of the caller.
     * @param reviewedUser The user reviewed.
     * @param callback     A callback to be executed after the operation is completed.
     */
    public static void requestUserReviews(Activity context, User reviewedUser, @Nullable AsyncDatabaseConnector.OnStartConnectionListener onStartConnectionListener, @Nullable AsyncDatabaseConnector.OnEndConnectionListener<UserReview> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(UserReview.Columns.REVIEWED_USER_KEY, reviewedUser.getUsername());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .getData();
    }

    /**
     * Get all the reviews written to a itinerary.
     *
     * @param context   The context of the caller.
     * @param itinerary The itinerary reviewed.
     * @param callback  A callback to be executed after the operation is completed.
     */
    public static void requestItineraryReviews(Activity context, Itinerary itinerary, @Nullable AsyncDatabaseConnector.OnEndConnectionListener<UserReview> callback) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ItineraryReview.Columns.REVIEWED_ITINERARY_KEY, itinerary.getCode());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(context)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .getData();
    }


}

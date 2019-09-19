package com.fsc.cicerone.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.BusinessEntity;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * A <i>control</i> class that manages the users' accounts.
 */
public abstract class AccountManager {

    private static User currentLoggedUser;

    private AccountManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the current logged user if present.
     *
     * @return The current logged user (@see User).
     */
    public static User getCurrentLoggedUser() {
        return currentLoggedUser;
    }

    /**
     * An interface that emulates Java's
     * <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/Runnable.html">java.lang.Runnable</a>.
     * It can be implemented by a lambda function.
     */
    public interface RunnableUsingBusinessEntity {
        /**
         * The method that will be called by the interface's users.
         *
         * @param result  The JSON Object to be used in the body.
         * @param success Whether the previous operations were executed with success or not.
         */
        void run(BusinessEntity result, boolean success);
    }

    /**
     * An interface that emulates Java's
     * <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html">java.util.function.Consumer&lt;Boolean&gt;</a>.
     * It can be implemented by a lambda function.
     */
    public interface BooleanRunnable {
        /**
         * The method that will be called by the interface's users.
         *
         * @param success Whether the previous operations were executed with success or not.
         */
        void accept(boolean success);
    }

    /**
     * Attempt the login.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param onStart  A function to be executed before the login attempt.
     * @param onEnd    A function to be executed after the login attempt.
     */
    public static void attemptLogin(Activity context, String username, String password, Runnable onStart, RunnableUsingBusinessEntity onEnd) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("username", username);
        params.put("password", password);
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.LOGIN_CONNECTOR)
                .setContext(context)
                .setOnStartConnectionListener(onStart::run)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        // Get all the user's data.
                        SendInPostConnector<User> sendInPostConnector = new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                                .setContext(context)
                                .setOnEndConnectionListener(list -> {
                                    if (!list.isEmpty()) {
                                        list.get(0).setPassword(password);
                                        currentLoggedUser = list.get(0);
                                        onEnd.run(list.get(0), true);
                                    } else {
                                        BooleanConnector.BooleanResult booleanResult = new BooleanConnector.BooleanResult(false, "No user found");
                                        onEnd.run(booleanResult, false);
                                    }
                                })
                                .setObjectToSend(params)
                                .build();
                        sendInPostConnector.execute();
                    } else {
                        onEnd.run(result, false);
                    }
                })
                .setObjectToSend(params)
                .build();

        connector.execute();
    }

    /**
     * Logout the current user.
     */
    public static void logout() {
        currentLoggedUser = null;
    }

    /**
     * Delete the current logged account from the system.
     */
    public static void deleteCurrentAccount(Activity context) {
        if (!isLogged())
            return;

        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.DELETE_REGISTERED_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (!result.getResult()) {
                        Log.e("DELETE_USER_ERROR", result.getMessage());
                    }
                })
                .setObjectToSend(SendInPostConnector.paramsFromJSONObject(currentLoggedUser.getCredentials()))
                .build();
        connector.execute();

        logout();
    }

    /**
     * Check if the user is currently logged.
     *
     * @return True if the user is logged, false otherwise.
     */
    public static boolean isLogged() {
        return currentLoggedUser != null;
    }

    /**
     * Control if username exists on server.
     *
     * @param user   The username to verify.
     * @param result A function to be executed after the check.
     */
    public static void checkIfUsernameExists(Activity context, String user, BooleanRunnable result) {
        Map<String, Object> obj = new HashMap<>(1);
        obj.put("username", user);
        SendInPostConnector<User> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> result.accept(!list.isEmpty()))
                .setObjectToSend(obj)
                .build();
        connector.execute();
    }

    /**
     * Control if email exists on server.
     *
     * @param email  The email to verify.
     * @param result A function to be executed after the check.
     */
    public static void checkIfEmailExists(Activity context, String email, BooleanRunnable result) {
        Map<String, Object> obj = new HashMap<>(1);
        obj.put("email", email);
        SendInPostConnector<User> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> result.accept(!list.isEmpty()))
                .setObjectToSend(obj)
                .build();
        connector.execute();
    }

    /**
     * Inserts the user into the database.
     *
     * @param user     User to insert in the database.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void insertUser(Activity context, User user, BooleanRunnable callback) {
        Log.i("USERDATA", user.toJSONObject().toString());
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    callback.accept(result.getResult());
                    if (!result.getResult())
                        Log.e("ERROR INSERT USER", result.getMessage());
                })
                .setObjectToSend(SendInPostConnector.paramsFromJSONObject(user.toJSONObject())).build();
        connector.execute();
    }

    /**
     * Inserts a document into the database.
     *
     * @param username The username of the document owner.
     * @param document Document to insert in the database.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void insertUserDocument(Activity context, String username, Document document, BooleanRunnable callback) {
        Map<String, Object> doc = SendInPostConnector.paramsFromJSONObject(document.toJSONObject());
        doc.put("username", username);
        Log.i("DOCUMENT", doc.toString());
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_DOCUMENT)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    callback.accept(result.getResult());
                    if (!result.getResult())
                        Log.e("ERROR INSERT DOCUMENT", result.getMessage());
                })
                .setObjectToSend(doc)
                .build();
        connector.execute();
    }

    /**
     * Gets the average earnings of a user.
     *
     * @param username The username.
     * @param t        The TextView to be set with the earnings.
     * @param c        The application context.
     */
    public static void userAvgEarnings(Activity context, String username, TextView t, Context c) {
        Map<String, Object> user = new HashMap<>(1);
        user.put("cicerone", username);
        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    int count = 0;
                    float sum = 0;
                    for (Reservation reservation : list) {
                        if (reservation.isConfirmed()) {
                            count++;
                            sum += reservation.getTotal();
                        }
                    }
                    t.setText(c.getString(R.string.avg_earn, (count > 0) ? sum / count : 0));
                })
                .setObjectToSend(user)
                .build();
        connector.execute();
    }

    public static void sendEmailWithContacts(Activity context, Itinerary itinerary, User deliveryToUser, BooleanRunnable callback) {
        Map<String, Object> data = new HashMap<>(3);
        data.put("username", AccountManager.getCurrentLoggedUser().getUsername());
        data.put("itinerary_code", itinerary.getCode());
        data.put("recipient_email", deliveryToUser.getEmail());
        BooleanConnector sendEmailConnector = new BooleanConnector.Builder(
                ConnectorConstants.EMAIL_SENDER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    callback.accept(result.getResult());
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

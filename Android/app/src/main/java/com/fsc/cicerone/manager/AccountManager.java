package com.fsc.cicerone.manager;

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
        void accept(boolean success) throws JSONException;
    }

    /**
     * Attempt the login.
     *
     * @param username The user's username.
     * @param password The user's password.
     * @param onStart  A function to be executed before the login attempt.
     * @param onEnd    A function to be executed after the login attempt.
     */
    public static void attemptLogin(Context context, String username, String password, Runnable onStart, RunnableUsingBusinessEntity onEnd) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("username", username);
        params.put("password", password);
        BooleanConnector connector = new BooleanConnector(
                context,
                ConnectorConstants.LOGIN_CONNECTOR,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        onStart.run();
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if (result.getResult()) {
                            // Get all the user's data.
                            SendInPostConnector<User> connector = new SendInPostConnector<>(
                                    null,
                                    ConnectorConstants.REGISTERED_USER,
                                    BusinessEntityBuilder.getFactory(User.class),
                                    new DatabaseConnector.CallbackInterface<User>() {
                                        @Override
                                        public void onStartConnection() {
                                            // Do nothing
                                        }

                                        @Override
                                        public void onEndConnection(List<User> list) {
                                            if (!list.isEmpty()) {
                                                list.get(0).setPassword(password);
                                                currentLoggedUser = list.get(0);
                                                onEnd.run(list.get(0), true);
                                            } else {
                                                BooleanConnector.BooleanResult booleanResult = new BooleanConnector.BooleanResult(false, "No user found");
                                                onEnd.run(booleanResult, false);
                                            }
                                        }
                                    },
                                    params);
                            connector.execute();
                        } else {
                            onEnd.run(result, false);
                        }
                    }
                },
                params);

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
    public static void deleteCurrentAccount(Context context) {
        if (!isLogged())
            return;

        BooleanConnector connector = new BooleanConnector(
                context,
                ConnectorConstants.DELETE_REGISTERED_USER,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if (!result.getResult()) {
                            Log.e("DELETE_USER_ERROR", result.getMessage());
                        }
                    }
                },
                SendInPostConnector.paramsFromJSONObject(currentLoggedUser.getCredentials()));
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
    public static void checkIfUsernameExists(Context context, String user, BooleanRunnable result) {
        Map<String, Object> obj = new HashMap<>(1);
        obj.put("username", user);
        SendInPostConnector<User> connector = new SendInPostConnector<>(
                context,
                ConnectorConstants.REGISTERED_USER,
                BusinessEntityBuilder.getFactory(User.class),
                new DatabaseConnector.CallbackInterface<User>() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(List<User> list) throws JSONException {
                        result.accept(!list.isEmpty());
                    }
                },
                obj);
        connector.execute();
    }

    /**
     * Control if email exists on server.
     *
     * @param email  The email to verify.
     * @param result A function to be executed after the check.
     */
    public static void checkIfEmailExists(Context context, String email, BooleanRunnable result) {
        Map<String, Object> obj = new HashMap<>(1);
        obj.put("email", email);
        SendInPostConnector<User> connector = new SendInPostConnector<>(
                context,
                ConnectorConstants.REGISTERED_USER,
                BusinessEntityBuilder.getFactory(User.class),
                new DatabaseConnector.CallbackInterface<User>() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(List<User> list) throws JSONException {
                        result.accept(!list.isEmpty());
                    }
                },
                obj);
        connector.execute();
    }

    /**
     * Inserts the user into the database.
     *
     * @param user     User to insert in the database.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void insertUser(Context context, User user, BooleanRunnable callback) {
        Log.i("USERDATA", user.toJSONObject().toString());
        BooleanConnector connector = new BooleanConnector(
                context,
                ConnectorConstants.INSERT_USER,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        callback.accept(result.getResult());
                        if (!result.getResult())
                            Log.e("ERROR INSERT USER", result.getMessage());
                    }
                },
                SendInPostConnector.paramsFromJSONObject(user.toJSONObject()));
        connector.execute();
    }

    /**
     * Inserts a document into the database.
     *
     * @param username The username of the document owner.
     * @param document Document to insert in the database.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void insertUserDocument(Context context, String username, Document document, BooleanRunnable callback) {
        Map<String, Object> doc = SendInPostConnector.paramsFromJSONObject(document.toJSONObject());
        doc.put("username", username);
        Log.i("DOCUMENT", doc.toString());
        BooleanConnector connector = new BooleanConnector(
                context,
                ConnectorConstants.INSERT_DOCUMENT,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        callback.accept(result.getResult());
                        if (!result.getResult())
                            Log.e("ERROR INSERT DOCUMENT", result.getMessage());
                    }
                },
                doc);
        connector.execute();
    }

    /**
     * Gets the average earnings of a user.
     *
     * @param username The username.
     * @param t        The TextView to be set with the earnings.
     * @param c        The application context.
     */
    public static void userAvgEarnings(Context context, String username, TextView t, Context c) {
        Map<String, Object> user = new HashMap<>(1);
        user.put("cicerone", username);
        SendInPostConnector<Reservation> connector = new SendInPostConnector<>(
                context,
                ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY,
                BusinessEntityBuilder.getFactory(Reservation.class),
                new DatabaseConnector.CallbackInterface<Reservation>() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(List<Reservation> list) {
                        int count = 0;
                        float sum = 0;
                        for (Reservation reservation : list) {
                            if (reservation.isConfirmed()) {
                                count++;
                                sum += reservation.getTotal();
                            }
                        }
                        t.setText(c.getString(R.string.avg_earn, (count > 0) ? sum / count : 0));
                    }
                },
                user);
        connector.execute();
    }

    public static void sendEmailWithContacts(Context context, Itinerary itinerary, User deliveryToUser, BooleanRunnable callback) {
        Map<String, Object> data = new HashMap<>(3);
        data.put("username", AccountManager.getCurrentLoggedUser().getUsername());
        data.put("itinerary_code", itinerary.getCode());
        data.put("recipient_email", deliveryToUser.getEmail());
        BooleanConnector sendEmailConnector = new BooleanConnector(
                context,
                ConnectorConstants.EMAIL_SENDER,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        callback.accept(result.getResult());
                        if (result.getResult()) {
                            Log.i("SEND OK", "true");
                        } else {
                            Log.e("SEND ERROR", result.getMessage());
                        }
                    }
                },
                data);
        sendEmailConnector.execute();
    }
}

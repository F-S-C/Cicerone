package com.fsc.cicerone;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * A <i>control</i> class that manages the users' accounts.
 */
public abstract class AccountManager {

    private static User currentLoggedUser;
    private static final String ERROR_TAG = "ERROR IN " + AccountManager.class.getName();

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
    public interface RunnableUsingJson {
        /**
         * The method that will be called by the interface's users.
         *
         * @param jsonObject The JSON Object to be used in the body.
         * @param success    Whether the previous operations were executed with success or not.
         * @throws JSONException The method could throw a JSON Exception in its body if the parameter
         *                       jsonObject is used.
         */
        void run(JSONObject jsonObject, boolean success) throws JSONException;
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
     * @param user    The user's credentials (username and password). The JSON Object must
     *                contain both (using "username" and "password" as keys for the respective
     *                values).
     * @param onStart A function to be executed before the login attempt.
     * @param onEnd   A function to be executed after the login attempt.
     */
    public static void attemptLogin(JSONObject user, Runnable onStart, RunnableUsingJson onEnd) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.LOGIN_CONNECTOR,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        onStart.run();
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        if (result.getResult()) {
                            SendInPostConnector<User> connector = new SendInPostConnector<>(
                                    ConnectorConstants.REGISTERED_USER,
                                    BusinessEntityBuilder.getFactory(User.class),
                                    new DatabaseConnector.CallbackInterface<User>() {
                                        @Override
                                        public void onStartConnection() {
                                            // Do nothing
                                        }

                                        @Override
                                        public void onEndConnection(List<User> jsonArray) throws JSONException {
                                            User result = jsonArray.get(0);
                                            try {
                                                result.setPassword(user.getString("password"));
                                            } catch (JSONException e) {
                                                Log.e(ERROR_TAG, e.getMessage());
                                            }
                                            currentLoggedUser = result;
                                            onEnd.run(result.toJSONObject(), true);
                                        }
                                    },
                                    user);
                            connector.execute();
                        } else {
                            onEnd.run(result.toJSONObject(), false);
                        }
                    }
                },
                user);

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
    public static void deleteCurrentAccount() {
        if (!isLogged())
            return;

        BooleanConnector connector = new BooleanConnector(
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
                currentLoggedUser.getCredentials());
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
    public static void checkIfUsernameExists(String user, BooleanRunnable result) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("username", user);
            SendInPostConnector<User> connector = new SendInPostConnector<>(
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
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    /**
     * Control if email exists on server.
     *
     * @param email  The email to verify.
     * @param result A function to be executed after the check.
     */
    public static void checkIfEmailExists(String email, BooleanRunnable result) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("email", email);
            SendInPostConnector<User> connector = new SendInPostConnector<>(
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
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    /**
     * Inserts the user into the database.
     *
     * @param user   User to insert in the database.
     * @param result A function to be executed after the insert attempt.
     */
    public static void insertUser(User user, BooleanRunnable result) {
        Log.i("USERDATA", user.toJSONObject().toString());
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_USER,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result1) throws JSONException {
                        result.accept(result1.getResult());
                        if (!result1.getResult())
                            Log.e("ERROR INSERT USER", result1.getMessage());
                    }
                },
                user.toJSONObject());
        connector.execute();
    }

    /**
     * Inserts a document into the database.
     *
     * @param username The username of the document owner.
     * @param document Document to insert in the database.
     * @param result   A function to be executed after the insert attempt.
     */
    public static void insertUserDocument(String username, Document document, BooleanRunnable result) {
        JSONObject doc = document.toJSONObject();
        try {
            doc.put("username", username);
            Log.i("DOCUMENT", doc.toString());
            BooleanConnector connector = new BooleanConnector(
                    ConnectorConstants.INSERT_DOCUMENT,
                    new BooleanConnector.CallbackInterface() {
                        @Override
                        public void onStartConnection() {
                            //Do nothing
                        }

                        @Override
                        public void onEndConnection(BooleanConnector.BooleanResult result1) throws JSONException {
                            result.accept(result1.getResult());
                            if (!result1.getResult())
                                Log.e("ERROR INSERT DOCUMENT", result1.getMessage());
                        }
                    },
                    doc);
            connector.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    /**
     * Gets the average earnings of a user.
     *
     * @param username The username.
     * @param t        The TextView to be set with the earnings.
     * @param c        The application context.
     */
    public static void userAvgEarnings(String username, TextView t, Context c) {
        JSONObject user = new JSONObject();
        try {
            user.put("cicerone", username);
            SendInPostConnector<Reservation> connector = new SendInPostConnector<>(
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
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    public static void sendEmailWithContacts(Itinerary itinerary, User deliveryToUser, BooleanRunnable result) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", AccountManager.getCurrentLoggedUser().getUsername());
            data.put("itinerary_code", itinerary.getCode());
            data.put("recipient_email", deliveryToUser.getEmail());
            BooleanConnector sendEmailConnector = new BooleanConnector(
                    ConnectorConstants.EMAIL_SENDER,
                    new BooleanConnector.CallbackInterface() {
                        @Override
                        public void onStartConnection() {
                            //Do nothing
                        }

                        @Override
                        public void onEndConnection(BooleanConnector.BooleanResult result1) throws JSONException {
                            result.accept(result1.getResult());
                            if (result1.getResult()) {
                                Log.i("SEND OK", "true");
                            } else {
                                Log.e("SEND ERROR", result1.getMessage());
                            }
                        }
                    },
                    data);
            sendEmailConnector.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }
}

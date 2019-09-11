package com.fsc.cicerone;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.LOGIN_CONNECTOR, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                onStart.run();
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                boolean success = result.getBoolean("result");
                if (success) {
                    SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
                        @Override
                        public void onStartConnection() {
                            // Do nothing
                        }

                        @Override
                        public void onEndConnection(JSONArray jsonArray) throws JSONException {
                            JSONObject result = jsonArray.getJSONObject(0);
                            result.put("password", user.getString("password"));
                            currentLoggedUser = new User(result);
                            onEnd.run(result, true);
                        }
                    }, user);
                    connector.execute();
                } else {
                    onEnd.run(result, false);
                }
            }
        }, user);

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

        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                if (!result.getBoolean("result")) {
                    Log.e("DELETE_USER_ERROR", result.getString("error"));
                }
            }
        }, currentLoggedUser.getCredentials());
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
            SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    result.accept(jsonArray.length() > 0);
                }
            }, obj);
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
            SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    result.accept(jsonArray.length() > 0);
                }
            }, obj);
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
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                result.accept(jsonArray.getJSONObject(0).getBoolean("result"));
                if (!jsonArray.getJSONObject(0).getBoolean("result"))
                    Log.e("ERROR INSERT USER", jsonArray.getJSONObject(0).getString("error"));
            }
        });
        connector.setObjectToSend(user.toJSONObject());
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
        JSONObject doc = document.getJSONObject();
        try {
            doc.put("username", username);
            Log.i("DOCUMENT", doc.toString());
            SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_DOCUMENT, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    result.accept(jsonArray.getJSONObject(0).getBoolean("result"));
                    if (!jsonArray.getJSONObject(0).getBoolean("result"))
                        Log.e("ERROR INSERT DOCUMENT", jsonArray.getJSONObject(0).getString("error"));
                }
            });
            connector.setObjectToSend(doc);
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
            SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    // TODO: Use Reservation object
                    int count = 0;
                    float sum = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (!jsonArray.getJSONObject(i).isNull("confirm_date")) {
                            if (!jsonArray.getJSONObject(i).getString("confirm_date").equals("0000-00-00")) {
                                count++;
                                sum += Float.valueOf(jsonArray.getJSONObject(i).getString("total"));
                            }
                        }
                    }
                    t.setText(c.getString(R.string.avg_earn, (count > 0) ? sum / count : 0));
                }
            }, user);
            connector.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    public static void sendEmailWithContacts(Itinerary itinerary, User deliveryToUser, BooleanRunnable result){
        JSONObject data = new JSONObject();
        try {
            data.put("username", AccountManager.getCurrentLoggedUser().getUsername());
            data.put("itinerary_code",itinerary.getCode());
            data.put("recipient_email",deliveryToUser.getEmail());
            SendInPostConnector sendEmailConnector = new SendInPostConnector(ConnectorConstants.EMAIL_SENDER, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    result.accept(jsonArray.getJSONObject(0).getBoolean("result"));
                    if(jsonArray.getJSONObject(0).getBoolean("result")){
                        Log.i("SEND OK","true");
                    }else{
                        Log.e("SEND ERROR", jsonArray.getJSONObject(0).getString("error"));
                    }
                }
            }, data);
            sendEmailConnector.execute();
        }catch(JSONException e){
            Log.e(ERROR_TAG,e.toString());
        }
    }
}

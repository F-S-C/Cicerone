package com.fsc.cicerone;

import android.util.Log;

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
        if(!isLogged())
            return;

        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                if(!result.getBoolean("result")){
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
}

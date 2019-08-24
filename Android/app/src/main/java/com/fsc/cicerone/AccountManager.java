package com.fsc.cicerone;

import android.util.Log;
import android.util.Patterns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

import static java.lang.Thread.sleep;

/**
 * A <i>control</i> class that manages the users' accounts.
 */
public abstract class AccountManager {

    private static User currentLoggedUser;
    private static final String ERROR_TAG = "ERROR IN " + AccountManager.class.getName();

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
        void accept(boolean success);
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
     * @param user The username to verify.
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
                public void onEndConnection(JSONArray jsonArray) {
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
     * @param email The email to verify.
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
                public void onEndConnection(JSONArray jsonArray) {
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
     * @param user User to insert in the database.
     * @param result A function to be executed after the insert attempt.
     */
    public static void insertUser(User user, BooleanRunnable result){
        Log.i("USERDATA",user.toJSONObject().toString());
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                result.accept(jsonArray.getJSONObject(0).getBoolean("result"));
                if(!jsonArray.getJSONObject(0).getBoolean("result"))
                    Log.e("ERROR INSERT USER",jsonArray.getJSONObject(0).getString("error"));
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
     * @param result A function to be executed after the insert attempt.
     */
    public static void insertUserDocument(String username, Document document, BooleanRunnable result){
        JSONObject doc = document.getJSONObject();
        try {
            doc.put("username", username);
            Log.i("DOCUMENT",doc.toString());
            SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_DOCUMENT, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    result.accept(jsonArray.getJSONObject(0).getBoolean("result"));
                    if(!jsonArray.getJSONObject(0).getBoolean("result"))
                        Log.e("ERROR INSERT DOCUMENT",jsonArray.getJSONObject(0).getString("error"));
                }
            });
            connector.setObjectToSend(doc);
            connector.execute();
        }catch (JSONException e){
            Log.e(ERROR_TAG, e.toString());
        }
    }
}

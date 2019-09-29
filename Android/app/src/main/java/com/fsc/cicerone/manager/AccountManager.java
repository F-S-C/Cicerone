/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fsc.cicerone.manager;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.DatabaseConnector;
import com.fsc.cicerone.app_connector.GetDataConnector;
import com.fsc.cicerone.functional_interfaces.BooleanRunnable;
import com.fsc.cicerone.functional_interfaces.RunnableUsingBusinessEntity;
import com.fsc.cicerone.mailer.Mailer;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;

/**
 * A <i>control</i> class that manages the users' accounts.
 */
public abstract class AccountManager {

    private static User currentLoggedUser;
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

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
     * Attempt the login.
     *
     * @param credentials The credentials of the user.
     * @param onStart     A function to be executed before the login attempt.
     * @param onEnd       A function to be executed after the login attempt. The boolean value
     *                    contains true if login was successful, false otherwise.
     */
    public static void attemptLogin(Activity context, User.Credentials credentials, @Nullable DatabaseConnector.OnStartConnectionListener onStart, @Nullable BooleanRunnable onEnd) {
        Map<String, Object> params = new HashMap<>(2);
        params.put(USERNAME_KEY, credentials.getUsername());
        params.put(PASSWORD_KEY, credentials.getPassword());
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.LOGIN_CONNECTOR)
                .setContext(context)
                .setOnStartConnectionListener(onStart)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        // Get all the user's data.
                        SendInPostConnector<User> sendInPostConnector = new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                                .setContext(context)
                                .setOnEndConnectionListener(list -> {
                                    if (!list.isEmpty()) {
                                        currentLoggedUser = list.get(0);
                                        currentLoggedUser.setPassword(credentials.getPassword());
                                        if (onEnd != null) onEnd.accept(true);
                                    } else {
                                        if (onEnd != null) onEnd.accept(false);
                                    }
                                })
                                .setObjectToSend(params)
                                .build();
                        sendInPostConnector.execute();
                    } else {
                        if (onEnd != null) onEnd.accept(false);
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
                    }else{
                        Mailer.sendAccountDeleteConfirmationEmail(v -> logout());
                    }
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(currentLoggedUser.getCredentials()))
                .build();
        connector.execute();
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
        obj.put(USERNAME_KEY, user);
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
                .setObjectToSend(SendInPostConnector.paramsFromObject(user)).build();
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
        Map<String, Object> doc = SendInPostConnector.paramsFromObject(document);
        doc.put(USERNAME_KEY, username);
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
     * @param context  The application context.
     * @param username The username.
     * @param t        The TextView to be set with the earnings.
     */
    public static void userAvgEarnings(Activity context, String username, TextView t) {
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
                    t.setText(String.format(context.getString(R.string.avg_earn), (count > 0) ? sum / count : 0));
                })
                .setObjectToSend(user)
                .build();
        connector.execute();
    }

    //TODO

    /**
     * Get list of the user that a user can review.
     *
     * @param context The application context.
     * @param users   The user to be reported.
     */
    public static void setUsersInSpinner(Activity context, Spinner users) {
        // TODO: Needs cleanup?
        GetDataConnector<User> connector = new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    List<String> cleanList = new ArrayList<>();

                    for (User user : list) {
                        if (!user.getUsername().equals(AccountManager.getCurrentLoggedUser().getUsername()))
                            cleanList.add(user.getUsername());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(context),
                            android.R.layout.simple_spinner_item, cleanList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    users.setAdapter(dataAdapter);

                })
                .build();
        connector.execute();
    }

    /**
     * Delete registered user.
     *
     * @param context  The application context.
     * @param user     The user to be deleted.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void deleteAccount(Activity context, User user, @Nullable BooleanRunnable callback) {
        new BooleanConnector.Builder(ConnectorConstants.DELETE_REGISTERED_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(user))
                .build()
                .execute();
    }

    /**
     * Get list of the registered user.
     *
     * @param context                   The application context.
     * @param onStartConnectionListener On start connection callback.
     * @param callback                  A function to be executed after the insert attempt.
     */
    public static void getListUsers(Activity context, @Nullable GetDataConnector.OnStartConnectionListener onStartConnectionListener, @Nullable GetDataConnector.OnEndConnectionListener<User> callback) {
        new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .build()
                .execute();
    }
}

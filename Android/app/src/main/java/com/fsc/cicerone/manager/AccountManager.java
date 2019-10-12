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
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.AsyncDatabaseConnector;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.GetDataConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.functional_interfaces.Consumer;
import com.fsc.cicerone.mailer.Mailer;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.notifications.Config;
import com.fsc.cicerone.notifications.NotificationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Attempt the login.
     *
     * @param credentials The credentials of the user.
     * @param onStart     A function to be executed before the login attempt.
     * @param onEnd       A function to be executed after the login attempt. The boolean value
     *                    contains true if login was successful, false otherwise.
     */
    public static void attemptLogin(@NonNull Activity context, @NonNull User.Credentials credentials, @Nullable AsyncDatabaseConnector.OnStartConnectionListener onStart, @Nullable Consumer<Boolean> onEnd) {
        Map<String, Object> params = new HashMap<>(2);
        params.put(User.Columns.USERNAME_KEY, credentials.getUsername());
        params.put(User.Columns.PASSWORD_KEY, credentials.getPassword());
        new BooleanConnector.Builder(ConnectorConstants.LOGIN_CONNECTOR)
                .setContext(context)
                .setOnStartConnectionListener(onStart)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        currentLoggedUser = new User(result.getMessage());
                        currentLoggedUser.setPassword(credentials.getPassword());
                        NotificationUtils.subscribeToTopic(context, Config.TOPIC_GLOBETROTTER(currentLoggedUser));
                        if (currentLoggedUser.getUserType() == UserType.CICERONE) {
                            NotificationUtils.subscribeToTopic(context, Config.TOPIC_CICERONE(currentLoggedUser));
                        }
                    }
                    if (onEnd != null) onEnd.accept(result.getResult());
                })
                .setObjectToSend(params)
                .build()
                .getData();
    }

    /**
     * Logout the current user.
     */
    public static void logout(@NonNull Context context) {
        currentLoggedUser = null;
        NotificationUtils.unsubscribeFromAllTopics(context);
    }

    /**
     * Delete the current logged account from the system.
     */
    public static void deleteCurrentAccount(@NonNull Activity context) {
        if (!isLogged())
            return;

        new BooleanConnector.Builder(ConnectorConstants.DELETE_REGISTERED_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (!result.getResult()) {
                        Log.e("DELETE_USER_ERROR", result.getMessage());
                    } else {
                        Mailer.sendAccountDeleteConfirmationEmail(v -> logout(context));
                    }
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(currentLoggedUser.getCredentials()))
                .build()
                .getData();
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
    public static void checkIfUsernameExists(@Nullable Activity context, @NonNull String user, @NonNull Consumer<Boolean> result) {
        Map<String, Object> obj = new HashMap<>(1);
        obj.put(User.Columns.USERNAME_KEY, user);
        new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> result.accept(!list.isEmpty()))
                .setObjectToSend(obj)
                .build()
                .getData();
    }

    /**
     * Control if email exists on server.
     *
     * @param email  The email to verify.
     * @param result A function to be executed after the check.
     */
    public static void checkIfEmailExists(@Nullable Activity context, @NonNull String email, @NonNull Consumer<Boolean> result) {
        Map<String, Object> obj = new HashMap<>(1);
        obj.put(User.Columns.EMAIL_KEY, email);
        new SendInPostConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> result.accept(!list.isEmpty()))
                .setObjectToSend(obj)
                .build()
                .getData();
    }

    /**
     * Inserts the user into the database.
     *
     * @param user     User to insert in the database.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void insertUser(@Nullable Activity context, @NonNull User user, @Nullable Consumer<Boolean> callback) {
        Log.i("USERDATA", user.toJSONObject().toString());
        new BooleanConnector.Builder(ConnectorConstants.INSERT_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                    if (!result.getResult())
                        Log.e("ERROR INSERT USER", result.getMessage());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(user)).build()
                .getData();
    }

    /**
     * Gets the average earnings of a user.
     *
     * @param context  The application context.
     * @param username The username.
     * @param t        The TextView to be set with the earnings.
     */
    public static void userAvgEarnings(@NonNull Activity context, @NonNull String username, @NonNull TextView t) {
        Map<String, Object> user = new HashMap<>(1);
        user.put(UserType.CICERONE.toString(), username);
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
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
                .build()
                .getData();
    }

    /**
     * Get list of the user that a user can review.
     *
     * @param context The application context.
     * @param users   The user to be reported.
     */
    public static void setUsersInSpinner(@NonNull Activity context, @NonNull Spinner users) {
        // TODO: Needs cleanup?
        new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    List<String> cleanList = new ArrayList<>();

                    for (User user : list) {
                        if (!user.getUsername().equals(AccountManager.getCurrentLoggedUser().getUsername()))
                            cleanList.add(user.getUsername());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, cleanList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    users.setAdapter(dataAdapter);

                })
                .build()
                .getData();
    }

    /**
     * Delete registered user.
     *
     * @param context  The application context.
     * @param user     The user to be deleted.
     * @param callback A function to be executed after the insert attempt.
     */
    public static void deleteAccount(@Nullable Activity context, User user, @Nullable Consumer<Boolean> callback) {
        new BooleanConnector.Builder(ConnectorConstants.DELETE_REGISTERED_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(user))
                .build()
                .getData();
    }

    /**
     * Get list of the registered user.
     *
     * @param context                   The application context.
     * @param onStartConnectionListener On start connection callback.
     * @param callback                  A function to be executed after the insert attempt.
     */
    public static void getListUsers(@Nullable Activity context, @Nullable GetDataConnector.OnStartConnectionListener onStartConnectionListener, @Nullable GetDataConnector.OnEndConnectionListener<User> callback) {
        new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .build()
                .getData();
    }
}

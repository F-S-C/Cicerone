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

import androidx.annotation.Nullable;

import com.fsc.cicerone.functional_interfaces.BooleanRunnable;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Wishlist;

import java.util.HashMap;
import java.util.Map;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.DatabaseConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;

public abstract class WishlistManager {

    private static final String USERNAME_KEY = "username";
    private static final String ITINERARY_KEY = "itinerary_in_wishlist";

    private WishlistManager() {
        throw new IllegalStateException("Utility class");
    }

    public static void getWishlist(Activity context, @Nullable DatabaseConnector.OnStartConnectionListener onStartCallback, @Nullable DatabaseConnector.OnEndConnectionListener<Wishlist> onEndCallback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> parameters = new HashMap<>(1);
            parameters.put(USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());
            new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_WISHLIST, BusinessEntityBuilder.getFactory(Wishlist.class))
                    .setContext(context)
                    .setOnStartConnectionListener(onStartCallback)
                    .setOnEndConnectionListener(onEndCallback)
                    .setObjectToSend(parameters)
                    .build()
                    .execute();
        }
    }

    public static void clearWishlist(Activity context, @Nullable BooleanConnector.OnEndConnectionListener callback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> parameters = new HashMap<>(1);
            parameters.put(USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());
            new BooleanConnector.Builder(ConnectorConstants.CLEAR_WISHLIST)
                    .setContext(context)
                    .setOnEndConnectionListener(callback)
                    .setObjectToSend(parameters)
                    .build()
                    .execute();
        }
    }

    public static void removeFromWishlist(Activity context, Itinerary itinerary, @Nullable BooleanConnector.OnEndConnectionListener callback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> params = new HashMap<>(2);
            params.put(ITINERARY_KEY, itinerary.getCode());
            params.put(USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());

            new BooleanConnector.Builder(ConnectorConstants.DELETE_WISHLIST)
                    .setContext(context)
                    .setOnEndConnectionListener(callback)
                    .setObjectToSend(params)
                    .build()
                    .execute();
        }
    }

    public static void addToWishlist(Activity context, Itinerary itinerary, @Nullable BooleanConnector.OnEndConnectionListener callback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> params = new HashMap<>(2);
            params.put(ITINERARY_KEY, itinerary.getCode());
            params.put(USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());

            new BooleanConnector.Builder(ConnectorConstants.INSERT_WISHLIST)
                    .setContext(context)
                    .setOnEndConnectionListener(callback)
                    .setObjectToSend(params)
                    .build()
                    .execute();
        }
    }

    public static void isInWishlist(Activity context, Itinerary itinerary, @Nullable BooleanRunnable callback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> params = new HashMap<>(2);
            params.put(ITINERARY_KEY, itinerary.getCode());
            params.put(USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());

            new SendInPostConnector.Builder<>(ConnectorConstants.SEARCH_WISHLIST, BusinessEntityBuilder.getFactory(Wishlist.class))
                    .setContext(context)
                    .setOnEndConnectionListener(list -> {
                        if (callback != null) callback.accept(!list.isEmpty());
                    })
                    .setObjectToSend(params)
                    .build()
                    .execute();
        }
    }
}

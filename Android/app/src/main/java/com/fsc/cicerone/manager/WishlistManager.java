package com.fsc.cicerone.manager;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.fsc.cicerone.functional_interfaces.BooleanRunnable;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Wishlist;

import java.util.HashMap;
import java.util.Map;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public abstract class WishlistManager {

    public static void getWishlist(Activity context, @Nullable DatabaseConnector.OnStartConnectionListener onStartCallback, @Nullable DatabaseConnector.OnEndConnectionListener<Wishlist> onEndCallback) {
        if (AccountManager.isLogged()) {
            Map<String, Object> parameters = new HashMap<>(1);
            parameters.put("username", AccountManager.getCurrentLoggedUser().getUsername());
            new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_WISHLIST, BusinessEntityBuilder.getFactory(Wishlist.class)) //TODO: Check
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
            parameters.put("username", AccountManager.getCurrentLoggedUser().getUsername());
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
            params.put("itinerary_in_wishlist", itinerary.getCode());
            params.put("username", AccountManager.getCurrentLoggedUser().getUsername());

            new BooleanConnector.Builder(ConnectorConstants.DELETE_WISHLIST)
                    .setContext(context)
                    .setOnEndConnectionListener(callback)
                    .setObjectToSend(params)
                    .build()
                    .execute();
        }
    }

    public static void addToWishlist(Activity context, Itinerary itinerary, @Nullable BooleanConnector.OnEndConnectionListener callback){
        if (AccountManager.isLogged()) {
            Map<String, Object> params = new HashMap<>(2);
            params.put("itinerary_in_wishlist", itinerary.getCode());
            params.put("username", AccountManager.getCurrentLoggedUser().getUsername());

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
            params.put("itinerary_in_wishlist", itinerary.getCode());
            params.put("username", AccountManager.getCurrentLoggedUser().getUsername());

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

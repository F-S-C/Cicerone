package com.fsc.cicerone.notifications;

import androidx.annotation.NonNull;

import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;

public abstract class Config {
    private Config() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Global notification topic to receive all push notifications.
     */
    public static final String TOPIC_GLOBAL = "all";

    /**
     * Get the notification topic associated to a globetrotter.
     *
     * @param user The globetrotter.
     * @return The notification topic's name.
     */
    @NonNull
    public static String TOPIC_GLOBETROTTER(@NonNull User user) {
        return "globetrotter-" + user.getUsername();
    }

    /**
     * Get the notification topic associated to a cicerone.
     *
     * @param user The cicerone.
     * @return The notification topic's name.
     * @throws IllegalArgumentException if user is not a cicerone.
     */
    @NonNull
    public static String TOPIC_CICERONE(@NonNull User user) {
        if (user.getUserType() != UserType.CICERONE)
            throw new IllegalArgumentException("User " + user.getUsername() + " is not a cicerone!");
        return "cicerone-" + user.getUsername();
    }

    /**
     * Get the notification topic associated to an itinerary.
     *
     * @param itinerary The itinerary.
     * @return The notification topic's name.
     */
    @NonNull
    public static String TOPIC_ITINERARY(@NonNull Itinerary itinerary) {
        return "itinerary-" + itinerary.getCode();
    }

    static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    static final int NOTIFICATION_ID = 100;
    static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}

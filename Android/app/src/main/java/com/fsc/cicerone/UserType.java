package com.fsc.cicerone;

import android.support.annotation.NonNull;

/**
 * UserType is an enumerative type that specifies the type that a user can assume.
 **/

public enum UserType {
    /**
     * Globetrotter: a "normal" user that can leave reviews and take part to itineraries.
     */
    GLOBETROTTER,

    /**
     * Cicerone: a "special" user that can create itineraries.
     * A "Cicerone" is also a "Globetrotter".
     */
    CICERONE;

    /**
     * Get the User Type associated with an integer.
     *
     * @param u The integer.
     * @return The User Type associated with the integer u.
     * If the value of the integer is 0 then the function will return GLOBETROTTER.
     * Otherwise, if the value of the integer is 1 then the function will return CICERONE.
     */
    public static UserType getValue(Integer u) {
        u = u % 2;
        switch (u) {
            case 0:
                return GLOBETROTTER;
            case 1:
                return CICERONE;
            default:
                return null;
        }
    }

    /**
     * Convert the User Type to an integer.
     *
     * @return The integer associated with the user type.
     * If the value of the User Type is GLOBETROTTER the function will return 0.
     * Otherwise, if the value of the User Type is CICERONE, the function will return 1.
     */
    @NonNull
    public Integer toInt() {
        int toReturn;
        switch (this) {
            case GLOBETROTTER:
                toReturn = 0;
                break;
            case CICERONE:
                toReturn = 1;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return toReturn;
    }
}
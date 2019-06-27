package com.fsc.cicerone;

/**
 * UserType is an enumerative type that specifies the type that a user can assume.
 * If the value of the module is 1 then the user will be a Cicerone.
 * If the value of the module is 0 then the user will be a Globetrotter.
 **/

public enum UserType {
    GLOBETROTTER, CICERONE;

    /**
     * Get the User Type associated with an integer.
     *
     * @param u The integer
     * @return The User Type associated with the integer u
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
}

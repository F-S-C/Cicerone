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

package com.fsc.cicerone.model;

import androidx.annotation.NonNull;

/**
 * UserType is an enumerative type that specifies the type that a user can assume.
 **/

public enum UserType {
    /**
     * Globetrotter: a "normal" user that can leave reviews and take part to itineraries.
     */
    GLOBETROTTER,

    /**
     * Admin: a "special" user that administer the system.
     */
    ADMIN,

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
    @NonNull
    public static UserType getValue(Integer u) {
        u = u % 3;
        switch (u) {
            case 0:
                return GLOBETROTTER;
            case 1:
                return CICERONE;
            case 2:
                return ADMIN;
            default:
                throw new IllegalArgumentException();
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
            case ADMIN:
                toReturn = 2;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return toReturn;
    }

    /**
     * Get the string associated with the UserType.
     * @return The string.
     */
    @NonNull
    @Override
    public String toString() {
        String toReturn;
        switch (this){
            case GLOBETROTTER:
                toReturn = "globetrotter";
                break;
            case CICERONE:
                toReturn = "cicerone";
                break;
            case ADMIN:
                toReturn = "admin";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return toReturn;
    }
}
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
 * The sexes available in Cicerone.
 */
public enum Sex {
    /**
     * The Male sex.
     */
    MALE,

    /**
     * The Female sex.
     */
    FEMALE,

    /**
     * A fallback for every non-binary sex.
     */
    OTHER;

    /**
     * Get a sex from a string.
     *
     * @param s The string.
     * @return The Sex value associated with the string (<i>case insensitive</i>). The accepted
     * values are mapped in the following way:
     * <table>
     * <tr>
     * <th>String</th>
     * <th>Sex value</th>
     * </tr>
     * <tr>
     * <td>"male"</td>
     * <td>MALE</td>
     * </tr>
     * <tr>
     * <td>"female"</td>
     * <td>FEMALE</td>
     * </tr>
     * <tr>
     * <td>"other"</td>
     * <td>OTHER</td>
     * </tr>
     * </table>
     * Every other string value will be converted to null.
     */
    public static Sex getValue(String s) {
        s = s.toLowerCase();
        Sex sex;
        switch (s) {
            case "male":
                sex = MALE;
                break;
            case "female":
                sex = FEMALE;
                break;
            case "other":
                sex = OTHER;
                break;
            case "maschio":
                sex = MALE;
                break;
            case "femmina":
                sex = FEMALE;
                break;
            case "altro":
                sex = OTHER;
                break;
            default:
                sex = null;
                break;
        }
        return sex;
    }

    /**
     * Convert a Sex to String.
     *
     * @return The String associated to the Sex value (lower case). If the Sex value is null, the
     * function returns null.
     */
    @NonNull
    @Override
    public String toString() {
        String toReturn;
        switch (this) {
            case MALE:
                toReturn = "male";
                break;
            case FEMALE:
                toReturn = "female";
                break;
            case OTHER:
                toReturn = "other";
                break;
            default:
                toReturn = null;
        }
        return toReturn;
    }
}

package com.fsc.cicerone;

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

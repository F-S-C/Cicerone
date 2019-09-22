package com.fsc.cicerone.model;

import androidx.annotation.NonNull;

/**
 * Enum class that specifies the states that a Report can take.
 */
public enum ReportStatus {
    OPEN,
    CLOSED,
    PENDING,
    CANCELED;

    /**
     * @param i The integer that indicates the enum type.
     * @return the enum type corresponding to the integer past.
     */
    @NonNull
    public static ReportStatus getValue(Integer i) {
        i = i % 4;
        switch (i) {
            case 0:
                return OPEN;
            case 1:
                return PENDING;
            case 2:
                return CLOSED;
            case 3:
                return CANCELED;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return The integer that corresponding to the enum type.
     */
    @NonNull
    public Integer toInt() {
        switch (this) {
            case OPEN:
                return 0;
            case PENDING:
                return 1;
            case CLOSED:
                return 2;
            case CANCELED:
                return 3;
            default:
                throw new IllegalArgumentException();
        }
    }
}

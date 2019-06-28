package com.fsc.cicerone;

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
                return null;
        }
    }
}

package com.fsc.cicerone.model;

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

    /**
     * @param state The report status.
     * @return  The integer that corresponding to the enum type.
     */
    public static Integer getInt (ReportStatus state){
        switch (state){
            case OPEN:
                return 0;
            case PENDING:
                return 1;
            case CLOSED:
                return 2;
            case CANCELED:
                return 3;
            default:
                    return null;
        }
    }
}

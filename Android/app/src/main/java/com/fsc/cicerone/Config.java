package com.fsc.cicerone;

public abstract class Config {
    private Config() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SHARED_PREF_KEY = "com.fsc.cicerone";
}

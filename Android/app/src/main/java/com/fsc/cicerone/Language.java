package com.fsc.cicerone;

import java.util.Objects;

/**
 * A language as represented in the system Cicerone. A language is composed by a code and a name
 * and it's identified by its code.
 */
public class Language {
    private String code;
    private String name;

    /**
     * Default empty constructor.
     */
    public Language() {
    }

    /**
     * Create a new language by specifying its values.
     *
     * @param code The language's code.
     * @param name The language's name.
     */
    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Get the language's code.
     *
     * @return The language's code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Set the language's code.
     *
     * @param code The new language's code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Get the language's name.
     *
     * @return The language's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the language's name.
     *
     * @param name The new language's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if two languages are the same language. The check is made by the two languages' codes.
     *
     * @param o The other language Object.
     * @return true if the two languages are equals (by code), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return Objects.equals(code, language.code) &&
                Objects.equals(name, language.name);
    }

    /**
     * Generate an hash code for the object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}

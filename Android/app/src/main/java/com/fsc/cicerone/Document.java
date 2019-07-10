package com.fsc.cicerone;

import java.util.Date;
import java.util.Objects;

/**
 * A document as represented in Cicerone. Each document is identified by a number.
 */
public class Document {
    private String number;
    private String type;
    private Date expiryDate;

    /**
     * Default empty constructor.
     */
    public Document() {
    }

    /**
     * Create a new document by specifying its values.
     *
     * @param number     The document's number.
     * @param type       The document's type.
     * @param expiryDate The document's expiration date.
     */
    public Document(String number, String type, Date expiryDate) {
        this.number = number;
        this.type = type;
        this.expiryDate = expiryDate;
    }

    /**
     * Get the document's number.
     *
     * @return The document's number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the document's number.
     *
     * @param number The new document's number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Get the document's type.
     *
     * @return The document's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the document's type.
     *
     * @param type The new document's type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the document's expiration date.
     *
     * @return The document's expiration date.
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * Set the document's expiration date.
     *
     * @param expiryDate The new document's expiration date.
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Check if two documents are equals.
     *
     * @param o The other document Object.
     * @return true if the two documents are equals, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return number.equals(document.number) &&
                type.equals(document.type) &&
                expiryDate.equals(document.expiryDate);
    }

    /**
     * Generate an hash code for the object.
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(number, type, expiryDate);
    }
}

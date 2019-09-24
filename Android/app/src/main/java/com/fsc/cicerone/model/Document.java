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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.fsc.cicerone.app_connector.ConnectorConstants;

/**
 * A document as represented in Cicerone. Each document is identified by a number.
 */
public class Document extends BusinessEntity {
    private String number;
    private String type;
    private Date expirationDate;

    /**
     * Default empty constructor.
     */
    public Document() {
    }

    public Document(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    public Document(String json) {
        this(getJSONObject(json));
    }

    /**
     * Create a new document by specifying its values.
     *
     * @param number     The document's number.
     * @param type       The document's type.
     * @param expirationDate The document's expiration date.
     */
    public Document(String number, String type, Date expirationDate) {
        this.number = number;
        this.type = type;
        this.expirationDate = expirationDate;
    }

    /**
     * Create a new document by specifying its values.
     *
     * @param number     The document's number.
     * @param type       The document's type.
     * @param expirationDate The document's expiration date.
     */
    public Document(String number, String type, String expirationDate) {
        this.number = number;
        this.type = type;
        try {
            this.expirationDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US).parse(expirationDate);
        } catch (ParseException e) {
            this.expirationDate = null;
        }
    }

    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        try {
            number = jsonObject.getString("document_number");
        } catch (JSONException e) {
            number = "";
        }
        try {
            type = jsonObject.getString("document_type");
        } catch (JSONException e) {
            type = "";
        }
        try {
            expirationDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse(jsonObject.getString("expiry_date"));
        } catch (ParseException | JSONException e) {
            expirationDate = new Date();
        }
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
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Set the document's expiration date.
     *
     * @param expirationDate The new document's expiration date.
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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
                expirationDate.equals(document.expirationDate);
    }

    /**
     * Generate an hash code for the object.
     *
     * @return The hash code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(number, type, expirationDate);
    }

    /**
     * Convert the document to a JSON Object
     *
     * @return A JSON Object containing the data that were stored in the object.
     */
    public JSONObject toJSONObject() {
        JSONObject doc = new JSONObject();
        try {
            doc.put("document_number", this.number);
            doc.put("document_type", this.type);
            doc.put("expiry_date", new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.expirationDate));
        } catch (JSONException e) {
            doc = null;
        }
        return doc;
    }
}

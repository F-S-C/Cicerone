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

import android.util.Patterns;

import com.fsc.cicerone.app_connector.ConnectorConstants;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * An <i>entity</i> class that stores the data of a user.
 */
@SuppressWarnings("WeakerAccess")
public class User extends BusinessEntity {

    private String name;
    private String surname;
    private String email;
    private String password;
    private Sex sex;
    private String taxCode;
    private String username;
    private UserType userType;
    private String cellphone;
    private Date birthDate;

    private Document document;
    private Set<Language> languages;

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String USERNAME_KEY = "username";
        public static final String PASSWORD_KEY = "password";
        public static final String TAX_CODE_KEY = "tax_code";
        public static final String NAME_KEY = "name";
        public static final String SURNAME_KEY = "surname";
        public static final String EMAIL_KEY = "email";
        public static final String USER_TYPE_KEY = "user_type";
        public static final String CELLPHONE_KEY = "cellphone";
        public static final String BIRTH_DATE_KEY = "birth_date";
        public static final String SEX_KEY = "sex";
        public static final String DOCUMENT_KEY = "document";
        public static final String LANGUAGES_KEY = "languages";
    }

    public static class Credentials extends BusinessEntity {
        private String username;
        private String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Credentials(Map<String, Object> jsonObject) {
            loadFromMap(jsonObject);
        }

        public Credentials(String json) {
            this(getMapFromJson(json));
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean isValid() {
            return username != null && password != null && !username.isEmpty() && !password.isEmpty();
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> jsonObject = new HashMap<>();
            if (username != null) jsonObject.put(Columns.USERNAME_KEY, username);
            if (password != null) jsonObject.put(Columns.PASSWORD_KEY, password);
            return jsonObject;
        }

        @Override
        protected void loadFromMap(Map<String, Object> jsonObject) {
            username = (String) jsonObject.get(Columns.USERNAME_KEY);
            password = (String) jsonObject.get(Columns.PASSWORD_KEY);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Default empty constructor.
     */
    public User() {
        // Automatically set everything to a default value
        languages = new HashSet<>();
    }

    /**
     * Create a new User object based on a JSON Object. The JSON Object <i>must</i> contain <i>at
     * least</i> the following values:
     * <pre>
     * {@code
     * {
     *     "username": "test",
     *     "tax_code": "testtaxcode",
     *     "name": "Nome",
     *     "surname": "Cognome",
     *     "password": "password",
     *     "email": "email@email.com",
     *     "user_type": "1",
     *     "cellphone": "0123456789",
     *     "birth_date": "2010-01-20",
     *     "sex": "male"
     * }
     * }
     * </pre>
     *
     * @param jsonObject The JSON object from which data will be fetched.
     */
    public User(Map<String, Object> jsonObject) {
        loadFromMap(jsonObject);
    }

    public User(String json) {
        this(getMapFromJson(json));
    }


    @Override
    protected void loadFromMap(Map<String, Object> user) {
        name = (String) user.get(Columns.NAME_KEY);

        surname = (String) user.get(Columns.SURNAME_KEY);

        email = (String) user.get(Columns.EMAIL_KEY);

        password = (String) user.get(Columns.PASSWORD_KEY);

        sex = Sex.getValue((String) user.get(Columns.SEX_KEY));

        taxCode = (String) user.get(Columns.TAX_CODE_KEY);

        username = (String) user.get(Columns.USERNAME_KEY);

        userType = UserType.getValue((Integer) user.get(Columns.USER_TYPE_KEY));

        cellphone = (String) user.get(Columns.CELLPHONE_KEY);

        try {
            birthDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).parse((String) user.get(Columns.BIRTH_DATE_KEY));
        } catch (ParseException e) {
            birthDate = null;
        }

        document = new Document((String) user.get(Columns.DOCUMENT_KEY));

        languages = Language.getSetFromJSONArray((String) user.get(Columns.LANGUAGES_KEY));
    }

    /**
     * Get the user's name.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the user's name
     *
     * @param name The new user's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the user's surname.
     *
     * @return The user's surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the user's surname.
     *
     * @param surname The new user's surname.
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Get the user's email.
     *
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the user's email.
     *
     * @param email The new user's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the user's password.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     *
     * @param password The new user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the user's sex.
     *
     * @return The user's sex.
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Set the user's sex.
     *
     * @param sex The new user's sex.
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * Get the user's tax code.
     *
     * @return The user's tax code.
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * Set the user's tax code.
     *
     * @param taxCode The new user's tax code.
     */
    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    /**
     * Get the user's username.
     *
     * @return The user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the user's type.
     *
     * @return The user's type.
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Set the user's type.
     *
     * @param userType The new user's type.
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    /**
     * Get the user's cellphone number.
     *
     * @return The user's cellphone number.
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * Set the user's cellphone number.
     *
     * @param cellphone The new user's cellphone number.
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    /**
     * Get the user's birth date.
     *
     * @return The user's birth date.
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * Set the user's birth date.
     *
     * @param birthDate The new user's birth date.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Get the user's current (last added and valid) document.
     *
     * @return The user's current document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Set the user's current (last added and valid) document.
     *
     * @param document The new user's current document.
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * Get all the user's languages.
     *
     * @return A set containing all the user's languages.
     */
    public Set<Language> getLanguages() {
        return languages;
    }

    /**
     * Add a language to the user's languages' list if it doesn't exists, otherwise do nothing.
     *
     * @param language The language to be added.
     */
    public void addLanguage(Language language) {
        this.languages.add(language);
    }

    /**
     * Remove a language from the user's languages' list if it exists, otherwise do nothing.
     *
     * @param language The language to be removed.
     */
    public void removeLanguage(Language language) {
        this.languages.remove(language);
    }

    /**
     * Convert the user to a JSON Object.
     *
     * @return A JSON Object containing the data that were stored in the object.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        if (this.name != null) result.put(Columns.NAME_KEY, this.name);
        if (this.surname != null) result.put(Columns.SURNAME_KEY, this.surname);
        if (this.email != null) result.put(Columns.EMAIL_KEY, this.email);
        if (this.password != null) result.put(Columns.PASSWORD_KEY, this.password);
        if (this.sex != null) result.put(Columns.SEX_KEY, this.sex.toString());
        if (this.taxCode != null) result.put(Columns.TAX_CODE_KEY, this.taxCode);
        if (this.username != null) result.put(Columns.USERNAME_KEY, this.username);
        if (this.userType != null) result.put(Columns.USER_TYPE_KEY, this.userType.toInt());
        if (this.cellphone != null) result.put(Columns.CELLPHONE_KEY, this.cellphone);
        if (this.birthDate != null)
            result.put(Columns.BIRTH_DATE_KEY, new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US).format(this.birthDate));
        if (document != null)
            result.put(Columns.DOCUMENT_KEY, this.document.toMap());
        if (languages != null) result.put(Columns.LANGUAGES_KEY, new JSONArray(languages));
        return result;
    }

    /**
     * Get the user's credentials (username and password).
     *
     * @return A Credentials Object that contains the user's username and password.
     */
    public Credentials getCredentials() {
        return new Credentials(username, password);
    }

    /**
     * A function that validates a username. Check that the username is correctly formatted.
     *
     * @param usernameInput The username to check.
     * @return True if username is valid. False if isn't.
     */
    public static boolean validateUsername(String usernameInput) {
        return (Pattern.compile("[a-zA-Z._-]{4,20}").matcher(usernameInput).matches());
    }

    /**
     * A function that validates a email. Check that the email is correctly formatted.
     *
     * @param emailInput The username to check.
     * @return True if email is valid. False if isn't.
     */
    public static boolean validateEmail(String emailInput) {
        return (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches());
    }

    public User(Builder builder) {
        this.name = builder.name;
        this.surname = builder.surname;
        this.email = builder.email;
        this.password = builder.password;
        this.sex = builder.sex;
        this.taxCode = builder.taxCode;
        this.username = builder.username;
        this.userType = builder.userType;
        this.cellphone = builder.cellphone;
        this.birthDate = builder.birthDate;
        this.document = builder.document;
        this.languages = builder.languages;
    }

    public static class Builder {
        private String name;
        private String surname;
        private String email;
        private String password;
        private Sex sex;
        private String taxCode;
        private String username;
        private UserType userType;
        private String cellphone;
        private Date birthDate;

        private Document document;
        private Set<Language> languages;

        public Builder(String username, String password) {
            this.password = password;
            this.username = username;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setSex(Sex sex) {
            this.sex = sex;
            return this;
        }

        public Builder setTaxCode(String taxCode) {
            this.taxCode = taxCode;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setUserType(UserType userType) {
            this.userType = userType;
            return this;
        }

        public Builder setCellphone(String cellphone) {
            this.cellphone = cellphone;
            return this;
        }

        public Builder setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder setDocument(Document document) {
            this.document = document;
            return this;
        }

        public Builder setLanguages(Set<Language> languages) {
            this.languages = languages;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

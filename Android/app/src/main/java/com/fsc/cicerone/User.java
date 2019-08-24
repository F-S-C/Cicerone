package com.fsc.cicerone;

import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * An <i>entity</i> class that stores the data of a user.
 */
public class User {

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

    private Document currentDocument;
    private Set<Document> documents;

    private Set<Language> languages;

    /**
     * Default empty constructor.
     */
    public User() {
        // Automatically set everything to a default value
    }

    /**
     * Create a new User object based on a JSON Object.
     * The JSON Object <i>must</i> contain <i>at least</i> the following values:
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
     * @param user The JSON object from which data will be fetched.
     */
    public User(JSONObject user) {
        try {
            name = user.getString("name");
        } catch (JSONException e) {
            name = null;
        }

        try {
            surname = user.getString("surname");
        } catch (JSONException e) {
            surname = null;
        }

        try {
            email = user.getString("email");
        } catch (JSONException e) {
            email = null;
        }

        try {
            password = user.getString("password");
        } catch (JSONException e) {
            password = null;
        }

        try {
            sex = Sex.getValue(user.getString("sex"));
        } catch (JSONException e) {
            sex = null;
        }

        try {
            taxCode = user.getString("tax_code");
        } catch (JSONException e) {
            taxCode = null;
        }

        try {
            username = user.getString("username");
        } catch (JSONException e) {
            username = null;
        }

        try {
            userType = UserType.getValue(user.getInt("user_type"));
        } catch (JSONException e) {
            userType = null;
        }

        try {
            cellphone = user.getString("cellphone");
        } catch (JSONException e) {
            cellphone = null;
        }

        try {
            birthDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(user.getString("birth_date"));
        } catch (JSONException | ParseException e) {
            birthDate = null;
        }
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
     * @return The user's sex (see {@see Sex}).
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Set the user's sex.
     *
     * @param sex The new user's sex (see {@see Sex}).
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
     * Set the user's username.
     *
     * @param username The new user's username.
     */
    public void setUsername(String username) {
        // TODO: check for uniqueness
        this.username = username;
    }

    /**
     * Get the user's type.
     *
     * @return The user's type (see {@see UserType}).
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Set the user's type.
     *
     * @param userType The new user's type (see {@see UserType}).
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
    public Document getCurrentDocument() {
        return currentDocument;
    }

    /**
     * Set the user's current (last added and valid) document.
     *
     * @param currentDocument The new user's current document.
     */
    public void setCurrentDocument(Document currentDocument) {
        this.currentDocument = currentDocument;
    }

    /**
     * Get all the user's documents.
     *
     * @return A set containing all the user's documents.
     */
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * Add a document to the user's documents' list if it doesn't exists, otherwise do nothing.
     *
     * @param document The document to be added.
     */
    public void addDocument(Document document) {
        this.documents.add(document);
    }

    /**
     * Remove a document from the user's documents' list if it exists, otherwise do nothing.
     *
     * @param document The document to be removed.
     */
    public void removeDocument(Document document) {
        this.documents.remove(document);
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
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        try {
            result.put("name", this.name);
            result.put("surname", this.surname);
            result.put("email", this.email);
            result.put("password", this.password);
            result.put("sex", this.sex.toString());
            result.put("tax_code", this.taxCode);
            result.put("username", this.username);
            result.put("user_type", this.userType.toInt());
            result.put("cellphone", this.cellphone);
            result.put("birth_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.birthDate));
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    /**
     * Get the user's credentials (username and password).
     *
     * @return A JSON Object that contains the user's username and password.
     */
    public JSONObject getCredentials() {
        JSONObject result = new JSONObject();
        try {
            result.put("password", this.password);
            result.put("username", this.username);
        } catch (JSONException e) {
            result = null;
        }
        return result;
    }

    /**
     * A function that validates a username. Check that the username is correctly formatted.
     * @param usernameInput The username to check.
     * @return True if username is valid. False if isn't.
     */
    public static boolean validateUsername(String usernameInput) {
        return (Pattern.compile("[a-zA-Z._-]{4,20}").matcher(usernameInput).matches());
    }

    /**
     * A function that validates a email. Check that the email is correctly formatted.
     * @param emailInput The username to check.
     * @return True if email is valid. False if isn't.
     */
    public static boolean validateEmail(String emailInput) {
        return (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches());
    }
}

package com.fsc.cicerone.manager;

import android.util.Log;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Language;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.GetDataConnector;

/**
 * LanguageManager is used to manage multiple instances of the Language class and to interact
 * with the server.
 */
public class LanguageManager {

    private static List<Language> langs;
    private static final String ERROR_TAG = "ERROR IN " + LanguageManager.class.getName();

    /**
     * LanguageManager class constructor. Create an array with the list of languages stored
     * on the server.
     */
    public LanguageManager() {
        GetDataConnector<Language> request = new GetDataConnector<>(
                ConnectorConstants.REQUEST_LANGUAGES,
                BusinessEntityBuilder.getFactory(Language.class),
                new DatabaseConnector.CallbackInterface<Language>() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(List<Language> list) {
                        langs = list;
                    }
                });
        request.execute();
    }

    /**
     * Returns the list of stored language names.
     *
     * @return The list of language names.
     */
    public List<String> getLanguagesNames() {
        List<String> languageList = new ArrayList<>();
        for (int i = 0; i < langs.size(); i++) {
            languageList.add(langs.get(i).getName());
        }
        HashSet<String> hashSet = new HashSet<>(languageList);
        languageList.clear();
        languageList.addAll(hashSet);
        return languageList;
    }

    /**
     * Return the list of languages starting from a list of language names.
     *
     * @param names List of language names.
     * @return List of languages.
     */
    public List<Language> getLanguagesFromNames(List<String> names) {
        List<Language> returnLangs = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>(names);
        names.clear();
        names.addAll(hashSet);
        for (int i = 0; i < langs.size(); i++) {
            if (names.contains(langs.get(i).getName())) {
                returnLangs.add(langs.get(i));
            }
        }
        return returnLangs;
    }

    /**
     * Set the languages known by the user in the database.
     *
     * @param username      Username.
     * @param languageToSet List of languages.
     */
    public void setUserLanguages(String username, List<Language> languageToSet) {
        try {
            JSONObject data = new JSONObject();
            data.put("username", username);
            for (int i = 0; i < languageToSet.size(); i++) {
                data.put("language_code", languageToSet.get(i).getCode());
                Log.i("LANGUAGE", data.toString());
                new BooleanConnector(
                        ConnectorConstants.INSERT_USER_LANGUAGE,
                        new BooleanConnector.CallbackInterface() {
                            @Override
                            public void onStartConnection() {
                                //Do nothing
                            }

                            @Override
                            public void onEndConnection(BooleanConnector.BooleanResult result) {
                                if (!result.getResult())
                                    Log.e("ERROR INSERT LANGUAGE", result.getMessage());

                            }
                        },
                        new JSONObject(data.toString()))
                        .execute();
                data.remove("language_code");
            }
            languageToSet.size();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

}
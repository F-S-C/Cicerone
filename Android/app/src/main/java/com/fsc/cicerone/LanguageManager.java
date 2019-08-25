package com.fsc.cicerone;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.GetDataConnector;
import app_connector.SendInPostConnector;

/**
 * LanguageManager is used to manage multiple instances of the Language class and to interact
 * with the server.
 */
public class LanguageManager {

    private static ArrayList<Language> langs = new ArrayList<>();
    private static final String ERROR_TAG = "ERROR IN " + LanguageManager.class.getName();

    /**
     * LanguageManager class constructor. Create an array with the list of languages stored
     * on the server.
     */
    public LanguageManager() {
        GetDataConnector request = new GetDataConnector(ConnectorConstants.REQUEST_LANGUAGES, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                for (int i = 0; i < jsonArray.length(); i++) {
                    langs.add(new Language(jsonArray.getJSONObject(i)));
                }
            }
        });
        request.execute();
    }

    /**
     * Returns the list of stored language names.
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
     * @param names List of language names.
     * @return List of languages.
     */
    public List<Language> getLanguagesFromNames(List<String> names){
        List<Language> returnLangs = new ArrayList<>();
        HashSet<String> hashSet = new HashSet<>(names);
        names.clear();
        names.addAll(hashSet);
        for( int i = 0; i < langs.size(); i++){
           if(names.contains(langs.get(i).getName())) {
               returnLangs.add(langs.get(i));
           }
        }
        return returnLangs;
    }

    /**
     * Set the languages known by the user in the database.
     * @param username Username.
     * @param languageToSet List of languages.
     */
    public void setUserLanguages(String username, List<Language> languageToSet){
        try {
            JSONObject data = new JSONObject();
            data.put("username", username);
            for( int i = 0; i < languageToSet.size(); i++)
            {
                data.put("language_code",languageToSet.get(i).getCode());
                Log.i("LANGUAGE",data.toString());
                new SendInPostConnector(ConnectorConstants.INSERT_USER_LANGUAGE, new DatabaseConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        //Do nothing
                    }

                    @Override
                    public void onEndConnection(JSONArray jsonArray) throws JSONException {
                        if(!jsonArray.getJSONObject(0).getBoolean("result"))
                            Log.e("ERROR INSERT LANGUAGE",jsonArray.getJSONObject(0).getString("error"));

                    }
                }, new JSONObject(data.toString())).execute();
                data.remove("language_code");
            }
            languageToSet.size();
        }catch (JSONException e){
            Log.e(ERROR_TAG, e.toString());
        }
    }

}
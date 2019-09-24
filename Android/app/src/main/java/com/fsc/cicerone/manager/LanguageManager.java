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

package com.fsc.cicerone.manager;

import android.util.Log;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.GetDataConnector;

/**
 * LanguageManager is used to manage multiple instances of the Language class and to interact
 * with the server.
 */
public class LanguageManager {

    private static List<Language> langs;

    /**
     * LanguageManager class constructor. Create an array with the list of languages stored
     * on the server.
     */
    public LanguageManager() {
        GetDataConnector<Language> request = new GetDataConnector.Builder<>(ConnectorConstants.REQUEST_LANGUAGES, BusinessEntityBuilder.getFactory(Language.class))
                .setOnEndConnectionListener(list -> langs = list)
                .build();

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
        Map<String, Object> data = new HashMap<>(languageToSet.size() + 1);
        data.put("username", username);
        for (int i = 0; i < languageToSet.size(); i++) {
            data.put("language_code", languageToSet.get(i).getCode());
            Log.i("LANGUAGE", data.toString());
            new BooleanConnector.Builder(ConnectorConstants.INSERT_USER_LANGUAGE)
                    .setContext(null)
                    .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                        if (!result.getResult())
                            Log.e("ERROR INSERT LANGUAGE", result.getMessage());
                    })
                    .setObjectToSend(data)
                    .build()
                    .execute();
        }
        languageToSet.size();
    }

}
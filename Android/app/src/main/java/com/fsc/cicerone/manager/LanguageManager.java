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

import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.GetDataConnector;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Language;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A class that provides useful functions for the management of the languages.
 */
public class LanguageManager {

    private List<Language> languages;

    /**
     * LanguageManager class constructor. Create an array with the list of languages stored on the
     * server.
     */
    public LanguageManager() {
        new GetDataConnector.Builder<>(ConnectorConstants.REQUEST_LANGUAGES, BusinessEntityBuilder.getFactory(Language.class))
                .setOnEndConnectionListener(list -> languages = list)
                .build()
                .getData();
    }

    /**
     * Returns the list of stored language names.
     *
     * @return The list of language names.
     */
    public List<String> getLanguagesNames() {
        HashSet<String> hashSet = new HashSet<>(languages.size());
        for (Language language : languages)
            hashSet.add(language.getName());
        return new ArrayList<>(hashSet);
    }

    /**
     * Return the list of languages starting from a list of language names.
     *
     * @param names List of language names.
     * @return List of languages.
     */
    public List<Language> getLanguagesFromNames(List<String> names) {
        List<Language> toReturn = new ArrayList<>(names.size());
        HashSet<String> hashSet = new HashSet<>(names); // Remove duplicates
        for (Language language : languages) {
            if (hashSet.contains(language.getName())) {
                toReturn.add(language);
            }
        }
        return toReturn;
    }

}
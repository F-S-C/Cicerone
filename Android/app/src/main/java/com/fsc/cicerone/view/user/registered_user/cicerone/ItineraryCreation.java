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

package com.fsc.cicerone.view.user.registered_user.cicerone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ItineraryManager;
import com.fsc.cicerone.model.Language;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.view.itinerary.ItineraryModifier;
import com.fsc.cicerone.view.user.UserMainActivity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * A class that allows the creation of an Itinerary
 */
public class ItineraryCreation extends ItineraryModifier {

    private class LanguageCheckBox {
        private CheckBox checkBox;
        private Language language;

        /**
         * A function that returns the Language CheckBox
         *
         * @return The language CheckBox to return.
         */
        public CheckBox getCheckBox() {
            return checkBox;
        }

        /**
         * A function that returns the Language.
         *
         * @return The language to return.
         */
        public Language getLanguage() {
            return language;
        }

        /**
         * A Constructor that sets CheckBox and Language.
         *
         * @param checkBox The CheckBox to set.
         * @param language The Language to Set.
         */
        public LanguageCheckBox(CheckBox checkBox, Language language) {
            this.checkBox = checkBox;
            this.language = language;
        }

        /**
         * A function that controls if the CheckBox is checked or not.
         *
         * @return True if the Checkbox is checked, False otherwise.
         */
        public boolean isChecked() {
            return checkBox.isChecked();
        }
    }

    private List<LanguageCheckBox> checkBoxList;


    /**
     * Empty Constructor.
     */
    public ItineraryCreation() {
        this.layout = R.layout.activity_itinerary_creation;
    }

    /**
     * A Constructor that takes a Layout as a parameter.
     *
     * @param contentLayoutId The Layout to set.
     */
    public ItineraryCreation(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_itinerary_creation;
    }

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image.setOnClickListener(v -> imageManager.selectImage());
        setLanguages();

    }

    /**
     * @see android.app.Activity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManager.manageResult(requestCode, resultCode, data, image);
    }

    /**
     * A functtion that checks if at least a Language has been selected.
     *
     * @return True if a Language is set, False otherwise.
     */
    public boolean checkIfLanguagesAreSet() {
        for (LanguageCheckBox language : checkBoxList) {
            if (language.isChecked()) return true;
        }
        return false;
    }

    /**
     * @see com.fsc.cicerone.view.itinerary.ItineraryModifier#sendData(View)
     */
    @Override
    public void sendData(View view) {
        if (allFilled() && checkIfLanguagesAreSet() && noFieldWithError()) {
            Set<Language> languages = new HashSet<>();
            for (LanguageCheckBox checkBox : checkBoxList)
                if (checkBox.isChecked()) languages.add(checkBox.getLanguage());
            submit.setEnabled(false);
            submit.setText(R.string.loading);
            imageManager.upload(response -> {
                if (response.getResult()) {
                    String imgURL = ConnectorConstants.IMG_FOLDER.concat(response.getMessage());
                    ItineraryManager.uploadItinerary(
                            title.getText().toString(),
                            description.getText().toString(),
                            selectBeginningDate.getText().toString(),
                            selectEndingDate.getText().toString(),
                            selectReservationDate.getText().toString(),
                            location.getText().toString(),
                            durationHours.getText().toString() + ":" + durationMinutes.getText().toString(),
                            Integer.parseInt(repetitions.getText().toString()),
                            Integer.parseInt(minParticipants.getText().toString()),
                            Integer.parseInt(maxParticipants.getText().toString()),
                            Float.parseFloat(fullPrice.getText().toString()),
                            Float.parseFloat(reducedPrice.getText().toString()),
                            languages,
                            imgURL,
                            insStatus -> {
                                if (insStatus.getResult()) {
                                    Toast.makeText(ItineraryCreation.this, getString(R.string.itinerary_added), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent().setClass(ItineraryCreation.this, UserMainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(ItineraryCreation.this, getString(R.string.error_during_operation) + ":" + insStatus.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(ItineraryCreation.this, getString(R.string.error_during_operation) + ":" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            if (title.getText().toString().equals(""))
                title.setError(getString(R.string.empty_title_error));
            if (description.getText().toString().equals(""))
                description.setError(getString(R.string.empty_description_itineary_error));
            if (selectBeginningDate.getText().toString().equals(""))
                selectBeginningDate.setError(getString(R.string.empty_beginningdate_error));
            if (selectEndingDate.getText().toString().equals(""))
                selectEndingDate.setError(getString(R.string.empty_endingdate_error));
            if (selectReservationDate.getText().toString().equals(""))
                selectReservationDate.setError(getString(R.string.empty_reservationdate_error));
            if (location.getText().toString().equals(""))
                location.setError(getString(R.string.empty_location_error));
            if (repetitions.getText().toString().equals(""))
                repetitions.setError(getString(R.string.empty_repetitions_error));
            if (durationHours.getText().toString().equals(""))
                durationHours.setError(getString(R.string.empty_duration_hours_error));
            if (durationMinutes.getText().toString().equals(""))
                durationMinutes.setError(getString(R.string.empty_duration_minutes_error));
            if (maxParticipants.getText().toString().equals(""))
                maxParticipants.setError(getString(R.string.empty_max_participants_error));
            if (minParticipants.getText().toString().equals(""))
                minParticipants.setError(getString(R.string.empty_min_participants_error));
            if (fullPrice.getText().toString().equals(""))
                fullPrice.setError(getString(R.string.empty_fullprice_error));
            if (reducedPrice.getText().toString().equals(""))
                reducedPrice.setError(getString(R.string.empty_reduced_price_error));
            if (!imageManager.isSelected())
                Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.empty_image_error), Toast.LENGTH_SHORT).show();
            if (!checkIfLanguagesAreSet())
                Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.empty_language), Toast.LENGTH_SHORT).show();
            if (!noFieldWithError())
                Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_wrong_fields), Toast.LENGTH_SHORT).show();


        }
    }

    /**
     * @see ItineraryModifier#allFilled()
     */
    @Override
    protected boolean allFilled() {
        return super.allFilled() && imageManager.isSelected();
    }

    /**
     * A function that manage to set the languages in the CheckBox. The languages are taken from the
     * User's languages.
     */
    private void setLanguages() {
        LinearLayout languagesLayout = findViewById(R.id.languageLayout);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();
        checkBoxList = new LinkedList<>();

        for (Language language : currentLoggedUser.getLanguages()) {
            CheckBox selectLanguage = new CheckBox(ItineraryCreation.this);
            selectLanguage.setText(language.getName());
            checkBoxList.add(new LanguageCheckBox(selectLanguage, language));
            languagesLayout.addView(selectLanguage);
        }
    }
}



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

package com.fsc.cicerone.view;

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

import java.util.LinkedList;
import java.util.List;


public class ItineraryCreation extends ItineraryModifier {

    LinearLayout languagesLayout;
    User currentLoggedUser;
    List<CheckBox> checkBoxList;


    public ItineraryCreation() {
        this.layout = R.layout.activity_itinerary_creation;
    }

    public ItineraryCreation(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_itinerary_creation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image.setOnClickListener(v -> imageManager.selectImage());
         setLanguages();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManager.manageResult(requestCode, resultCode, data, image);
    }

    @Override
    public void sendData(View view) {
        if (allFilled()) {
            if(this.layout == R.layout.activity_itinerary_creation)
            {
                for(CheckBox language : checkBoxList)
                {
                    if(language.isSelected())
                }
            }
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
        }
    }

    @Override
    boolean allFilled() {
        return super.allFilled() && imageManager.isSelected();
    }

    private void setLanguages()
    {

        for(Language language : currentLoggedUser.getLanguages())
        {
            languagesLayout = findViewById(R.id.languageLayout);
            currentLoggedUser = AccountManager.getCurrentLoggedUser();
            checkBoxList = new LinkedList<>();
            CheckBox selectLanguage = new CheckBox(ItineraryCreation.this);
            checkBoxList.add(selectLanguage);
            languagesLayout.addView(selectLanguage);
            selectLanguage.setText(language.getName());

        }
    }
}



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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.ItineraryManager;
import com.fsc.cicerone.model.Itinerary;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class ItineraryUpdate extends ItineraryModifier {
    Itinerary currentItinerary;

    public static final int RESULT_ITINERARY_UPDATED = 1050;

    public ItineraryUpdate() {
        this.layout = R.layout.activity_itinerary_update;
    }

    public ItineraryUpdate(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_itinerary_update;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        currentItinerary = new Itinerary(Objects.requireNonNull(bundle).getString("itinerary"));


        // Setting all the fields to the previous values
        title.setText(currentItinerary.getTitle());
        description.setText(currentItinerary.getDescription());
        location.setText(currentItinerary.getLocation());
        // TODO: Why is it crashing?
        //SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        //selectBeginningDate.setText(String.valueOf(currentItinerary.getBeginningDate()));
        //selectEndingDate.setText(formatter.format(currentItinerary.getEndingDate()));
        //selectReservationDate.setText(formatter.format(currentItinerary.getReservationDate()));
        minParticipants.setText(String.valueOf(currentItinerary.getMinParticipants()));
        maxParticipants.setText(String.valueOf(currentItinerary.getMaxParticipants()));
        durationHours.setText(currentItinerary.getDuration().substring(0, currentItinerary.getDuration().indexOf(":")));
        durationMinutes.setText(currentItinerary.getDuration().substring(currentItinerary.getDuration().indexOf(":") + 1));
        repetitions.setText(String.valueOf(currentItinerary.getRepetitions()));
        if(currentItinerary.getImageUrl() != null && !currentItinerary.getImageUrl().equals("")) {
            Picasso.get().load(currentItinerary.getImageUrl()).into(image);
        }
        fullPrice.setText(String.valueOf(currentItinerary.getFullPrice()));
        reducedPrice.setText(String.valueOf(currentItinerary.getReducedPrice()));
    }

    @Override
    public void sendData(View view) throws ParseException {
        DateFormat outputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        if (allFilled()) {
            submit.setEnabled(false);
            submit.setText(R.string.loading);
            currentItinerary.setTitle(title.getText().toString());
            currentItinerary.setDescription(description.getText().toString());
            currentItinerary.setBeginningDate(outputFormat.parse(selectBeginningDate.getText().toString()));
            currentItinerary.setEndingDate(outputFormat.parse(selectEndingDate.getText().toString()));
            currentItinerary.setReservationDate(outputFormat.parse(selectReservationDate.getText().toString()));
            currentItinerary.setMaxParticipants(Integer.parseInt(maxParticipants.getText().toString()));
            currentItinerary.setMinParticipants(Integer.parseInt(minParticipants.getText().toString()));
            currentItinerary.setRepetitions(Integer.parseInt(repetitions.getText().toString()));
            currentItinerary.setLocation(location.getText().toString());
            currentItinerary.setDuration(durationHours.getText().toString() + ":" + durationMinutes.getText().toString() + ":00");
            currentItinerary.setFullPrice(Float.parseFloat(fullPrice.getText().toString()));
            currentItinerary.setReducedPrice(Float.parseFloat(reducedPrice.getText().toString()));
            if(imageManager.isSelected()){
                imageManager.upload(response -> {
                    if (response.getResult()) {
                        currentItinerary.setImageUrl(ConnectorConstants.IMG_FOLDER.concat(response.getMessage()));
                        ItineraryManager.updateItinerary(this, currentItinerary, success -> {
                            if (success)
                                Toast.makeText(ItineraryUpdate.this, getString(R.string.itinerary_updated), Toast.LENGTH_LONG).show();
                        });
                    }else{
                        Toast.makeText(ItineraryUpdate.this, getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ItineraryManager.updateItinerary(this, currentItinerary, success -> {
                    if (success)
                        Toast.makeText(ItineraryUpdate.this, getString(R.string.itinerary_updated), Toast.LENGTH_LONG).show();
                });
            }
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
        }
    }
}

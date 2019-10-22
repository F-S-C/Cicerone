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

package com.fsc.cicerone.view.itinerary;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.ImageManager;
import com.fsc.cicerone.app_connector.ConnectorConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public abstract class ItineraryModifier extends AppCompatActivity {
    protected EditText title;
    protected EditText description;
    protected EditText selectBeginningDate;
    protected EditText selectEndingDate;
    protected EditText selectReservationDate;
    protected EditText minParticipants;
    protected EditText maxParticipants;
    protected EditText repetitions;
    protected EditText durationHours;
    protected EditText durationMinutes;
    protected EditText location;
    protected EditText fullPrice;
    protected EditText reducedPrice;
    protected ImageView image;
    protected Button submit;
    protected ImageManager imageManager;

    protected int layout;

    protected static final String DATE_FORMAT = ConnectorConstants.DATE_FORMAT;
    protected final Calendar myCalendar = Calendar.getInstance();

    private static final String ERROR_TAG = "ERROR IN " + ItineraryModifier.class.getSimpleName();

    public ItineraryModifier() {
    }

    public ItineraryModifier(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        title = findViewById(R.id.inputTitle);
        description = findViewById(R.id.inputDescription);
        location = findViewById(R.id.inputLocation);
        selectBeginningDate = findViewById(R.id.inputBeginningDate);
        selectEndingDate = findViewById(R.id.inputEndingDate);
        selectReservationDate = findViewById(R.id.inputReservationDate);
        minParticipants = findViewById(R.id.inputMinimumParticipants);
        maxParticipants = findViewById(R.id.inputMaximumPartecipants);
        durationHours = findViewById(R.id.inputDurationHours);
        durationMinutes = findViewById(R.id.inputDurationMinutes);
        repetitions = findViewById(R.id.inputRepetitions);
        reducedPrice = findViewById(R.id.inputReducedPrice);
        fullPrice = findViewById(R.id.inputFullPrice);
        submit = findViewById(R.id.submit);
        image = findViewById(R.id.itinerary_image2);
        imageManager = new ImageManager(this);

        final ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        setUpListeners();
    }

    private void setUpListeners() {
        submit.setOnClickListener(view -> {
            try {
                sendData(view);
            } catch (ParseException e) {
                Log.e(ERROR_TAG, e.getMessage());
            }
        });

        selectBeginningDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectBeginningDate.callOnClick();
            }
        });

        selectEndingDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectBeginningDate.callOnClick();
            }
        });

        selectReservationDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                selectReservationDate.callOnClick();
            }
        });

        OnTextChangedListener participantsListener = (s, start, before, count) -> checkMinMaxParticipants(minParticipants);
        maxParticipants.addTextChangedListener(participantsListener);
        minParticipants.addTextChangedListener(participantsListener);

        durationMinutes.addTextChangedListener((OnTextChangedListener) (s, start, before, count) -> {
            String minutes = durationMinutes.getText().toString();
            if (!minutes.equals("") && Integer.parseInt(minutes) > 60) {
                durationMinutes.setError(ItineraryModifier.this.getString(R.string.wrong_number));
            }
        });

        durationHours.addTextChangedListener((OnTextChangedListener) (s, start, before, count) -> {
            String duration = durationHours.getText().toString();
            if (!duration.equals("")) {
                if (Integer.parseInt(duration) > 23) {
                    repetitions.setText("1");
                    repetitions.setClickable(false);
                    repetitions.setFocusableInTouchMode(false);
                    repetitions.setFocusable(false);
                } else {
                    repetitions.setClickable(true);
                    repetitions.setFocusableInTouchMode(true);
                    repetitions.setFocusable(true);
                }
            }
        });

        repetitions.addTextChangedListener((OnTextChangedListener) (s, start, before, count) -> {
            String repInserted = repetitions.getText().toString();
            if (!repInserted.equals("") && Integer.parseInt(repInserted) < 1) {
                repetitions.setError(ItineraryModifier.this.getString(R.string.wrong_number));
            }
        });

        reducedPrice.setOnClickListener(v -> {
            if (fullPrice.getText().toString().equals("")) {
                reducedPrice.setClickable(false);
                reducedPrice.setActivated(false);
                reducedPrice.setFocusableInTouchMode(false);
                reducedPrice.setFocusable(false);
            } else {
                reducedPrice.setClickable(true);
                reducedPrice.setActivated(true);
                reducedPrice.setFocusableInTouchMode(true);
                reducedPrice.setFocusable(true);
            }
        });

        fullPrice.addTextChangedListener((OnTextChangedListener) (s, start, before, count) -> {
            String fPrice = fullPrice.getText().toString();
            String rPrice = reducedPrice.getText().toString();
            if (!fPrice.equals("")) {
                reducedPrice.setFocusable(true);
                reducedPrice.setClickable(true);
                reducedPrice.setFocusableInTouchMode(true);
                if (!rPrice.equals("")) {
                    if (Float.parseFloat(rPrice) > Float.parseFloat(fPrice)) {
                        fullPrice.setError(ItineraryModifier.this.getString(R.string.wrong_number));
                    } else {
                        reducedPrice.setError(null);
                        fullPrice.setError(null);
                    }
                }
            }
        });

        reducedPrice.addTextChangedListener((OnTextChangedListener) (s, start, before, count) -> {
            String fPrice = fullPrice.getText().toString();
            String rPrice = reducedPrice.getText().toString();
            if (!rPrice.equals("")) {
                if (Float.parseFloat(rPrice) > Float.parseFloat(fPrice)) {
                    reducedPrice.setError(ItineraryModifier.this.getString(R.string.wrong_number));
                } else {
                    reducedPrice.setError(null);
                    fullPrice.setError(null);
                }
            }
        });

        image.setOnClickListener(v -> imageManager.selectImage());

    }

    void updateBeginningDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        selectBeginningDate.setText(sdf.format(myCalendar.getTime()));
        selectEndingDate.setClickable(true);
        selectEndingDate.setActivated(true);
    }

    void updateEndingDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        selectEndingDate.setText(sdf.format(myCalendar.getTime()));
    }

    protected void updateReservationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        selectReservationDate.setText(sdf.format(myCalendar.getTime()));
    }

    protected void checkMinMaxParticipants(EditText currentEditText) {
        String maxInserted = maxParticipants.getText().toString();
        String minInserted = minParticipants.getText().toString();
        if (!maxInserted.equals("") && !minInserted.equals("")) {
            int max = Integer.parseInt(maxInserted);
            int min = Integer.parseInt(minInserted);
            if (min > max) {
                currentEditText.setError(getString(R.string.wrong_number));
            }
            else
                currentEditText.setError(null);

        }
    }

    public void setBeginningDate(View view) {
        DatePickerDialog.OnDateSetListener bDate = (view2, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBeginningDate();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, bDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
        selectBeginningDate.clearFocus();
        selectEndingDate.setText(null);
    }

    public void setEndingDate(View view) {
        DatePickerDialog.OnDateSetListener eDate = (view2, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndingDate();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, eDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        Date minDate = null;
        try {
            minDate = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(selectBeginningDate.getText().toString());
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
        }
        if (minDate != null) {
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
            datePickerDialog.show();
        } else {
            Toast.makeText(this, getString(R.string.error_insert_beginning_date), Toast.LENGTH_SHORT).show();
        }

        selectEndingDate.clearFocus();
    }

    public void setReservationDate(View view) {
        DatePickerDialog.OnDateSetListener rDate = (view2, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateReservationDate();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, rDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        Date minDate = null;
        Date maxDate = null;
        try {
            minDate = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(selectBeginningDate.getText().toString());
            maxDate = new SimpleDateFormat(DATE_FORMAT, Locale.US).parse(selectEndingDate.getText().toString());
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
        }
        if (minDate != null && maxDate != null) {
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
            datePickerDialog.show();
        } else {
            if (minDate == null) {
                Toast.makeText(this, getString(R.string.error_insert_beginning_date), Toast.LENGTH_SHORT).show();
            }
            if (maxDate == null) {
                Toast.makeText(this, getString(R.string.error_insert_ending_date), Toast.LENGTH_SHORT).show();
            }
        }

    }

    protected boolean allFilled() {
        return !title.getText().toString().equals("")
                && !description.getText().toString().equals("")
                && !selectBeginningDate.getText().toString().equals("")
                && !selectEndingDate.getText().toString().equals("")
                && !selectReservationDate.getText().toString().equals("")
                && !minParticipants.getText().toString().equals("")
                && !maxParticipants.getText().toString().equals("")
                && !repetitions.getText().toString().equals("")
                && !durationHours.getText().toString().equals("")
                && !durationMinutes.getText().toString().equals("")
                && !location.getText().toString().equals("")
                && !fullPrice.getText().toString().equals("")
                && !reducedPrice.getText().toString().equals("");
    }

    protected boolean noFieldWithError() {
        return minParticipants.getError() == null
                && maxParticipants.getError() == null
                && durationMinutes.getError() == null
                && fullPrice.getError() == null
                && reducedPrice.getError() == null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageManager.manageResult(requestCode, resultCode, data, image);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public abstract void sendData(View view) throws ParseException;

    private interface OnTextChangedListener extends TextWatcher {
        @Override
        default void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        default void afterTextChanged(Editable s) {
            // Do nothing
        }
    }
}

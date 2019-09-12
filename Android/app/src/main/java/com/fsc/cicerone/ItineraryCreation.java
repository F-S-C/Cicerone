package com.fsc.cicerone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;


public class ItineraryCreation extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText selectBeginningDate;
    EditText selectEndingDate;
    EditText selectReservationDate;
    EditText minParticipants;
    EditText maxParticipants;
    EditText repetitions;
    EditText durationHours;
    EditText durationMinutes;
    EditText location;
    EditText fullPrice;
    EditText reducedPrice;
    Button submit;
    final Calendar myCalendar = Calendar.getInstance();

    private static final String ERROR_TAG = "ERROR IN " + ItineraryCreation.class.getName();
    private static final String DATE_FORMAT = "dd-MM-yy";
    private static String URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_creation);

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

        maxParticipants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkMinMaxParticipants(maxParticipants);
            }


            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        minParticipants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkMinMaxParticipants(minParticipants);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });


        durationMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String minutes = durationMinutes.getText().toString();
                if (!minutes.equals("") && Integer.parseInt(minutes) > 60) {
                    durationMinutes.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        durationHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String duration = durationHours.getText().toString();
                if (!duration.equals("")) {
                    if (Integer.parseInt(duration) > 23) {
                        repetitions.setText("1");
                        repetitions.setClickable(false);
                        repetitions.setFocusableInTouchMode(false);
                        repetitions.setFocusable(false);
                    } else {
                        if (repetitions.getText().toString().equals("1")) {
                            repetitions.setText("");
                        }

                        repetitions.setClickable(true);
                        repetitions.setFocusableInTouchMode(true);
                        repetitions.setFocusable(true);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        repetitions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String repInserted = repetitions.getText().toString();
                if (!repInserted.equals("") && Integer.parseInt(repInserted) < 1) {
                    repetitions.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
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

        fullPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String fPrice = fullPrice.getText().toString();
                String rPrice = reducedPrice.getText().toString();
                if (!fPrice.equals("")) {
                    reducedPrice.setFocusable(true);
                    reducedPrice.setClickable(true);
                    reducedPrice.setFocusableInTouchMode(true);
                    if (!rPrice.equals("")) {
                        if (Float.parseFloat(rPrice) > Float.parseFloat(fPrice)) {
                            fullPrice.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                        } else {
                            reducedPrice.setError(null);
                            fullPrice.setError(null);
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        reducedPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String fPrice = fullPrice.getText().toString();
                String rPrice = reducedPrice.getText().toString();
                if (!rPrice.equals("")) {
                    if (Float.parseFloat(rPrice) > Float.parseFloat(fPrice)) {
                        reducedPrice.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                    } else {
                        reducedPrice.setError(null);
                        fullPrice.setError(null);

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

    }

    public void sendData(View view) {
        boolean canSend = allFilled();
        if (canSend) {

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
                    URL
            );


        } else {
            Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
        }
    }

    public void setBeginningDate(View view) {
        DatePickerDialog.OnDateSetListener bDate = (view2, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBeginningDate();
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(ItineraryCreation.this, bDate, myCalendar
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ItineraryCreation.this, eDate, myCalendar
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
            Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_insert_beginning_date), Toast.LENGTH_SHORT).show();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ItineraryCreation.this, rDate, myCalendar
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
                Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_insert_beginning_date), Toast.LENGTH_SHORT).show();
            }
            if (maxDate == null) {
                Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_insert_ending_date), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void submit(JSONObject params) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_ITINERARY,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if (result.getResult()) {
                            Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.itinerary_added), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent().setClass(ItineraryCreation.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }

                    }
                },
                params);
        connector.execute();
    }

    private boolean allFilled() {
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

    private void updateBeginningDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        selectBeginningDate.setText(sdf.format(myCalendar.getTime()));
        selectEndingDate.setClickable(true);
        selectEndingDate.setActivated(true);
    }

    private void updateEndingDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        selectEndingDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateReservationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        selectReservationDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void checkMinMaxParticipants(EditText currentEditText) {
        String maxInserted = maxParticipants.getText().toString();
        String minInserted = minParticipants.getText().toString();
        if (!maxInserted.equals("") && !minInserted.equals("")) {
            int max = Integer.parseInt(maxInserted);
            int min = Integer.parseInt(minInserted);
            if (min > max) {
                currentEditText.setError(ItineraryCreation.this.getString(R.string.wrong_number));
            }
        }
    }

}



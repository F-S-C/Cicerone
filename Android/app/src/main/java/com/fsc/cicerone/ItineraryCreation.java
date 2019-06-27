package com.fsc.cicerone;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

import static android.app.DatePickerDialog.OnDateSetListener;


public class ItineraryCreation extends AppCompatActivity {
    EditText title;
    EditText description;
    EditText selectBeginningDate;
    EditText selectEndingDate;
    EditText selectReservationDate;
    EditText minPartecipants;
    EditText maxPartecipants;
    EditText repetitions;
    EditText durationHours;
    EditText durationMinutes;
    EditText location;
    EditText fullPrice;
    EditText reducedPrice;
    Button submit;
    final Calendar myCalendar = Calendar.getInstance();

    @SuppressLint("SimpleDateFormat")
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
        minPartecipants = findViewById(R.id.inputMinimumParticipants);
        maxPartecipants = findViewById(R.id.inputMaximumPartecipants);
        durationHours = findViewById(R.id.inputDurationHours);
        durationMinutes = findViewById(R.id.inputDurationMinutes);
        repetitions = findViewById(R.id.inputRepetitions);
        reducedPrice = findViewById(R.id.inputReducedPrice);
        fullPrice = findViewById(R.id.inputFullPrice);
        submit = findViewById(R.id.submit);



        OnDateSetListener bDate = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBeginningDate();
        };

        selectBeginningDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            DatePickerDialog datePickerDialog = new DatePickerDialog(ItineraryCreation.this, bDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
            selectBeginningDate.clearFocus();
            selectEndingDate.setText(null);
        });
        OnDateSetListener eDate = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndingDate();
        };

        selectEndingDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            DatePickerDialog datePickerDialog = new DatePickerDialog(ItineraryCreation.this, eDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            Date minDate = null;
            try {
                minDate = new SimpleDateFormat("dd-MM-yy").parse(selectBeginningDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (minDate != null) {
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.show();
            } else {
                Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_insert_beginning_date), Toast.LENGTH_SHORT).show();
            }

            selectEndingDate.clearFocus();
        });


        OnDateSetListener rDate = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateReservationDate();
        };
        selectReservationDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(ItineraryCreation.this, rDate, myCalendar
            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            Date minDate = null;
            Date maxDate = null;
            try {
                minDate = new SimpleDateFormat("dd-MM-yy").parse(selectBeginningDate.getText().toString());
                maxDate = new SimpleDateFormat("dd-MM-yy").parse(selectEndingDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (minDate != null && maxDate != null) {
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime());
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTime());
                datePickerDialog.show();
            } else {
                if(minDate == null) {
                    Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_insert_beginning_date), Toast.LENGTH_SHORT).show();
                }
                if(maxDate == null) {
                    Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_insert_ending_date), Toast.LENGTH_SHORT).show();
                }
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

        maxPartecipants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int min;
                String maxInserted = maxPartecipants.getText().toString();
                String minInserted = minPartecipants.getText().toString();
                if(!maxInserted.equals(""))
                {
                    if(!minInserted.equals(""))
                    {
                        int max = Integer.parseInt(maxInserted);
                        min = Integer.parseInt(minInserted);
                        if( min > max) {
                            maxPartecipants.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                        }
                    }
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        minPartecipants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int min;
                String maxInserted = maxPartecipants.getText().toString();
                String minInserted = minPartecipants.getText().toString();
                if(!maxInserted.equals(""))
                {
                    if(!minInserted.equals(""))
                    {
                        int max = Integer.parseInt(maxInserted);
                        min = Integer.parseInt(minInserted);
                        if( min > max) {
                            minPartecipants.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        durationMinutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String minutes = durationMinutes.getText().toString();
                if(!minutes.equals(""))
                {
                    if(Integer.parseInt(minutes)>60) {
                        durationMinutes.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        durationHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String durationh = durationHours.getText().toString();
                if(!durationh.equals(""))
                {
                    if(Integer.parseInt(durationh) > 23)
                    {
                        repetitions.setText("1");
                        repetitions.setClickable(false);
                        repetitions.setFocusableInTouchMode(false);
                        repetitions.setFocusable(false);
                    }
                    else
                    {
                        if(repetitions.getText().toString().equals("1")) {
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

            }
        });

        repetitions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String repInserted = repetitions.getText().toString();
                if(!repInserted.equals(""))
                {
                    int rep = Integer.parseInt(repInserted);
                    if(rep < 1 ) {
                        repetitions.setError(ItineraryCreation.this.getString(R.string.wrong_number));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        reducedPrice.setOnClickListener(v -> {
            if(fullPrice.getText().toString().equals(""))
            {
                reducedPrice.setClickable(false);
                reducedPrice.setActivated(false);
                reducedPrice.setFocusableInTouchMode(false);
                reducedPrice.setFocusable(false);
            }
            else
            {
                reducedPrice.setClickable(true);
                reducedPrice.setActivated(true);
                reducedPrice.setFocusableInTouchMode(true);
                reducedPrice.setFocusable(true);
            }
        });

            fullPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String fPrice = fullPrice.getText().toString();
                    String rPrice = reducedPrice.getText().toString();
                    if(!fPrice.equals("")) {
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

                }
            });

            reducedPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String fPrice = fullPrice.getText().toString();
                    String rPrice = reducedPrice.getText().toString();
                    if(!rPrice.equals(""))
                    {
                        if(Float.parseFloat(rPrice) > Float.parseFloat(fPrice))
                        {
                            reducedPrice.setError(ItineraryCreation.this.getString(R.string.wrong_number));

                        }
                        else
                        {
                            reducedPrice.setError(null);
                            fullPrice.setError(null);

                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            submit.setOnClickListener(v -> {
                JSONObject params = new JSONObject();
                boolean canSend = allFilled();
                if(canSend)
                {
                    try {
                        params.put("title", title.getText().toString());
                        params.put("description", description.getText().toString());
                        params.put("beginning_date", selectBeginningDate.getText().toString());
                        params.put("ending_date" , selectEndingDate.getText().toString());
                        params.put("end_reservations_date", selectReservationDate.getText().toString());
                        params.put("maximum_partecipants_number", maxPartecipants.getText().toString());
                        params.put("minimum_partecipants_number", minPartecipants.getText().toString());
                        params.put("repetitions_per_day", repetitions.getText().toString());
                        params.put("location", location.getText().toString());
                        params.put("duration", durationHours.getText().toString() + ":" + durationMinutes.getText().toString() + ":00");
                        params.put("full_price", fullPrice.getText().toString());
                        params.put("reduced_price", reducedPrice.getText().toString());
                        SharedPreferences random = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
                        JSONObject obj = new JSONObject(random.getString("session",""));
                        obj.remove("password");
                        params.put("username", obj.getString("username"));

                        Submit(params);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
                }

            });

    }


    private void Submit(JSONObject params) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {

            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                // TODO Change page
                Log.e("p", object.toString());
                if(object.getBoolean("result"))
                {
                    Toast.makeText(ItineraryCreation.this, ItineraryCreation.this.getString(R.string.itinerary_added), Toast.LENGTH_SHORT).show();

                }

            }
        });
            connector.setObjectToSend(params);
            connector.execute();
    }

        private boolean allFilled()
        {
            return !title.getText().toString().equals("")
                    && !description.getText().toString().equals("")
                    && !selectBeginningDate.getText().toString().equals("")
                    && !selectEndingDate.getText().toString().equals("")
                    && !selectReservationDate.getText().toString().equals("")
                    && !minPartecipants.getText().toString().equals("")
                    && !maxPartecipants.getText().toString().equals("")
                    && !repetitions.getText().toString().equals("")
                    && !durationHours.getText().toString().equals("")
                    && !durationMinutes.getText().toString().equals("")
                    && !location.getText().toString().equals("")
                    && !fullPrice.getText().toString().equals("")
                    && !reducedPrice.getText().toString().equals("");
        }
    private void updateBeginningDate() {
        String myFormat = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectBeginningDate.setText(sdf.format(myCalendar.getTime()));
        selectEndingDate.setClickable(true);
        selectEndingDate.setActivated(true);
    }

    private void updateEndingDate() {
        String myFormat = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectEndingDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateReservationDate() {
        String myFormat = "dd-MM-yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectReservationDate.setText(sdf.format(myCalendar.getTime()));
    }

}



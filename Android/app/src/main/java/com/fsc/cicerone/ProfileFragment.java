package com.fsc.cicerone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class ProfileFragment extends Fragment {

    private EditText name, surname, email, cellphone, birthdate;

    /**
     * Empty constructor
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        SharedPreferences preferences = this.getActivity().getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        Spinner sexList = view.findViewById(R.id.sexList);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        email = view.findViewById(R.id.email);
        cellphone = view.findViewById(R.id.cellphone);
        birthdate = view.findViewById(R.id.birthdate);
        addItemsSex(sexList);
        try {
            final JSONObject parameters = new JSONObject(preferences.getString("session", "")); //Connection params
            parameters.remove("password");
            requestUserData(view, parameters, sexList);
        } catch (JSONException e) {
            Log.e("EXCEPTION", e.toString());
        }
        return view;
    }

    /**
     * Create a list of string used for the Spinner present in the page.
     *
     * @param spinner The spinner where to insert the list.
     */
    public void addItemsSex(Spinner spinner) {

        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.male));
        list.add(getResources().getString(R.string.female));
        list.add(getResources().getString(R.string.other));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void requestUserData(View view, JSONObject parameters, Spinner spinner) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                progressBar.setVisibility(View.GONE);
                JSONObject userData = jsonArray.getJSONObject(0);
                if (userData.getString("sex").equals("male")) {
                    spinner.setSelection(0);
                } else if (userData.getString("sex").equals("female")) {
                    spinner.setSelection(1);
                } else
                    spinner.setSelection(2);
                name.setText(userData.getString("name"));
                surname.setText(userData.getString("surname"));
                email.setText(userData.getString("email"));
                cellphone.setText(userData.getString("cellphone"));
                try {
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = inputFormat.parse(userData.getString("birth_date"));
                    //TODO Change date with calendar
                    birthdate.setText(outputFormat.format(date));
                } catch (ParseException e) {
                    Log.e("EXCEPTION", e.toString());
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

}

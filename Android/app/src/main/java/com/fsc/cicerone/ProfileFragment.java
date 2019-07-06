package com.fsc.cicerone;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class ProfileFragment extends Fragment {

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText cellphone;
    private EditText birthDate;
    private Button switchButton;
    private Dialog logoutDialog;
    private Dialog switchToCiceroneDialog;

    /**
     * Empty constructor
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        logoutDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(logoutDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        logoutDialog.setContentView(R.layout.activity_logout);
        logoutDialog.setCancelable(true);

        switchToCiceroneDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(switchToCiceroneDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        switchToCiceroneDialog.setContentView(R.layout.switch_to_cicerone);
        switchToCiceroneDialog.setCancelable(true);

        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        Spinner sexList = view.findViewById(R.id.sexList);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        email = view.findViewById(R.id.email);
        cellphone = view.findViewById(R.id.cellphone);
        birthDate = view.findViewById(R.id.birthdate);
        switchButton = view.findViewById(R.id.switch_to_cicerone);
        Button logoutButton = view.findViewById(R.id.logout);

        addItemsSex(sexList);
        try {
            final JSONObject parameters = new JSONObject(preferences.getString("session", "")); //Connection params
            parameters.remove("password");
            requestUserData(view, parameters, sexList);
            final JSONObject updateParams = new JSONObject();
            updateParams.put("username", parameters.getString("username"));
            updateParams.put("user_type","1");

            switchButton.setOnClickListener(view1 -> {
                Button noButton = switchToCiceroneDialog.findViewById(R.id.switch_cicerone_no_button);
                noButton.setOnClickListener(v -> switchToCiceroneDialog.hide());

                Button yesButton = switchToCiceroneDialog.findViewById(R.id.switch_cicerone_yes_button);
                yesButton.setOnClickListener(v -> {
                    switchToCiceroneDialog.hide();
                    switchToCicerone(updateParams);
                });

                switchToCiceroneDialog.show();
            });
        } catch (JSONException e) {
            Log.e("EXCEPTION", e.toString());
        }

        logoutButton.setOnClickListener(view1 -> {
            Button noButton = logoutDialog.findViewById(R.id.no_logout_button);
            noButton.setOnClickListener(v -> logoutDialog.hide());

            Button yesButton = logoutDialog.findViewById(R.id.yes_logout_button);
            yesButton.setOnClickListener(v -> {
                logoutDialog.hide();
                logoutDialog.dismiss();
                preferences.edit().clear().apply();
                Intent i = new Intent(getActivity(), SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            });

            logoutDialog.show();
        });

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
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
                if(userData.getInt("user_type") == 1) {
                    switchButton.setVisibility(View.GONE);
                } else {
                    switchButton.setVisibility(View.VISIBLE);
                }
                try {
                    @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = inputFormat.parse(userData.getString("birth_date"));
                    //TODO Change date with calendar
                    birthDate.setText(outputFormat.format(date));
                } catch (ParseException e) {
                    Log.e("EXCEPTION", e.toString());
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

    private void switchToCicerone(JSONObject parameters) {

        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.UPDATE_REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
             //Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject userData = jsonArray.getJSONObject(0);

                if (userData.getBoolean("result")) {
                    switchToCiceroneDialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.operation_completed) , Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getActivity(), SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else{
                    Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                }

            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }
}
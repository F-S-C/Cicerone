package com.fsc.cicerone;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * ProfileActivity is the class that allows you to build a user's profile activity.
 **/
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final JSONObject params;
        try {
            // TODO: per mancanza di item funzionali realizzati, la seguente funzione è stata
            //       basata sull'utente recentemente loggato. Per tanto sarà necessario modificarlo
            //       appena verranno realizzati gli item di interesse.
            SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
            params = new JSONObject(preferences.getString("session", ""));
            params.remove("password");
            TextView username = findViewById(R.id.username_profile);
            username.setText(params.getString("username"));
            getData(params);
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }

    }

    private void getData(JSONObject parameters) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                TextView name = findViewById(R.id.name_profile);
                name.setText(result.getString("name"));
                TextView surname = findViewById(R.id.surname_profile);
                surname.setText(result.getString("surname"));
                TextView email = findViewById(R.id.email_profile);
                email.setText(result.getString("email"));
                TextView userType = findViewById(R.id.user_type_profile);
                if (Objects.requireNonNull(UserType.getValue(result.getInt("user_type"))) == UserType.CICERONE) {
                    userType.setText(R.string.user_type_cicerone);
                } else {
                    userType.setText(R.string.user_type_globetrotter);
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();

        SendInPostConnector connectorReview = new SendInPostConnector(ConnectorConstants.USER_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                int i;
                int sum = 0;
                JSONObject result;
                for (i = 0; i < jsonArray.length(); i++) {
                    result = jsonArray.getJSONObject(i);
                    sum += result.getInt("feedback");
                }
                RatingBar star = findViewById(R.id.avg_feedback);
                star.setRating((i > 0) ? ((float) sum / i) : 0);
            }
        });
        try {
            JSONObject newParameter = new JSONObject();
            newParameter.put("reviewed_user", parameters.getString("username"));
            connectorReview.setObjectToSend(newParameter);
            connectorReview.execute();
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }
}
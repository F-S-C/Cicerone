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

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**Profile is the class that allows you to build a user's profile activity.**/
public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final JSONObject params;
        try {
            //TODO per mancanza di item funzionali realizzati, la seguente funzione è stata basata sull'utente recentemente loggato. Per tanto sarà necessario modificarlo appena verranno realizzati gli item di interesse.
            SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
            params = new JSONObject(preferences.getString("session", ""));
            params.remove("password");
            TextView username = findViewById(R.id.username_profile);
            username.setText(params.getString("username"));
            getData(params);
        } catch (JSONException e) {
            Log.e("error",e.toString());
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
                try {
                    JSONObject result = jsonArray.getJSONObject(0);
                    TextView name = findViewById(R.id.name_profile);
                    name.setText(result.getString("name"));
                    TextView surname = findViewById(R.id.surname_profile);
                    surname.setText(result.getString("surname"));
                    TextView email = findViewById(R.id.email_profile);
                    email.setText(result.getString("email"));
                    TextView user_type = findViewById(R.id.user_type_profile);
                    switch (UserType.getValue(result.getInt("user_type"))) {
                        case CICERONE:
                            user_type.setText(R.string.user_type_cicerone);
                            break;
                        case GLOBETROTTER:
                            user_type.setText(R.string.user_type_globetrotter);
                            break;
                    }
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();

        SendInPostConnector connector_review = new SendInPostConnector(ConnectorConstants.USER_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                try {
                    int i, sum = 0;
                    JSONObject result;
                    for (i = 0; i < jsonArray.length(); i++) {
                        result = jsonArray.getJSONObject(i);
                        sum += result.getInt("feedback");
                    }
                    RatingBar star = findViewById(R.id.avg_feedback);
                    star.setRating((i > 0)? ((float) sum / i) : 0);
                } catch (JSONException e) {
                    Log.e("error", e.toString());
                }
            }
        });
        try {
            JSONObject new_parameter = new JSONObject();
            new_parameter.put("reviewed_user", parameters.getString("username"));
            connector_review.setObjectToSend(new_parameter);
            connector_review.execute();
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }
}
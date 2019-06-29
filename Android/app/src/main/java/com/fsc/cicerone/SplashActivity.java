package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private static final String ERROR_TAG = "ERROR IN " + SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AccountManager accountManager = new AccountManager();
        SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        try {
            JSONObject currentLoggedUser = new JSONObject(preferences.getString("session", ""));
            if (!currentLoggedUser.getString("username").equals("") && !currentLoggedUser.getString("password").equals("")) {
                accountManager.attemptLogin(currentLoggedUser, () -> {
                    // Do nothing
                }, jsonArray -> {
                    JSONObject result = jsonArray.getJSONObject(0);
                    boolean done = false;
                    try {
                        Log.e("CIAO", String.valueOf(result.getString("result")));
                        done = result.getBoolean("result");
                    } catch (JSONException e) {
                        Log.e(ERROR_TAG, e.toString());
                    }

                    Intent intent = new Intent(SplashActivity.this, (done) ? ProfileActivity.class : LoginActivity.class);

                    // TODO: Change activity
                    // startActivity(new Intent(LoginActivity.this, AccountDetails.class));
                    startActivity(intent);
                    finish();
                });
            }
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }


    }
}

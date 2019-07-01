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
        String latestLoggedUserCredentials = preferences.getString("session", "");

        if (latestLoggedUserCredentials != null && !latestLoggedUserCredentials.equals("")) {
            try {
                JSONObject currentLoggedUser = new JSONObject(latestLoggedUserCredentials);
                if (!currentLoggedUser.getString("username").equals("") && !currentLoggedUser.getString("password").equals("")) {
                    accountManager.attemptLogin(currentLoggedUser, () -> {
                        // Do nothing
                    }, jsonArray -> {
                        JSONObject result = jsonArray.getJSONObject(0);
                        boolean done = result.getBoolean("result");

                        Intent intent = new Intent(SplashActivity.this, (done) ? AccountDetails.class : LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.toString());
            }
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }


    }
}

package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Class activityToOpenIfLogged = MainActivity.class;
        final Class activityToOpenIfNotLogged = LoginActivity.class;
        final Class activityToOpenIfLoggedAdmin = AdminMainActivity.class;

        SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        String latestLoggedUserCredentials = preferences.getString("session", "");

        if (latestLoggedUserCredentials != null && !latestLoggedUserCredentials.equals("")) {
            try {
                JSONObject currentLoggedUser = new JSONObject(latestLoggedUserCredentials);
                if (!currentLoggedUser.getString("username").equals("") && !currentLoggedUser.getString("password").equals("")) {
                    AccountManager.attemptLogin(this, currentLoggedUser.getString("username"), currentLoggedUser.getString("password"), () -> {
                        // Do nothing
                    }, (result, success) -> {
                        Intent intent = new Intent(SplashActivity.this,
                                (success) ?
                                        (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) ?
                                                activityToOpenIfLoggedAdmin :
                                                activityToOpenIfLogged :
                                        activityToOpenIfNotLogged);
                        startActivity(intent);
                        finish();
                    });
                }
            } catch (JSONException e) {
                startActivity(new Intent(SplashActivity.this, activityToOpenIfNotLogged));
                finish();
            }
        } else {
            startActivity(new Intent(SplashActivity.this, activityToOpenIfNotLogged));
            finish();
        }


    }
}

package com.fsc.cicerone;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private static final String ERROR_TAG = "ERROR IN " + SplashActivity.class.getName();

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
                    AccountManager.attemptLogin(currentLoggedUser, () -> {
                        // Do nothing
                    }, (result, success) -> {
                        if(AccountManager.getCurrentLoggedUser().getUserType()==UserType.ADMIN){
                            Intent intent = new Intent(SplashActivity.this, (success) ? activityToOpenIfLoggedAdmin : activityToOpenIfNotLogged);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(SplashActivity.this, (success) ? activityToOpenIfLogged : activityToOpenIfNotLogged);
                            startActivity(intent);
                        }
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

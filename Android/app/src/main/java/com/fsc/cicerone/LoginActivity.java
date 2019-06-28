package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class LoginActivity extends AppCompatActivity {

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();

    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        try {
            JSONObject currentLoggedUser = new JSONObject(preferences.getString("session", ""));
            if (!currentLoggedUser.getString("username").equals("") && !currentLoggedUser.getString("password").equals("")) {
                attemptLogin(currentLoggedUser, true);
            }
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }

        usernameEditText = findViewById(R.id.usernameInput);
        passwordEditText = findViewById(R.id.passwordInput);
    }

    public void login(View view) {
        if (usernameEditText.getText().toString().trim().length() == 0) {
            usernameEditText.setError(getString(R.string.empty_username_error));
            return;
        }
        if (passwordEditText.getText().toString().trim().length() == 0) {
            passwordEditText.setError(getString(R.string.empty_password_error));
            return;
        }

        final String username = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final JSONObject user = new JSONObject();
        try {
            user.put("username", username);
            user.put("password", password);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }

        attemptLogin(user, false);

    }

    public void forgotPassword(View view) {
        Toast.makeText(this, "Sorry, Work In Progress", Toast.LENGTH_SHORT).show();
    }

    public void skipLogin(View view) {
        Toast.makeText(this, "Sorry, Work In Progress", Toast.LENGTH_SHORT).show();
    }

    public void goToSignUpPage(View view) {
        Toast.makeText(this, "Sorry, Work In Progress", Toast.LENGTH_SHORT).show();
    }

    private void attemptLogin(JSONObject user, Boolean isAutomatic) {
        RelativeLayout progressBar = findViewById(R.id.loginProgressBarContainer);

        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.LOGIN_CONNECTOR, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                JSONObject result = jsonArray.getJSONObject(0);
                boolean done = false;
                try {
                    Log.e("CIAO", String.valueOf(result.getString("result")));
                    done = result.getBoolean("result");
                } catch (JSONException e) {
                    Log.e(ERROR_TAG, e.toString());
                }
                if (!done && !isAutomatic) {
                    Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                    usernameEditText.setError(getString(R.string.wrong_credentials));
                    passwordEditText.setError(getString(R.string.wrong_credentials));
                    return;
                }

                SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
                preferences.edit().putString("session", user.toString()).apply();

                // TODO: Change activity
                startActivity(new Intent(LoginActivity.this, AccountDetails.class));
                //startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            }
        });

        connector.setObjectToSend(user);

        connector.execute();
    }
}

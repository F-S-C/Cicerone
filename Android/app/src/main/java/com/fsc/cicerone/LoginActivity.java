package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();

    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameInput);
        passwordEditText = findViewById(R.id.passwordInput);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Button button = findViewById(R.id.loginButton);
                button.performClick();
                return true;
            }
            return false;
        });
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

        attemptLogin(user);

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

    private void attemptLogin(JSONObject user) {
        RelativeLayout progressBar = findViewById(R.id.loginProgressBarContainer);

        AccountManager.attemptLogin(user, () -> {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }, (result, success) -> {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (!success) {
                Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                usernameEditText.setError(getString(R.string.wrong_credentials));
                passwordEditText.setError(getString(R.string.wrong_credentials));
                return;
            }

            CheckBox rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
            if (rememberMeCheckBox.isChecked()) {
                SharedPreferences preferences = getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
                preferences.edit().putString("session", user.toString()).apply();
            }

            // startActivity(new Intent(LoginActivity.this, AccountDetails.class));
            startActivity(new Intent(LoginActivity.this, ItineraryDetails.class));
            finish();
        });
    }
}

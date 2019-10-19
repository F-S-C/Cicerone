/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fsc.cicerone.view.user.registered_user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.Config;
import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.user.RegistrationActivity;
import com.fsc.cicerone.view.user.UserMainActivity;
import com.fsc.cicerone.view.admin.AdminMainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameEditTextContainer;
    private TextInputEditText usernameEditText;
    private TextInputLayout passwordEditTextContainer;
    private TextInputEditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);

        usernameEditTextContainer = findViewById(R.id.usernameInputContainer);
        passwordEditTextContainer = findViewById(R.id.passwordInputContainer);
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
        final String username = usernameEditText.getText() != null ? usernameEditText.getText().toString().trim() : "";
        final String password = passwordEditText.getText() != null ? passwordEditText.getText().toString() : "";

        if (username.isEmpty()) {
            usernameEditTextContainer.setError(getString(R.string.empty_username_error));
            return;
        }
        if (password.isEmpty()) {
            passwordEditTextContainer.setError(getString(R.string.empty_password_error));
            return;
        }

        attemptLogin(username, password);
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
        finish();
    }

    public void skipLogin(View view) {
        startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
        finish();
    }

    public void goToSignUpPage(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        finish();
    }

    private void attemptLogin(String username, String password) {
        RelativeLayout progressBar = findViewById(R.id.loginProgressBarContainer);

        User.Credentials credentials = new User.Credentials(username, password);

        AccountManager.attemptLogin(this, credentials, () -> {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }, success -> {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (!success) {
                Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_SHORT).show();
                usernameEditTextContainer.setError(getString(R.string.wrong_credentials));
                passwordEditTextContainer.setError(getString(R.string.wrong_credentials));
                return;
            }

            CheckBox rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);
            if (rememberMeCheckBox.isChecked()) {
                SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_KEY, Context.MODE_PRIVATE);
                preferences.edit().putString("session", credentials.toString()).apply();
            }

            Class targetActivity = (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) ? AdminMainActivity.class : UserMainActivity.class;
            startActivity(new Intent(LoginActivity.this, targetActivity));
            finish();
        });
    }
}

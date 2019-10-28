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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.R;
import com.fsc.cicerone.mailer.Mailer;
import com.fsc.cicerone.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A class that represents the user interface that manages the creation of a new password for the
 * user (if the original password was forgotten).
 */
public class ForgotPassword extends AppCompatActivity {

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        TextInputEditText emailText = findViewById(R.id.emailTextForgotPassword);
        TextInputLayout emailContainer = findViewById(R.id.emailForgotPassword);
        Button send = findViewById(R.id.resetForgotPassword);

        send.setOnClickListener(view -> {
            String email = emailText.getText() != null ? emailText.getText().toString().trim() : "";
            if (User.validateEmail(email)) {
                emailContainer.setError(null);
                Log.e("PRE EMAIL", email);
                Mailer.sendPasswordResetLink(this, email, v -> Toast.makeText(this, getString(R.string.email_sent), Toast.LENGTH_SHORT).show());
                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                finish();
            } else {
                emailContainer.setError(getString(R.string.email_not_valid));
            }
        });
    }
}

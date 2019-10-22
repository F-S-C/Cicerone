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

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that manage the situation of changing the password of an User.
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText verifyNewPassword;
    private Button changeP;

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPassword = findViewById(R.id.old_pw_textbox);
        newPassword = findViewById(R.id.new_pw_textbox);
        verifyNewPassword = findViewById(R.id.ver_pw_textbox);
        changeP = findViewById(R.id.change_btn);
        changeP.setEnabled(false);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextWatcher onTextChangedListener = new TextWatcher() {
            /**
             * @param s
             */
            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }

            /**
             * @param s
             * @param start
             * @param count
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            /**
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyFields();
            }

        };

        oldPassword.addTextChangedListener(onTextChangedListener);
        newPassword.setOnFocusChangeListener((view, hasFocus) -> verifyFields());

        newPassword.addTextChangedListener(onTextChangedListener);
        newPassword.setOnFocusChangeListener((view, hasFocus) -> verifyFields());

        verifyNewPassword.addTextChangedListener(onTextChangedListener);
        verifyNewPassword.setOnFocusChangeListener((view, hasFocus) -> verifyFields());

        changeP.setOnClickListener(view -> {
            if (oldPassword.getText().toString().equals(newPassword.getText().toString())) {
                Toast.makeText(getApplicationContext(), getString(R.string.oldpw_equals_new_err), Toast.LENGTH_LONG).show();
                changeP.setEnabled(false);
            } else {
                if (oldPassword.getText().toString().equals(AccountManager.getCurrentLoggedUser().getPassword())) {
                    changePasswordOnServer();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.oldpw_mism_error), Toast.LENGTH_SHORT).show();
                    changeP.setEnabled(false);
                }
            }
        });
    }

    /**
     * A function that manages the change the password of an User in the Server.
     */
    private void changePasswordOnServer() {
        User user = AccountManager.getCurrentLoggedUser();
        Map<String, Object> params = new HashMap<>(2);
        params.put("username", user.getUsername());
        params.put("password", newPassword.getText().toString());
        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REGISTERED_USER)
                .setContext(this)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        user.setPassword(newPassword.getText().toString());
                        Toast.makeText(ChangePasswordActivity.this, ChangePasswordActivity.this.getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, ChangePasswordActivity.this.getString(R.string.error_during_operation), Toast.LENGTH_SHORT).show();
                    }
                })
                .setObjectToSend(params)
                .build()
                .getData();
    }

    /**
     * A function that manages to verify if the values inserted when trying to change password are correct.
     * if they aren't, errors are set where necessary.
     */
    private void verifyFields() {
        if (oldPassword.getText().toString().equals("")) {
            oldPassword.setError(getString(R.string.error_fields_empty));
            changeP.setEnabled(false);
        } else {
            oldPassword.setError(null);
            if (newPassword.getText().toString().equals("")) {
                newPassword.setError(getString(R.string.error_fields_empty));
            } else {
                newPassword.setError(null);
                if (newPassword.getText().toString().equals(verifyNewPassword.getText().toString())) {
                    verifyNewPassword.setError(null);
                    changeP.setEnabled(true);
                } else {
                    verifyNewPassword.setError(getString(R.string.notequals));
                    changeP.setEnabled(false);
                }
            }
        }
    }
}

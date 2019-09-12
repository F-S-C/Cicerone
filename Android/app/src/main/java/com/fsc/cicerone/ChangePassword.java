package com.fsc.cicerone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fsc.cicerone.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class ChangePassword extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText verifyNewPassword;
    private Button changeP;
    private static final String ERROR_TAG = "ERROR IN " + ItineraryCreation.class.getName();

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

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyFields();
            }
        });

        newPassword.setOnFocusChangeListener((view, hasFocus) -> verifyFields());

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyFields();
            }
        });

        newPassword.setOnFocusChangeListener((view, hasFocus) -> verifyFields());

        verifyNewPassword.setOnFocusChangeListener((view, hasFocus) -> verifyFields());

        verifyNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyFields();
            }
        });

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

    private void changePasswordOnServer() {
        User user = AccountManager.getCurrentLoggedUser();
        JSONObject params = new JSONObject();
        try {
            params.put("username", user.getUsername());
            params.put("password", newPassword.getText().toString());
            BooleanConnector connector = new BooleanConnector(
                    ConnectorConstants.UPDATE_REGISTERED_USER,
                    new BooleanConnector.CallbackInterface() {
                        @Override
                        public void onStartConnection() {
                            // Do nothing
                        }

                        @Override
                        public void onEndConnection(BooleanConnector.BooleanResult result) {
                            if (result.getResult()) {
                                user.setPassword(newPassword.getText().toString());
                                Toast.makeText(ChangePassword.this, ChangePassword.this.getString(R.string.password_changed), Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(ChangePassword.this, ChangePassword.this.getString(R.string.error_during_operation), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    params);
            connector.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

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

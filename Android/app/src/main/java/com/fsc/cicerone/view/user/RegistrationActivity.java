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

package com.fsc.cicerone.view.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.mailer.Mailer;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.LanguageManager;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.Sex;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.system.SplashActivity;
import com.hootsuite.nachos.NachoTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * A class that represents the user interface for a user's registration.
 */
public class RegistrationActivity extends AppCompatActivity {

    private static final String ERROR_TAG = "ERROR IN " + RegistrationActivity.class.getName();
    private EditText username;
    private EditText email;
    private EditText name;
    private EditText surname;
    private EditText birthDate;
    private EditText fiscalCode;
    private EditText cellphone;
    private EditText password;
    private EditText docNumber;
    private EditText docType;
    private EditText expDate;
    private CheckBox checkPrivacyPolicy;
    private Spinner sex;
    private NachoTextView nachoTextView;
    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[a-zA-Z0-9]+");
    private static final Pattern LETTERS_AND_SPACES = Pattern.compile("[a-zA-Z ]+");
    private static final Pattern CONTROL_PHONE_NUMBER = Pattern.compile("[+]?[0-9]+");
    private Calendar birthCalendar = Calendar.getInstance(TimeZone.getDefault());
    private Calendar expCalendar = Calendar.getInstance(TimeZone.getDefault());
    private LanguageManager languages = new LanguageManager();
    private TextView privacyPolicyText;

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_registration);
        LanguageManager languageManager = new LanguageManager();

        ViewFlipper viewFlipper = findViewById(R.id.registrationView);
        username = findViewById(R.id.registrationUsername);
        email = findViewById(R.id.registrationEmail);
        name = findViewById(R.id.registrationName);
        surname = findViewById(R.id.registrationSurname);
        birthDate = findViewById(R.id.registrationBirthDate);
        sex = findViewById(R.id.registrationSex);
        fiscalCode = findViewById(R.id.registrationFiscalCode);
        cellphone = findViewById(R.id.registrationCellphone);
        password = findViewById(R.id.passwordText);
        Button next = findViewById(R.id.registrationNextButton);
        docNumber = findViewById(R.id.registrationDocNumber);
        docType = findViewById(R.id.registrationDocumentType);
        expDate = findViewById(R.id.registrationExpDate);
        Button signup = findViewById(R.id.signUpButton);
        createCustomSexSpinner(sex);
        viewFlipper.setInAnimation(this, R.anim.in_from_right);
        viewFlipper.setOutAnimation(this, R.anim.out_to_left);
        nachoTextView = findViewById(R.id.selectLanguage);
        checkPrivacyPolicy = findViewById(R.id.privacyCheckBox);
        privacyPolicyText = findViewById(R.id.privacypolicy);
        birthCalendar.add(Calendar.YEAR, -18);
        expCalendar.add(Calendar.DAY_OF_MONTH, 1);

        privacyPolicyText.setOnClickListener(v -> {
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectorConstants.PRIVACY_POLICY));
            startActivity(launchBrowser);
        });

        DatePickerDialog.OnDateSetListener birthDateSelect = (view, year, monthOfYear, dayOfMonth) -> {
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, monthOfYear);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateWithCalendarValue(birthDate, birthCalendar.getTime());
        };

        DatePickerDialog.OnDateSetListener expDateSelect = (view, year, monthOfYear, dayOfMonth) -> {
            expCalendar.set(Calendar.YEAR, year);
            expCalendar.set(Calendar.MONTH, monthOfYear);
            expCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateWithCalendarValue(expDate, expCalendar.getTime());
        };

        birthDate.setOnClickListener(view -> {
            hideKeyboard(this);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, birthDateSelect, birthCalendar
                    .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                    birthCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(birthCalendar.getTimeInMillis());
            datePickerDialog.show();
        });

        expDate.setOnClickListener(view -> {
            hideKeyboard(this);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, expDateSelect, expCalendar
                    .get(Calendar.YEAR), expCalendar.get(Calendar.MONTH),
                    expCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(expCalendar.getTimeInMillis());
            datePickerDialog.show();
        });


        next.setOnClickListener(view -> {
            if (validateFirstPageData()) {
                next.setText(R.string.loading);
                next.setEnabled(false);
                AccountManager.checkIfUsernameExists(RegistrationActivity.this, username.getText().toString().trim().toLowerCase(), resultUser -> {
                    if (resultUser) {
                        username.setError(getString(R.string.username_already_exists));
                        next.setText(R.string.next);
                        next.setEnabled(true);
                    } else {
                        AccountManager.checkIfEmailExists(RegistrationActivity.this, email.getText().toString().trim().toLowerCase(), resultEmail -> {
                            if (resultEmail) {
                                email.setError(getString(R.string.email_already_exists));
                                next.setText(R.string.next);
                                next.setEnabled(true);
                            } else {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, languageManager.getLanguagesNames());
                                nachoTextView.setAdapter(adapter);
                                nachoTextView.disableEditChipOnTouch();
                                viewFlipper.showNext();
                            }
                        });
                    }
                });
            }
        });

        username.addTextChangedListener(new TextWatcher() {

            /**
             * @see android.text.TextWatcher#afterTextChanged(Editable)
             */
            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }

            /**
             * @see android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Do nothing
            }

            /**
             * @see android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)
             */
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (User.validateUsername(username.getText().toString().trim())) {
                    username.setError(null);
                } else {
                    username.setError(getString(R.string.username_error));
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {

            /**
             * @see android.text.TextWatcher#afterTextChanged(Editable)
             */
            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }


            /**
             * @see android.text.TextWatcher#beforeTextChanged(CharSequence, int, int, int)
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Do nothing
            }

            /**
             * @see android.text.TextWatcher#onTextChanged(CharSequence, int, int, int)
             */
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (User.validateEmail(email.getText().toString().trim())) {
                    email.setError(null);
                } else {
                    email.setError(getString(R.string.email_not_valid));
                }
            }
        });

        signup.setOnClickListener(view -> {
            if (validateSecondPageData()) {
                signup.setText(R.string.loading);
                signup.setEnabled(false);
                User newUser = setNewUser();
                AccountManager.insertUser(this, newUser, result -> {
                    if (result) {
                        Intent i = new Intent(this, SplashActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                        Mailer.sendRegistrationConfirmationEmail(this, newUser, res -> {
                            if (!res) {
                                Toast.makeText(this, getString(R.string.error_send_email), Toast.LENGTH_LONG).show();
                            }
                        });
                        startActivity(i);
                    } else {
                        signup.setText(R.string.sign_up);
                        signup.setEnabled(true);
                        Toast.makeText(this, getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    /**
     * A function that check if a given text contains letters and numbers without space.
     *
     * @param text The given text to control.
     * @return True if it doesn't match, False if it does.
     */
    private boolean specialCharactersNoSpace(String text) {
        return !LETTERS_NUMBERS.matcher(text).matches();
    }

    /**
     * A function that check if a given text contains letter and spaces.
     *
     * @param text The given text to control.
     * @return True if it doesn't match, False if it does.
     */
    private boolean specialCharactersSpace(String text) {
        return !LETTERS_AND_SPACES.matcher(text).matches();
    }

    /**
     * A function that check if a given text contains only numbers with no space in between.
     *
     * @param text The given text to control.
     * @return True if the text matches the criteria, false if it doesn't.
     */
    private boolean onlyNumbersNoSpace(String text) {
        return CONTROL_PHONE_NUMBER.matcher(text).matches();
    }

    /**
     * A function that check if all the values inserted in the first page are correct.
     *
     * @return True if every field is correct, False otherwise.
     */
    private boolean validateFirstPageData() {
        name.setError(null);
        surname.setError(null);
        birthDate.setError(null);
        fiscalCode.setError(null);
        cellphone.setError(null);
        password.setError(null);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - 18);
        Date majDate = cal.getTime();

        if (!User.validateUsername(username.getText().toString().trim())) {
            username.setError(getString(R.string.username_error));
            return false;
        } else if (!User.validateEmail(email.getText().toString().trim())) {
            email.setError(getString(R.string.email_not_valid));
            return false;
        } else if (name.getText().toString().trim().isEmpty() || specialCharactersSpace(name.getText().toString().trim())) {
            name.setError(getString(R.string.not_valid_input));
            return false;
        } else if (surname.getText().toString().trim().isEmpty() || specialCharactersSpace(surname.getText().toString().trim())) {
            surname.setError(getString(R.string.not_valid_input));
            return false;
        } else if (birthDate.getText().toString().trim().isEmpty()) {
            birthDate.setError(getString(R.string.error_fields_empty));
            Toast.makeText(this, getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (majDate.compareTo(strToDate(birthDate.getText().toString())) < 1) {
            birthDate.setError(getString(R.string.major_date_required));
            Toast.makeText(this, getString(R.string.major_date_required), Toast.LENGTH_SHORT).show();
            return false;
        } else if (fiscalCode.getText().toString().trim().isEmpty() || specialCharactersNoSpace(fiscalCode.getText().toString().trim())) {
            fiscalCode.setError(getString(R.string.not_valid_input));
            return false;
        } else if (cellphone.getText().toString().trim().isEmpty() || !onlyNumbersNoSpace(cellphone.getText().toString().trim())) {
            cellphone.setError(getString(R.string.not_valid_input));
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            password.setError(getString(R.string.error_fields_empty));
            return false;
        } else {
            return true;
        }
    }


    /**
     * A function that checks if all the values inserted in the second page are correct.
     *
     * @return True if every field is correct, False otherwise.
     */
    private boolean validateSecondPageData() {
        docNumber.setError(null);
        docType.setError(null);
        expDate.setError(null);
        nachoTextView.setError(null);
        privacyPolicyText.setError(null);

        if (docNumber.getText().toString().trim().isEmpty() || specialCharactersNoSpace(docNumber.getText().toString().trim())) {
            docNumber.setError(getString(R.string.document_not_valid));
            return false;
        } else if (docType.getText().toString().trim().isEmpty()) {
            docType.setError(getString(R.string.error_fields_empty));
            return false;
        } else if (expDate.getText().toString().trim().isEmpty()) {
            expDate.setError(getString(R.string.error_fields_empty));
            Toast.makeText(this, getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (Calendar.getInstance().getTime().compareTo(strToDate(expDate.getText().toString())) > 0) {
            expDate.setError(getString(R.string.date_invalid));
            Toast.makeText(this, getString(R.string.date_invalid), Toast.LENGTH_SHORT).show();
            return false;
        } else if (nachoTextView.getChipValues().size() < 1) {
            nachoTextView.setError(getString(R.string.error_fields_empty));
            return false;
        } else if (!checkPrivacyPolicy.isChecked()) {
            privacyPolicyText.setError(getString(R.string.privacy_policy_required));
            return false;
        } else {
            docNumber.setError(null);
            docType.setError(null);
            expDate.setError(null);
            nachoTextView.setError(null);
            privacyPolicyText.setError(null);
            return true;
        }
    }

    /**
     * A function that set the possible values for the field Sex in a given Spinner.
     *
     * @param spinner The spinner to set.
     */
    private void createCustomSexSpinner(Spinner spinner) {
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.male));
        list.add(getResources().getString(R.string.female));
        list.add(getResources().getString(R.string.other));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * A function that creates a variable User using the values inserted in the fields by the user.
     *
     * @return The User that has been built.
     */
    private User setNewUser() {
        List<String> lanSelected = nachoTextView.getChipValues();
        return new User.Builder(username.getText().toString().trim().toLowerCase(), password.getText().toString())
                .setUserType(UserType.GLOBETROTTER)
                .setSex(Sex.getValue(sex.getSelectedItem().toString().toLowerCase()))
                .setBirthDate(strToDate(birthDate.getText().toString()))
                .setCellphone(cellphone.getText().toString().trim())
                .setEmail(email.getText().toString().trim().toLowerCase())
                .setName(name.getText().toString().trim())
                .setSurname(surname.getText().toString().trim())
                .setTaxCode(fiscalCode.getText().toString().trim().toLowerCase())
                .setLanguages(new HashSet<>(languages.getLanguagesFromNames(lanSelected)))
                .setDocument(new Document(docNumber.getText().toString().trim().toLowerCase(), docType.getText().toString().trim(), expDate.getText().toString()))
                .build();
    }

    /**
     * A function that converts a given String to a Date,  in the format 'dd/MM/yyyy'
     *
     * @param text The given text to convert.
     * @return The result of the conversion
     */
    private Date strToDate(String text) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            date = format.parse(text);
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
        }
        return date;
    }

    /**
     * A function that updates the values of an EditText, by converting a Date to a String and
     * setting it in the EditText.
     *
     * @param update The EditTet to update.
     * @param date   The Date to set in the EditText.
     */
    private void updateWithCalendarValue(EditText update, Date date) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        update.setText(sdf.format(date));
    }

    /**
     * A function that hide the input keyboard in the given activity.
     *
     * @param activity The given activity
     */
    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

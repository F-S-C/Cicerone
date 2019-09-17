package com.fsc.cicerone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.LanguageManager;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.Sex;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.hootsuite.nachos.NachoTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;

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
    private Spinner sex;
    private ViewFlipper viewFlipper;
    private NachoTextView nachoTextView;
    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[a-zA-Z0-9]+");
    private static final Pattern LETTERS_AND_SPACES = Pattern.compile("[a-zA-Z ]+");
    private static final Pattern CONTROL_PHONE_NUMBER = Pattern.compile("[+]?[0-9]+");
    private Calendar birthCalendar = Calendar.getInstance(TimeZone.getDefault());
    private Calendar expCalendar = Calendar.getInstance(TimeZone.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_registration);
        LanguageManager languages = new LanguageManager();

        viewFlipper = findViewById(R.id.registrationView);
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
            new DatePickerDialog(this, birthDateSelect, birthCalendar
                    .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                    birthCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        expDate.setOnClickListener(view -> {
            hideKeyboard(this);
            new DatePickerDialog(this, expDateSelect, expCalendar
                    .get(Calendar.YEAR), expCalendar.get(Calendar.MONTH),
                    expCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        next.setOnClickListener(view -> {
            if (checkConnection()) {
                if (validateFirstPageData()) {
                    next.setText(R.string.loading);
                    next.setEnabled(false);
                    AccountManager.checkIfUsernameExists(username.getText().toString().trim().toLowerCase(), resultUser -> {
                        if (resultUser) {
                            username.setError(getString(R.string.username_already_exists));
                            next.setText(R.string.next);
                            next.setEnabled(true);
                        } else {
                            AccountManager.checkIfEmailExists(email.getText().toString().trim().toLowerCase(), resultEmail -> {
                                if (resultEmail) {
                                    email.setError(getString(R.string.email_already_exists));
                                    next.setText(R.string.next);
                                    next.setEnabled(true);
                                } else {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, languages.getLanguagesNames());
                                    nachoTextView.setAdapter(adapter);
                                    nachoTextView.disableEditChipOnTouch();
                                    viewFlipper.showNext();
                                }
                            });
                        }
                    });
                }
            } else {
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
        });

        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Do nothing
            }

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

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // Do nothing
            }

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
            if (checkConnection()) {
                if (validateSecondPageData()) {
                    signup.setText(R.string.loading);
                    signup.setEnabled(false);
                    AccountManager.insertUser(setNewUser(), result -> {
                        if (result) {
                            AccountManager.insertUserDocument(username.getText().toString().trim().toLowerCase(), new Document(docNumber.getText().toString().trim().toLowerCase(), docType.getText().toString().trim(), expDate.getText().toString()),
                                    ins -> {
                                        if (ins) {
                                            ArrayList<String> lanSelected = new ArrayList<>(nachoTextView.getChipValues());
                                            languages.setUserLanguages(username.getText().toString().trim().toLowerCase(), languages.getLanguagesFromNames(lanSelected));
                                            Intent i = new Intent(this, SplashActivity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                            startActivity(i);
                                        } else {
                                            signup.setText(R.string.sign_up);
                                            signup.setEnabled(true);
                                            Toast.makeText(this, getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            signup.setText(R.string.sign_up);
                            signup.setEnabled(true);
                            Toast.makeText(this, getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean specialCharactersNoSpace(String text) {
        return !LETTERS_NUMBERS.matcher(text).matches();
    }

    private boolean specialCharactersSpace(String text) {
        return !LETTERS_AND_SPACES.matcher(text).matches();
    }

    private boolean onlyNumbersNoSpace(String text) {
        return CONTROL_PHONE_NUMBER.matcher(text).matches();
    }

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


    private boolean validateSecondPageData() {
        docNumber.setError(null);
        docType.setError(null);
        expDate.setError(null);
        nachoTextView.setError(null);

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
        } else {
            docNumber.setError(null);
            docType.setError(null);
            nachoTextView.setError(null);
            return true;
        }
    }

    private void createCustomSexSpinner(Spinner spinner) {
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.male));
        list.add(getResources().getString(R.string.female));
        list.add(getResources().getString(R.string.other));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(dataAdapter);
    }

    private User setNewUser() {
        User user = new User();
        user.setUsername(username.getText().toString().trim().toLowerCase());
        user.setPassword(password.getText().toString());
        user.setUserType(UserType.GLOBETROTTER);
        user.setSex(Sex.getValue(sex.getSelectedItem().toString().toLowerCase()));
        user.setBirthDate(strToDate(birthDate.getText().toString()));
        user.setCellphone(cellphone.getText().toString().trim());
        user.setEmail(email.getText().toString().trim().toLowerCase());
        user.setName(name.getText().toString().trim());
        user.setSurname(surname.getText().toString().trim());
        user.setTaxCode(fiscalCode.getText().toString().trim().toLowerCase());
        return user;
    }

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

    private void updateWithCalendarValue(EditText update, Date date) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        update.setText(sdf.format(date));
    }

    private static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }
}

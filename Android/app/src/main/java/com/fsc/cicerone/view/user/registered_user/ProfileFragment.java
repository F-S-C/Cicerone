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

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.Config;
import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.Sex;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.system.SplashActivity;
import com.fsc.cicerone.view.system.Refreshable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class ProfileFragment extends Fragment implements Refreshable {

    private static final String ERROR_TAG = "ERROR IN " + ProfileFragment.class.getName();
    private TextInputEditText name;
    private TextInputEditText surname;
    private TextInputEditText email;
    private TextInputEditText cellphone;
    private TextInputEditText birthDate;
    private TextInputEditText documentNumber;
    private TextInputEditText documentType;
    private TextInputEditText documentExpiryDate;
    private Button switchButton;
    private Calendar birthCalendar;
    private Calendar expCalendar = Calendar.getInstance(TimeZone.getDefault());
    private Spinner sexList;

    private SharedPreferences preferences;

    private Activity context;
    private static final int PERMISSION_REQUEST_CODE = 357;
    private static final String IT_DATE_FORMAT = "dd/MM/yyyy";
    private View holderView;

    /**
     * Empty constructor
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * @see androidx.fragment.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences(Config.SHARED_PREF_KEY, Context.MODE_PRIVATE);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        email = view.findViewById(R.id.email);
        cellphone = view.findViewById(R.id.cellphone);
        birthDate = view.findViewById(R.id.registrationBirthDate);
        documentNumber = view.findViewById(R.id.documentNrText);
        documentType = view.findViewById(R.id.documentType);
        documentExpiryDate = view.findViewById(R.id.documentExpiryDateText);
        Button logoutButton = view.findViewById(R.id.logout);
        switchButton = view.findViewById(R.id.switch_to_cicerone);
        Button modifyButton = view.findViewById(R.id.modifyButton);
        sexList = view.findViewById(R.id.sexList);
        Button changePaswButton = view.findViewById(R.id.change_passw_btn);
        birthCalendar = toCalendar(AccountManager.getCurrentLoggedUser().getBirthDate());
        DatePickerDialog.OnDateSetListener birthDateSelect = (view12, year, monthOfYear, dayOfMonth) -> {
            birthCalendar.set(Calendar.YEAR, year);
            birthCalendar.set(Calendar.MONTH, monthOfYear);
            birthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateBirth();
        };

        DatePickerDialog.OnDateSetListener expDateSelect = (view13, year, monthOfYear, dayOfMonth) -> {
            expCalendar.set(Calendar.YEAR, year);
            expCalendar.set(Calendar.MONTH, monthOfYear);
            expCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateExpDate();
        };

        addItemsSex(sexList);
        refresh();

        switchButton.setOnClickListener(view1 -> new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.are_you_sure))
                .setMessage(context.getString(R.string.answer_switch_to_cicerone))
                .setPositiveButton(context.getString(R.string.yes), ((dialog, which) -> switchToCicerone()))
                .setNegativeButton(context.getString(R.string.no), null)
                .show());

        name.setEnabled(false);
        surname.setEnabled(false);
        email.setEnabled(false);
        cellphone.setEnabled(false);
        birthDate.setEnabled(false);
        documentNumber.setEnabled(false);
        documentType.setEnabled(false);
        documentExpiryDate.setEnabled(false);
        sexList.setEnabled(false);

        logoutButton.setOnClickListener(view1 -> new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.are_you_sure))
                .setMessage(context.getString(R.string.exit_confirm_answer))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    AccountManager.logout(getActivity());
                    preferences.edit().remove("session").apply();
                    Intent i = new Intent(getActivity(), SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .setNegativeButton(getString(R.string.no), null)
                .show());

        modifyButton.setOnClickListener(view1 -> {
            if (!name.isEnabled()) {
                name.setEnabled(true);
                surname.setEnabled(true);
                email.setEnabled(true);
                cellphone.setEnabled(true);
                birthDate.setEnabled(true);
                documentNumber.setEnabled(true);
                documentType.setEnabled(true);
                documentExpiryDate.setEnabled(true);
                sexList.setEnabled(true);
                modifyButton.setText(view1.getContext().getString(R.string.save));
            } else {
                if (allFilled()) {
                    name.setEnabled(false);
                    surname.setEnabled(false);
                    email.setEnabled(false);
                    cellphone.setEnabled(false);
                    birthDate.setEnabled(false);
                    documentNumber.setEnabled(false);
                    documentType.setEnabled(false);
                    documentExpiryDate.setEnabled(false);
                    sexList.setEnabled(false);
                    Toast.makeText(view1.getContext(), view1.getContext().getString(R.string.updating), Toast.LENGTH_LONG).show();
                    modifyButton.setText(view1.getContext().getString(R.string.modify));
                    updateUserData();
                } else {
                    if (name.getText().toString().equals(""))
                        name.setError(getString(R.string.empty_name_error));
                    if (surname.getText().toString().equals(""))
                        surname.setError(getString(R.string.empty_surname_error));
                    if (email.getText().toString().equals(""))
                        email.setError(getString(R.string.empty_email_error));
                    if (cellphone.getText().toString().equals(""))
                        cellphone.setError(getString(R.string.empty_cellphone_error));
                    if (birthDate.getText().toString().equals(""))
                        birthDate.setError(getString(R.string.empty_birthday_error));
                    if (documentNumber.getText().toString().equals(""))
                        documentNumber.setError(getString(R.string.empty_document_number_error));
                    if (documentType.getText().toString().equals(""))
                        documentType.setError(getString(R.string.empty_document_type_error));
                    if (documentExpiryDate.getText().toString().equals(""))
                        documentExpiryDate.setError(getString(R.string.empty_document_expiry_date_error));
                }
            }
        });

        birthDate.setOnClickListener(view1 -> new DatePickerDialog(getActivity(), birthDateSelect, birthCalendar
                .get(Calendar.YEAR), birthCalendar.get(Calendar.MONTH),
                birthCalendar.get(Calendar.DAY_OF_MONTH)).show());

        documentExpiryDate.setOnClickListener(view1 -> new DatePickerDialog(getActivity(), expDateSelect, expCalendar
                .get(Calendar.YEAR), expCalendar.get(Calendar.MONTH),
                expCalendar.get(Calendar.DAY_OF_MONTH)).show());

        changePaswButton.setOnClickListener(view1 -> {
            Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
            getActivity().startActivity(i);
        });

        Button deleteAccountButton = view.findViewById(R.id.deleteAccountButton);
        deleteAccountButton.setOnClickListener(v -> deleteAccount());

        Button downloadUserDataButton = view.findViewById(R.id.downloadUserData);
        downloadUserDataButton.setOnClickListener(v -> requestUserData());

        holderView = view;

        return view;
    }

    /**
     * Create a list of string used for the Spinner present in the page.
     *
     * @param spinner The spinner where to insert the list.
     */
    private void addItemsSex(Spinner spinner) {

        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.male));
        list.add(getResources().getString(R.string.female));
        list.add(getResources().getString(R.string.other));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh()
     */
    @Override
    public void refresh() {
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        if (currentLoggedUser.getSex() == Sex.MALE) {
            sexList.setSelection(0);
        } else if (currentLoggedUser.getSex() == Sex.FEMALE) {
            sexList.setSelection(1);
        } else {
            sexList.setSelection(2);
        }
        name.setText(currentLoggedUser.getName());
        surname.setText(currentLoggedUser.getSurname());
        email.setText(currentLoggedUser.getEmail());
        cellphone.setText(currentLoggedUser.getCellphone());
        if (currentLoggedUser.getUserType() == UserType.CICERONE) {
            switchButton.setVisibility(View.GONE);
        } else {
            switchButton.setVisibility(View.VISIBLE);
        }

        if (getParentFragment() instanceof AccountDetailsFragment) {
            ((AccountDetailsFragment) getParentFragment()).refresh();
        }

        DateFormat outputFormat = new SimpleDateFormat(IT_DATE_FORMAT, Locale.US);
        birthDate.setText(outputFormat.format(currentLoggedUser.getBirthDate()));
        documentNumber.setText(currentLoggedUser.getDocument().getNumber());
        documentType.setText(currentLoggedUser.getDocument().getType());
        documentExpiryDate.setText(outputFormat.format(currentLoggedUser.getDocument().getExpirationDate()));
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh(SwipeRefreshLayout)
     */
    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        refresh();
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * A function that allows the logged User to become a Cicerone.
     */
    private void switchToCicerone() {
        final Map<String, Object> parameters = new HashMap<>(2);
        parameters.put(User.Columns.USERNAME_KEY, AccountManager.getCurrentLoggedUser().getUsername());
        parameters.put(User.Columns.USER_TYPE_KEY, UserType.CICERONE.toInt());

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REGISTERED_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), getString(R.string.operation_completed), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), SplashActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                    }

                })
                .setObjectToSend(parameters)
                .build()
                .getData();

    }

    /**
     * A function that manage to update the Birth date.
     */
    private void updateBirth() {
        SimpleDateFormat sdf = new SimpleDateFormat(IT_DATE_FORMAT, Locale.US);
        birthDate.setText(sdf.format(birthCalendar.getTime()));
    }

    /**
     * A function that manage to update the expire date of the User's Document.
     */
    private void updateExpDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(IT_DATE_FORMAT, Locale.US);
        documentExpiryDate.setText(sdf.format(expCalendar.getTime()));
    }

    /**
     * A function that manage to update the User data.
     */
    private void updateUserData() {
        Map<String, Object> userData = new HashMap<>();
        Map<String, Object> documentData = new HashMap<>();
        User user = AccountManager.getCurrentLoggedUser();
        userData.put(User.Columns.USERNAME_KEY, user.getUsername());
        userData.put(User.Columns.PASSWORD_KEY, user.getPassword());
        userData.put(User.Columns.NAME_KEY, name.getText());
        userData.put(User.Columns.SURNAME_KEY, surname.getText());
        userData.put(User.Columns.EMAIL_KEY, email.getText());
        userData.put(User.Columns.CELLPHONE_KEY, cellphone.getText());
        Sex sexSelected;
        switch (sexList.getSelectedItemPosition()) {
            case 0:
                sexSelected = Sex.MALE;
                break;
            case 1:
                sexSelected = Sex.FEMALE;
                break;
            default:
                sexSelected = Sex.OTHER;
                break;
        }
        userData.put(User.Columns.SEX_KEY, sexSelected.toString());
        userData.put(User.Columns.BIRTH_DATE_KEY, Objects.requireNonNull(itDateToServerDate(birthDate.getText().toString())));

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REGISTERED_USER)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (!result.getResult())
                        Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_SHORT).show();
                    user.setName(name.getText().toString());
                    user.setSurname(surname.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setCellphone(cellphone.getText().toString());
                    user.setBirthDate(strToDate(birthDate.getText().toString()));
                    user.setSex(Sex.getValue(sexList.getSelectedItem().toString().toLowerCase()));
                })
                .setObjectToSend(userData)
                .build()
                .getData();

        documentData.put(User.Columns.USERNAME_KEY, user.getUsername());
        documentData.put(Document.Columns.EXPIRY_DATE_KEY, Objects.requireNonNull(itDateToServerDate(documentExpiryDate.getText().toString())));
        documentData.put(Document.Columns.DOCUMENT_TYPE_KEY, Objects.requireNonNull(documentType.getText()));
        documentData.put(Document.Columns.DOCUMENT_NUMBER_KEY, Objects.requireNonNull(documentNumber.getText()));
        new BooleanConnector.Builder(ConnectorConstants.UPDATE_DOCUMENT)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (!result.getResult())
                        Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                    Document newUserDoc = new Document(documentNumber.getText().toString(), documentType.getText().toString(), strToDate(documentExpiryDate.getText().toString()));
                    user.setDocument(newUserDoc);
                    refresh();
                })
                .setObjectToSend(documentData)
                .build()
                .getData();
        Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }

    /**
     * A function that converts a given String to a Date.
     * @param text The given String
     * @return The converted Date
     */
    private Date strToDate(String text) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(IT_DATE_FORMAT, Locale.US);
        try {
            date = format.parse(text);
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
        }
        return date;
    }

    /**
     * A function that converts a given date in String type, to a Date in the Server Format.
     * @param dateToConvert The date to convert
     * @return The converted Date
     */
    private String itDateToServerDate(String dateToConvert) {
        try {
            DateFormat serverDate = new SimpleDateFormat(ConnectorConstants.DATE_FORMAT, Locale.US);
            DateFormat itDate = new SimpleDateFormat(IT_DATE_FORMAT, Locale.US);
            return serverDate.format(itDate.parse(dateToConvert));
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
            return null;
        }
    }

    /**
     * A function that checks if every field is filled.
     * @return True if every field is filled, False otherwise.
     */
    private boolean allFilled() {
        return !name.getText().toString().equals("")
                && !surname.getText().toString().equals("")
                && !email.getText().toString().equals("")
                && !cellphone.getText().toString().equals("")
                && !birthDate.getText().toString().equals("")
                && !documentNumber.getText().toString().equals("")
                && !documentType.getText().toString().equals("")
                && !documentExpiryDate.getText().toString().equals("");
    }

    /**
     * A function that converts a Date in an object Calendar and returns it.
     * @param date The Date to set.
     * @return The Calendar object.
     */
    private static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * A function that allows the logged User to request the download of his data.
     */
    private void requestUserData() {
        checkPermission();
    }

    /**
     * A function that manage to check the necessary permissions before allowing the logged User to download his data.
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // show an alert dialog
                    new MaterialAlertDialogBuilder(context)
                            .setMessage(getString(R.string.external_storage_permission_required_message))
                            .setTitle(getString(R.string.please_grant_permission))
                            .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE))
                            .setNegativeButton(getString(R.string.cancel), null)
                            .show();
                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            } else {
                downloadUserData();
            }
        } else {
            downloadUserData();
        }
    }

    /**
     * @see androidx.fragment.app.Fragment#onRequestPermissionsResult(int, String[], int[])
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadUserData();
            } else {
                Toast.makeText(context, getString(R.string.storage_permission_required), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * A function that allows the logged User to download his data.
     */
    private void downloadUserData() {
        final WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        Uri uri = Uri.parse(ConnectorConstants.DOWNLOAD_USER_DATA)
                .buildUpon()
                .appendQueryParameter(User.Columns.USERNAME_KEY, currentLoggedUser.getUsername())
                .appendQueryParameter(User.Columns.PASSWORD_KEY, currentLoggedUser.getPassword())
                .build();

        webView.loadUrl(uri.toString());
        LinearLayout rootLayout = holderView.findViewById(R.id.scroll_details);
        rootLayout.addView(webView);

        webView.setDownloadListener((url, userAgent, contentDescription, mimetype, contentLength) -> {
            // The download request
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.allowScanningByMediaScanner();

            // Set the notification visibility
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Set the destination on the device
            String fileName = URLUtil.guessFileName(url, contentDescription, mimetype);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            // Enqueue the download
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            dManager.enqueue(request);
            webView.destroy();
        });
    }

    /**
     * A function that manages the deletion of the account of the logged User.
     */
    public void deleteAccount() {
        DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
            AccountManager.deleteCurrentAccount(context);
            preferences.edit().remove("session").apply();
            startActivity(new Intent(context, LoginActivity.class));
            context.finish();
        };
        new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.are_you_sure))
                .setMessage(context.getString(R.string.sure_to_delete_account))
                .setPositiveButton(context.getString(R.string.yes), positiveClickListener)
                .setNegativeButton(context.getString(R.string.no), null)
                .show();
    }
}
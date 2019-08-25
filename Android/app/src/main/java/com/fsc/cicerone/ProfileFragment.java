package com.fsc.cicerone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class ProfileFragment extends Fragment {

    private static final String ERROR_TAG = "ERROR IN " + ProfileFragment.class.getName();
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText cellphone;
    private EditText birthDate;
    private EditText documentNumber;
    private EditText documentType;
    private EditText documentExpiryDate;
    private Dialog switchToCiceroneDialog;
    private Button switchButton;
    private Button modifyButton;
    private Calendar birthCalendar;
    private Calendar expCalendar = Calendar.getInstance(TimeZone.getDefault());
    private Spinner sexList;

    private SharedPreferences preferences;

    private Activity context;
    private static final int PERMISSION_REQUEST_CODE = 357;
    private View holderView;

    /**
     * Empty constructor
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        Dialog logoutDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(logoutDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        logoutDialog.setContentView(R.layout.activity_logout);
        logoutDialog.setCancelable(true);

        switchToCiceroneDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(switchToCiceroneDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        switchToCiceroneDialog.setContentView(R.layout.switch_to_cicerone);
        switchToCiceroneDialog.setCancelable(true);

        preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);
        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname_textbox);
        email = view.findViewById(R.id.email);
        cellphone = view.findViewById(R.id.cellphone);
        birthDate = view.findViewById(R.id.registrationBirthDate);
        documentNumber = view.findViewById(R.id.documentNrText);
        documentType = view.findViewById(R.id.documentType);
        documentExpiryDate = view.findViewById(R.id.documentExpiryDateText);
        Button logoutButton = view.findViewById(R.id.logout);
        switchButton = view.findViewById(R.id.switch_to_cicerone);
        modifyButton = view.findViewById(R.id.modifyButton);
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
        try {
            requestUserData(sexList);
            final JSONObject updateParams = new JSONObject();
            updateParams.put("username", AccountManager.getCurrentLoggedUser().getUsername());
            updateParams.put("user_type", AccountManager.getCurrentLoggedUser().getUserType().toInt());

            switchButton.setOnClickListener(view1 -> {
                Button noButton = switchToCiceroneDialog.findViewById(R.id.switch_cicerone_no_button);
                noButton.setOnClickListener(v -> switchToCiceroneDialog.hide());

                Button yesButton = switchToCiceroneDialog.findViewById(R.id.switch_cicerone_yes_button);
                yesButton.setOnClickListener(v -> {
                    switchToCiceroneDialog.hide();
                    switchToCicerone(updateParams);
                });

                switchToCiceroneDialog.show();
            });
        } catch (JSONException e) {
            Log.e("EXCEPTION", e.toString());
        }

        name.setEnabled(false);
        surname.setEnabled(false);
        email.setEnabled(false);
        cellphone.setEnabled(false);
        birthDate.setEnabled(false);
        documentNumber.setEnabled(false);
        documentType.setEnabled(false);
        documentExpiryDate.setEnabled(false);
        sexList.setEnabled(false);

        logoutButton.setOnClickListener(view1 -> {
            Button noButton = logoutDialog.findViewById(R.id.no_logout_button);
            noButton.setOnClickListener(v -> logoutDialog.hide());

            Button yesButton = logoutDialog.findViewById(R.id.yes_logout_button);
            yesButton.setOnClickListener(v -> {
                logoutDialog.hide();
                logoutDialog.dismiss();
                AccountManager.logout();
                preferences.edit().remove("session").apply();
                Intent i = new Intent(getActivity(), SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            });

            logoutDialog.show();
        });

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
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
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
            Intent i = new Intent(getActivity(), ChangePassword.class);
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

    private void requestUserData(Spinner spinner) {
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        if (currentLoggedUser.getSex() == Sex.MALE) {
            spinner.setSelection(0);
        } else if (currentLoggedUser.getSex() == Sex.FEMALE) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
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
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        birthDate.setText(outputFormat.format(currentLoggedUser.getBirthDate()));
        try {
            JSONObject parameters = new JSONObject();
            parameters.put("username", currentLoggedUser.getUsername());
            SendInPostConnector user_document_conn = new SendInPostConnector(ConnectorConstants.REQUEST_DOCUMENT, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    JSONObject data = jsonArray.getJSONObject(0);
                    documentNumber.setText(data.getString("document_number"));
                    documentType.setText(data.getString("document_type"));
                    try {
                        Date docExpiryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(data.getString("expiry_date"));
                        documentExpiryDate.setText(outputFormat.format(docExpiryDate));
                    } catch (ParseException e) {
                        Log.e(ERROR_TAG, e.toString());
                    }
                }
            });
            user_document_conn.setObjectToSend(parameters);
            user_document_conn.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    private void switchToCicerone(JSONObject parameters) {

        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.UPDATE_REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject userData = jsonArray.getJSONObject(0);

                if (userData.getBoolean("result")) {
                    switchToCiceroneDialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.operation_completed), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                }

            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();

    }

    private void updateBirth() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(sdf.format(birthCalendar.getTime()));
    }

    private void updateExpDate() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        documentExpiryDate.setText(sdf.format(expCalendar.getTime()));
    }

    private void updateUserData() {
        JSONObject userData = new JSONObject();
        JSONObject documentData = new JSONObject();
        try {
            User user = AccountManager.getCurrentLoggedUser();
            userData.put("username", user.getUsername());
            userData.put("password", user.getPassword());
            userData.put("name", name.getText());
            userData.put("surname", surname.getText());
            userData.put("email", email.getText());
            userData.put("cellphone", cellphone.getText());
            String sexSelected;
            switch (sexList.getSelectedItemPosition()) {
                case 0:
                    sexSelected = "male";
                    break;
                case 1:
                    sexSelected = "female";
                    break;
                default:
                    sexSelected = "other";
                    break;
            }
            userData.put("sex", sexSelected);
            userData.put("birth_date", itDateToServerDate(birthDate.getText().toString()));

            SendInPostConnector updateRegisteredUser = new SendInPostConnector(ConnectorConstants.UPDATE_REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    if (!jsonArray.getJSONObject(0).getBoolean("result"))
                        Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_SHORT).show();
                    user.setName(name.getText().toString());
                    user.setSurname(surname.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setCellphone(cellphone.getText().toString());
                    user.setBirthDate(strToDate(birthDate.getText().toString()));
                    user.setSex(Sex.getValue(sexList.getSelectedItem().toString().toLowerCase()));
                }
            });
            updateRegisteredUser.setObjectToSend(userData);
            updateRegisteredUser.execute();

            documentData.put("username", user.getUsername());
            documentData.put("expiry_date", itDateToServerDate(documentExpiryDate.getText().toString()));
            documentData.put("document_type", documentType.getText());
            documentData.put("document_number", documentNumber.getText());
            SendInPostConnector updateDocument = new SendInPostConnector(ConnectorConstants.UPDATE_DOCUMENT, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    if (!jsonArray.getJSONObject(0).getBoolean("result"))
                        Toast.makeText(getActivity(), getString(R.string.error_during_operation), Toast.LENGTH_LONG).show();
                    Document newUserDoc = new Document(documentNumber.getText().toString(), documentType.getText().toString(), strToDate(documentExpiryDate.getText().toString()));
                    user.setCurrentDocument(newUserDoc);
                    Intent i = new Intent(getActivity(), SplashActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
            updateDocument.setObjectToSend(documentData);
            updateDocument.execute();
            Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e("EXCEPTION", e.toString());
        }
    }

    private Date strToDate(String text) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            date = format.parse(text);
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
        }
        return date;
    }

    private String itDateToServerDate(String dateToConvert) {
        try {
            DateFormat serverDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            DateFormat itDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            return serverDate.format(itDate.parse(dateToConvert));
        } catch (ParseException e) {
            Log.e(ERROR_TAG, e.toString());
            return null;
        }
    }

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

    private static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private void requestUserData() {
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(getString(R.string.external_storage_permission_required_message));
                    builder.setTitle(getString(R.string.please_grant_permission));
                    builder.setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE));
                    builder.setNeutralButton(getString(R.string.cancel), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
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

    private void downloadUserData() {
        final WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        Uri uri = Uri.parse(ConnectorConstants.DOWNLOAD_USER_DATA)
                .buildUpon()
                .appendQueryParameter("username", currentLoggedUser.getUsername())
                .appendQueryParameter("password", currentLoggedUser.getPassword())
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

    public void deleteAccount() {
        DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
            AccountManager.deleteCurrentAccount();
            preferences.edit().remove("session").apply();
            startActivity(new Intent(context, LoginActivity.class));
            context.finish();
        };
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.are_you_sure))
                .setMessage(context.getString(R.string.sure_to_delete_account))
                .setPositiveButton(context.getString(R.string.yes), positiveClickListener)
                .setNegativeButton(context.getString(R.string.no), null)
                .show();
    }
}
package com.fsc.cicerone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the details of the user to whom the administrator is interested.
 */
public class AdminDetailsUserFragment extends Fragment {

    private static final String ERROR_TAG = "ERROR IN " + AdminDetailsUserFragment.class.getName();
    private JSONObject parameters = new JSONObject();
    private TextView documentNumber;
    private TextView documentType;
    private TextView documentExpiryDate;
    private String data;
    private DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private Activity context;
    /**
     * Empty constructor
     */
    public AdminDetailsUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = Objects.requireNonNull(getActivity());

        View view = inflater.inflate(R.layout.activity_admin_details_user_fragment, container, false);
        TextView name = view.findViewById(R.id.name_users_admin);
        TextView surname = view.findViewById(R.id.surname_user_admin);
        TextView email = view.findViewById(R.id.email_user_admin);
        TextView cellphone = view.findViewById(R.id.cellphone_user_admin);
        TextView birthDate = view.findViewById(R.id.bday_user_admin);
        documentNumber = view.findViewById(R.id.nrDoc_user_admin);
        documentType = view.findViewById(R.id.typeDoc_user_admin);
        documentExpiryDate = view.findViewById(R.id.dateEx_user_admin);
        TextView sex = view.findViewById(R.id.sex_user_admin);
        Button removeUser = view.findViewById(R.id.remove_user_admin);
        Bundle bundle = getArguments();

        User user = null;
        try {
            user = new User(new JSONObject((String) Objects.requireNonNull(Objects.requireNonNull(bundle).get("user"))));
        } catch (JSONException e) {
            Log.e(ERROR_TAG,e.getMessage());
        }

        assert user != null;
        try {
            parameters.put("username",user.getUsername());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        data = "Name: " + user.getName();
        name.setText(data);
        data = "Surname: " + user.getSurname();
        surname.setText(data);
        data = "Email: " + user.getEmail();
        email.setText(data);
        data = "Cellphone: " + user.getCellphone();
        cellphone.setText(data);
        data = "Birth Date: " + outputFormat.format(user.getBirthDate());
        birthDate.setText(data);
        data = "Sex: " + user.getSex().toString();
        sex.setText(data);

       requestUserDocument(parameters);
        removeUser.setOnClickListener(v -> deleteAccount(parameters));

        return view;
    }

    private void requestUserDocument(JSONObject parameters) {
            SendInPostConnector userDocumentConnector = new SendInPostConnector(ConnectorConstants.REQUEST_DOCUMENT, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    //Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    if (jsonArray.length()>0){
                    JSONObject dataDocument = jsonArray.getJSONObject(0);
                    data = "Document Number: " + dataDocument.getString("document_number");
                    documentNumber.setText(data);
                    data = "Document Type: " + dataDocument.getString("document_type");
                    documentType.setText(data);

                    try {

                        Date docExpiryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dataDocument.getString("expiry_date"));
                        data = "Expiry Date: " + outputFormat.format(docExpiryDate);
                        documentExpiryDate.setText(data);

                    } catch (ParseException e) {
                        Log.e(ERROR_TAG, e.toString());
                    }}
                    else{
                        documentNumber.setVisibility(View.GONE);
                        documentType.setVisibility(View.GONE);
                        documentExpiryDate.setVisibility(View.GONE);
                        Toast.makeText(context , AdminDetailsUserFragment.this.getString(R.string.doc_not_found), Toast.LENGTH_SHORT).show();

                    }
                }
            });
            userDocumentConnector.setObjectToSend(parameters);
            userDocumentConnector.execute();
    }

    private void deleteAccount(JSONObject parameters) {
        DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
            SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
                @Override
                public void onStartConnection() {
                    // Do nothing
                }

                @Override
                public void onEndConnection(JSONArray jsonArray) throws JSONException {
                    JSONObject result = jsonArray.getJSONObject(0);
                    if(!result.getBoolean("result")){
                        Log.e("DELETE_USER_ERROR", result.getString("error"));
                    }
                }
            });
            connector.setObjectToSend(parameters);
            connector.execute();
            startActivity(new Intent(context, AdminMainActivity.class));
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

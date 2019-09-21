package com.fsc.cicerone;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Document;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * Class that contains the details of the user to whom the administrator is interested.
 */
public class AdminDetailsUserFragment extends Fragment {

    private static final String ERROR_TAG = "ERROR IN " + AdminDetailsUserFragment.class.getName();
    private TextView documentNumber;
    private TextView documentType;
    private TextView documentExpiryDate;
    private TextView documentNotFound;
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
        TextView avgEarn = view.findViewById(R.id.avg_earn);
        TextView sex = view.findViewById(R.id.sex_user_admin);
        Button removeUser = view.findViewById(R.id.remove_user_admin);
        documentNotFound = view.findViewById(R.id.document_not_exists);
        Bundle bundle = getArguments();

        User user = null;
        try {
            user = new User(new JSONObject((String) Objects.requireNonNull(Objects.requireNonNull(bundle).get("user"))));
            if (user.getUserType() == UserType.CICERONE) {
                avgEarn.setVisibility(View.VISIBLE);
                AccountManager.userAvgEarnings(context, user.getUsername(), avgEarn);
            }
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }


        assert user != null;
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("username", user.getUsername());
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

    private void requestUserDocument(Map<String, Object> parameters) {
        SendInPostConnector<Document> userDocumentConnector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_DOCUMENT, BusinessEntityBuilder.getFactory(Document.class))
                .setContext(context)
                .setOnStartConnectionListener(()->{
                    documentNumber.setVisibility(View.GONE);
                    documentType.setVisibility(View.GONE);
                    documentNotFound.setVisibility(View.GONE);
                    documentExpiryDate.setVisibility(View.GONE);
                })
                .setOnEndConnectionListener(list -> {
                    if (!list.isEmpty()) {
                        Document dataDocument = list.get(0);

                        data = "Document Number: " + dataDocument.getNumber();
                        documentNumber.setText(data);
                        documentNumber.setVisibility(View.VISIBLE);

                        data = "Document Type: " + dataDocument.getType();
                        documentType.setText(data);
                        documentType.setVisibility(View.VISIBLE);

                        data = "Expiry Date: " + outputFormat.format(dataDocument.getExpirationDate());
                        documentExpiryDate.setText(data);
                        documentExpiryDate.setVisibility(View.VISIBLE);
                    } else {
                        documentNotFound.setVisibility(View.VISIBLE);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        userDocumentConnector.execute();
    }

    private void deleteAccount(Map<String, Object> parameters) {
        DialogInterface.OnClickListener positiveClickListener = (dialog, which) -> {
            BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.DELETE_REGISTERED_USER)
                    .setContext(context)
                    .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                        if (!result.getResult()) {
                            Log.e("DELETE_USER_ERROR", result.getMessage());
                        }
                    })
                    .setObjectToSend(parameters)
                    .build();
            connector.execute();
        };
        new MaterialAlertDialogBuilder(context)
                .setTitle(context.getString(R.string.are_you_sure))
                .setMessage(context.getString(R.string.sure_to_delete_account))
                .setPositiveButton(context.getString(R.string.yes), positiveClickListener)
                .setNegativeButton(context.getString(R.string.no), null)
                .show();
    }


}

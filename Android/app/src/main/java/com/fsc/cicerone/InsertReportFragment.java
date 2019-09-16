package com.fsc.cicerone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.GetDataConnector;


public class InsertReportFragment extends Fragment {

    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    public InsertReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_report, container, false);

        EditText object = view.findViewById(R.id.object);
        EditText body = view.findViewById(R.id.body);
        Spinner users = view.findViewById(R.id.users);
        Button sendReport = view.findViewById(R.id.sendReport);


        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        JSONObject param = new JSONObject();
        try {
            param.put("username", currentLoggedUser.getUsername());
            setUsersInSpinner(users, currentLoggedUser.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendReport.setOnClickListener(v -> {
            if (object.getText().toString().equals("") || body.getText().toString().equals("")) {
                Toast.makeText(InsertReportFragment.this.getActivity(), InsertReportFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

            } else {
                try {
                    param.put("reported_user", users.getSelectedItem().toString());
                    param.put("report_body", body.getText().toString());
                    param.put("state", "1");
                    param.put("object", object.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                InsertReportFragment.this.sendToTableReport(param);
                InsertReportFragment.this.sendToTableReportDetails(param);

                fragment = new ReportFragment();
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    public void setUsersInSpinner(Spinner users, String currentLoggedUser) {
        GetDataConnector<User> connector = new GetDataConnector<>(
                ConnectorConstants.REGISTERED_USER,
                BusinessEntityBuilder.getFactory(User.class),
                new DatabaseConnector.CallbackInterface<User>() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(List<User> list) {
                        List<String> cleanList = new ArrayList<>();

                        for (User user : list) {
                            if (!user.getUsername().equals(currentLoggedUser))
                                cleanList.add(user.getUsername());
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                                android.R.layout.simple_spinner_item, cleanList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        users.setAdapter(dataAdapter);

                    }
                });
        connector.execute();
    }

    public void sendToTableReport(JSONObject param) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_REPORT,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if (result.getResult()) {
                            Toast.makeText(getActivity(), InsertReportFragment.this.getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                param);
        connector.execute();
    }

    public void sendToTableReportDetails(JSONObject param) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_REPORT_DETAILS,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        Log.d("sendToTableReportDetail", String.valueOf(result.getResult()));
                    }
                },
                param);
        connector.execute();
    }


}

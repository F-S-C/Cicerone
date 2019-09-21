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
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.GetDataConnector;
import app_connector.SendInPostConnector;


public class InsertReportFragment extends Fragment {

    Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

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

        Map<String, Object> param = new HashMap<>();
        param.put("username", currentLoggedUser.getUsername());
        setUsersInSpinner(users, currentLoggedUser.getUsername());

        sendReport.setOnClickListener(v -> {
            if (object.getText().toString().equals("") || body.getText().toString().equals("")) {
                Toast.makeText(getActivity(), InsertReportFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
            } else {
                param.put("reported_user", users.getSelectedItem().toString());
                param.put("report_body", body.getText().toString());
                param.put("state", "1");
                param.put("object", object.getText().toString());

                sendToTableReport(param);

            }
        });

        return view;
    }


    public void setUsersInSpinner(Spinner users, String currentLoggedUser) {
        GetDataConnector<User> connector = new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(getActivity())
                .setOnEndConnectionListener(list -> {
                    List<String> cleanList = new ArrayList<>();

                    for (User user : list) {
                        if (!user.getUsername().equals(currentLoggedUser))
                            cleanList.add(user.getUsername());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                            android.R.layout.simple_spinner_item, cleanList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    users.setAdapter(dataAdapter);

                })
                .build();
        connector.execute();
    }

    public void sendToTableReport(Map<String, Object> param) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_REPORT)
                .setContext(getActivity())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if(result.getResult())
                    {
                        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_REPORT, BusinessEntityBuilder.getFactory(Report.class))
                                .setContext(getActivity())
                                .setOnEndConnectionListener(list -> {
                                    param.put("report_code", list.get(list.size() -1 ).getCode());
                                    Log.e("size", String.valueOf(list.size()));
                                    sendToTableReportDetails(param);
                                })
                                .setObjectToSend(param)
                                .build()
                                .execute();
                    }

                })
                .setObjectToSend(param)
                .build();
        connector.execute();
    }

    public void sendToTableReportDetails(Map<String, Object> param) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_REPORT_DETAILS)
                .setContext(getActivity())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Toast.makeText(getActivity() , getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
                        fragment = new ReportFragment();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                    }
                })
                .setObjectToSend(param)
                .build();
        connector.execute();
    }


}

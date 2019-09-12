package com.fsc.cicerone;

import android.os.Bundle;
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

import com.fsc.cicerone.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.GetDataConnector;
import app_connector.SendInPostConnector;


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
            param.put("username",currentLoggedUser.getUsername());
            setUsersInSpinner(users,currentLoggedUser.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        return view;
    }



    public void setUsersInSpinner(Spinner users, String currentLoggedUser) {
        GetDataConnector connector = new GetDataConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {

            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                List<String> list = new ArrayList<>();

                for(int i=0; i < jsonArray.length(); i++)
                {
                    if(
                            !jsonArray.getJSONObject(i).getString("username").equals("admin") &&
                            !jsonArray.getJSONObject(i).getString("username").equals("deleted_user") &&
                            !jsonArray.getJSONObject(i).getString("username").equals(currentLoggedUser))

                        list.add(jsonArray.getJSONObject(i).getString("username"));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                users.setAdapter(dataAdapter);

            }
        });
        connector.execute();
    }

    public void sendToTableReport(JSONObject param)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_REPORT, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                if (object.getBoolean("result")) {
                    Toast.makeText(getActivity(), InsertReportFragment.this.getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
                }

            }
        });
        connector.setObjectToSend(param);
        connector.execute();
    }

    public void sendToTableReportDetails(JSONObject param) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_REPORT_DETAILS, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {

            }
        });
        connector.setObjectToSend(param);
        connector.execute();
    }

    

}

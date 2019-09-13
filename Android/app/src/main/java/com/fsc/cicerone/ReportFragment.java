package com.fsc.cicerone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReportAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Report on the account details page.
 */
public class ReportFragment extends Fragment {

    RecyclerView.Adapter adapter;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();

    /**
     * Empty Constructor
     */
    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_report_fragment, container, false);

        Button insertReport = view.findViewById(R.id.newReport);


        SharedPreferences preferences = Objects.requireNonNull(this.getActivity()).getSharedPreferences("com.fsc.cicerone", Context.MODE_PRIVATE);

        try {
            final JSONObject parameters = new JSONObject(preferences.getString("session", "")); //Connection params
            parameters.remove("password");
            // set up the RecyclerView
            if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                parameters.remove("username");
            }
            RecyclerView recyclerView = view.findViewById(R.id.report_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            requireData(view, parameters, recyclerView);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }

        insertReport.setOnClickListener(v -> {
            fragment = new InsertReportFragment();
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        });

        return view;
    }


    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector<Report> connector = new SendInPostConnector<>(
                ConnectorConstants.REPORT_FRAGMENT,
                BusinessEntityBuilder.getFactory(Report.class),
                new DatabaseConnector.CallbackInterface<Report>() {
                    @Override
                    public void onStartConnection() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEndConnection(List<Report> list) {
                        progressBar.setVisibility(View.GONE);
                        adapter = new ReportAdapter(getActivity(), list);
                        recyclerView.setAdapter(adapter);
                    }
                },
                parameters);
        connector.execute();
    }


}
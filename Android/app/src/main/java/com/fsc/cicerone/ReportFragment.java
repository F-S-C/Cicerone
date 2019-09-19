package com.fsc.cicerone;

import android.os.Bundle;
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
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.UserType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

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


        final Map<String, Object> parameters = new HashMap<>(1); //Connection params
        parameters.put("username", AccountManager.getCurrentLoggedUser().getUsername());
        // set up the RecyclerView
        if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
            parameters.remove("username");
        }
        RecyclerView recyclerView = view.findViewById(R.id.report_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        insertReport.setOnClickListener(v -> {
            fragment = new InsertReportFragment();
            fragmentManager = getFragmentManager();
            fragmentTransaction = Objects.requireNonNull(fragmentManager).beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        });

        return view;
    }


    private void requireData(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector<Report> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REPORT_FRAGMENT, BusinessEntityBuilder.getFactory(Report.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> progressBar.setVisibility(View.VISIBLE))
                .setOnEndConnectionListener(list -> {
                    progressBar.setVisibility(View.GONE);
                    adapter = new ReportAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }


}
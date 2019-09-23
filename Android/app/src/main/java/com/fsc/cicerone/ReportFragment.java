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

package com.fsc.cicerone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.ReportAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.UserType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Report on the account details page.
 */
public class ReportFragment extends Fragment implements Refreshable {

    RecyclerView.Adapter adapter;
    Fragment fragment = null;
    private RecyclerView recyclerView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static final int RESULT_SHOULD_REPORT_BE_RELOADED = 1030;

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

        recyclerView = view.findViewById(R.id.report_list);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        refresh();

        insertReport.setVisibility(AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN ? View.GONE : View.VISIBLE);

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


    @Override
    public void refresh() {
        refresh(null);
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        final Map<String, Object> parameters = new HashMap<>(1); //Connection params
        parameters.put("username", AccountManager.getCurrentLoggedUser().getUsername());
        // set up the RecyclerView
        if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
            parameters.remove("username");
        }

        Log.e("TAG", parameters.toString());

        new SendInPostConnector.Builder<>(ConnectorConstants.REPORT_FRAGMENT, BusinessEntityBuilder.getFactory(Report.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
                })
                .setOnEndConnectionListener(list -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    adapter = new ReportAdapter(getActivity(), list, this);
                    recyclerView.setAdapter(adapter);
                })
                .setObjectToSend(parameters)
                .build()
                .execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReportFragment.RESULT_SHOULD_REPORT_BE_RELOADED && resultCode == Activity.RESULT_OK) {
                refresh();
        }
    }
}
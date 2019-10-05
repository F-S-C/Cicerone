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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.ReportAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReportManager;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.ReportStatus;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains the elements of the TAB Report on the account details page.
 */
public class ReportFragment extends Fragment implements Refreshable {

    RecyclerView.Adapter adapter;
    Fragment fragment = null;
    private RecyclerView recyclerView;
    public static final int RESULT_SHOULD_REPORT_BE_RELOADED = 1030;
    private SwipeRefreshLayout swipeRefreshLayout = null;


    /**
     * Empty Constructor
     */
    public ReportFragment() {
        // Required empty public constructor
    }

    public ReportFragment(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_report_fragment, container, false);

        recyclerView = view.findViewById(R.id.report_list);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        refresh(swipeRefreshLayout);

        return view;
    }

    @Override
    public void refresh() {
        refresh(null);
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        User user = null;
        if (AccountManager.getCurrentLoggedUser().getUserType() != UserType.ADMIN)
            user = AccountManager.getCurrentLoggedUser();


        ReportManager.requestReport(getActivity(), user, () -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        }, list -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                List<Report> filtered = new ArrayList<>(list.size());
                for (Report report : list) {
                    if (!(report.getStatus() == ReportStatus.CLOSED || report.getStatus() == ReportStatus.CANCELED))
                        filtered.add(report);
                }
                adapter = new ReportAdapter(getActivity(), filtered, this);
            } else
                adapter = new ReportAdapter(getActivity(), list, this);

            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReportFragment.RESULT_SHOULD_REPORT_BE_RELOADED && resultCode == Activity.RESULT_OK) {
            refresh();
        }
    }


}
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.ReportAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReportManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.GetDataConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Report on the account details page.
 */
public class ReportFragment extends Fragment implements Refreshable {

    RecyclerView.Adapter adapter;
    Fragment fragment = null;
    private RecyclerView recyclerView;
    public static final int RESULT_SHOULD_REPORT_BE_RELOADED = 1030;
    private EditText object;
    private EditText body;
    private Spinner users;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    /**
     * Empty Constructor
     */
    public ReportFragment() {
        // Required empty public constructor
    }

    public ReportFragment(SwipeRefreshLayout swipeRefreshLayout){
        this.swipeRefreshLayout = swipeRefreshLayout;
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
        refresh(swipeRefreshLayout);

        insertReport.setVisibility(AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN ? View.GONE : View.VISIBLE);

        insertReport.setOnClickListener(v -> insertReport());

        return view;
    }

    private void insertReport() {
        View reportView = getLayoutInflater().inflate(R.layout.dialog_new_report, null);

         object = reportView.findViewById(R.id.object);
         body = reportView.findViewById(R.id.body);
         users = reportView.findViewById(R.id.users);

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        setUsersInSpinner(users, currentLoggedUser.getUsername());

        AlertDialog dialogSubmit = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setView(reportView)
                .setTitle(getString(R.string.insert_report))
                .setMessage(getString(R.string.report_dialog_message))
                .setPositiveButton(R.string.insert_report, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialogSubmit.setOnShowListener(dialogInterface -> {

            Button button = dialogSubmit.getButton(AlertDialog.BUTTON_POSITIVE);
            /*button.setOnClickListener(view -> new MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.sure_to_insert_report))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        if (allFilled()) {
                            ReportManager.addNewReport(context, currentLoggedUser, users.getSelectedItem().toString(), object.getText().toString(), body.getText().toString());
                            refresh();
                            dialogSubmit.dismiss();
                        }else
                            Toast.makeText(context, context.getString(R.string.error_fields_empty),
                                    Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show());*/
            button.setOnClickListener(view ->{
                if(allFilled()){
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(R.string.insert_report)
                            .setMessage(R.string.sure_to_insert_report)
                            .setPositiveButton(R.string.yes, (dialog,witch)->{
                                ReportManager.addNewReport(getActivity(), currentLoggedUser,users.getSelectedItem().toString(),object.getText().toString(), body.getText().toString());
                                refresh();
                                dialogSubmit.dismiss();
                            })
                            .setNegativeButton(R.string.no,null)
                            .show();
                }else
                    Toast.makeText(getContext(), getContext().getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();
            });

        });

        dialogSubmit.show();
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

    private void setUsersInSpinner(Spinner users, String currentLoggedUser) {
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

    private boolean allFilled() {
        return !object.getText().toString().equals("") && !body.getText().toString().equals("") && !users.getSelectedItem().equals("");
    }
}
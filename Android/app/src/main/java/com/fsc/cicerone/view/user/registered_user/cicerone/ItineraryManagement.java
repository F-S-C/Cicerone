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

package com.fsc.cicerone.view.user.registered_user.cicerone;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.manager.ItineraryManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.ItineraryReview;
import com.fsc.cicerone.view.itinerary.ItineraryActivity;
import com.fsc.cicerone.view.user.UsersListFragment;
import com.fsc.cicerone.view.system.Refreshable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that shows the information of an itinerary on the Cicerone-side, allowing for future
 * updates.
 */
public class ItineraryManagement extends ItineraryActivity implements Refreshable {
    public static final int RESULT_ITINERARY_DELETED = 1020;
    private Fragment fragment = new UsersListFragment();
    private Map<String, Object> code;
    private TextView messageNoReview;


    /**
     * An empty Constructor.
     */
    public ItineraryManagement() {
        super();
        this.layout = R.layout.activity_itinerary_management;
    }

    /**
     * A Constructor that takes a Layout as a parameter.
     *
     * @param contentLayoutId The Layout to set.
     */
    public ItineraryManagement(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_itinerary_management;
    }

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button deleteItinerary = findViewById(R.id.deleteItinerary);
        FloatingActionButton updateItinerary = findViewById(R.id.editItinerary);

        RecyclerView recyclerView = findViewById(R.id.reviewList);
        messageNoReview = findViewById(R.id.messageNoReview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        code = new HashMap<>();
        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);

        //Extract the data…
        code.put("reviewed_itinerary", itinerary.getCode());
        requestDataForRecycleView(code, recyclerView);
        code.put("itinerary_code", itinerary.getCode());

        deleteItinerary.setOnClickListener(v -> new MaterialAlertDialogBuilder(ItineraryManagement.this).
                setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.confirm_delete))
                .setPositiveButton(getString(R.string.yes), ((dialog, which) -> deleteItineraryFromServer()))
                .setNegativeButton(getString(R.string.no), null)
                .show());


        updateItinerary.setOnClickListener(v -> {
            Intent i = new Intent().setClass(v.getContext(), ItineraryUpdate.class);
            i.putExtras(bundle);
            startActivityForResult(i, ItineraryUpdate.RESULT_ITINERARY_UPDATED);
        });


    }

    /**
     * @see com.fsc.cicerone.view.itinerary.ItineraryActivity#goToAuthor(View)
     */
    @Override
    public void goToAuthor(View view) {
        // Do nothing
    }

    /**
     * A function that deletes the current Itinerary from the Server.
     */
    public void deleteItineraryFromServer() {
        ItineraryManager.deleteItinerary(this, itinerary, success -> Toast.makeText(ItineraryManagement.this, ItineraryManagement.this.getString(R.string.itinerary_deleted), Toast.LENGTH_SHORT).show());
        setResult(Activity.RESULT_OK);
        ItineraryManagement.this.finish();
    }

    /**
     * A function that allows the Cicerone to see the participators of the Itinerary.
     *
     * @param view The current View.
     */
    public void participatorsList(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ItineraryParticipantsDialogFragment editNameDialogFragment = ItineraryParticipantsDialogFragment.newInstance(itinerary);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    /**
     * A function that takes data from the Server and sets them into a given RecyclerView.
     *
     * @param parameters   The parameters of the Query.
     * @param recyclerView The RecyclerView to set.
     */
    private void requestDataForRecycleView(Map<String, Object> parameters, RecyclerView recyclerView) {
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    if (!list.isEmpty()) {
                        RecyclerView.Adapter adapter = new ReviewAdapter(this, list);
                        recyclerView.setAdapter(adapter);
                    } else
                        messageNoReview.setVisibility(View.VISIBLE);
                })
                .setObjectToSend(parameters)
                .build()
                .getData();
    }

    /**
     * @see android.app.Activity#onActivityResult(int, int, Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ItineraryUpdate.RESULT_ITINERARY_UPDATED && resultCode == Activity.RESULT_OK) {
            ItineraryManager.requestItinerary(this, code, () -> {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);

            }, list -> {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                itinerary = list.get(0);
                ItineraryManagement.this.refresh();
            });
        }
    }
}



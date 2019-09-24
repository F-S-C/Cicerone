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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.manager.ItineraryManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.ItineraryReview;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Map;

public class ItineraryManagement extends ItineraryActivity {
    private Fragment fragment = new UsersListFragment();
    private RecyclerView.Adapter adapter;


    public ItineraryManagement() {
        super();
        this.layout = R.layout.activity_itinerary_management;
    }

    public ItineraryManagement(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_itinerary_management;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button deleteItinerary = findViewById(R.id.deleteItinerary);
        FloatingActionButton updateItinerary = findViewById(R.id.editItinerary);

        RecyclerView recyclerView = findViewById(R.id.reviewList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Map<String, Object> code = new HashMap<>();
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
            v.getContext().startActivity(i);
        });


    }

    @Override
    public void goToAuthor(View view) {
        // Do nothing
    }

    public void deleteItineraryFromServer() {
        ItineraryManager.deleteItinerary(this, itinerary, success -> Toast.makeText(ItineraryManagement.this, ItineraryManagement.this.getString(R.string.itinerary_deleted), Toast.LENGTH_SHORT).show());
        ItineraryManagement.this.finish();
    }

    public void participatorsList(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ItineraryParticipantsDialogFragment editNameDialogFragment = ItineraryParticipantsDialogFragment.newInstance(itinerary);
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    private void requestDataForRecycleView(Map<String, Object> parameters, RecyclerView recyclerView) {
        SendInPostConnector<ItineraryReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    if (!list.isEmpty()) {
                        adapter = new ReviewAdapter(this, list);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ItineraryUpdate.RESULT_ITINERARY_UPDATED && resultCode == Activity.RESULT_OK) {
            finish();
        }
    }
}



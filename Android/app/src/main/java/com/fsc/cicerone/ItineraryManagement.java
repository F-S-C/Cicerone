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


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ItineraryManagement extends AppCompatActivity {
    private ImageView image;
    private TextView description;
    private TextView bDate;
    private TextView eDate;
    private TextView rDate;
    private TextView location;
    private RatingBar review;
    private TextView author;
    private TextView minP;
    private TextView maxP;
    private TextView repetitions;
    private TextView duration;
    private TextView fPrice;
    private TextView rPrice;
    private ActionBar supportActionBar;
    private Fragment fragment = new UsersListFragment();
    private RecyclerView.Adapter adapter;

    private Itinerary itinerary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itinerary_management);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        bDate = findViewById(R.id.beginningDate);
        eDate = findViewById(R.id.endingDate);
        rDate = findViewById(R.id.reservationDate);
        review = findViewById(R.id.itineraryReview);
        author = findViewById(R.id.author);
        minP = findViewById(R.id.minP);
        maxP = findViewById(R.id.maxP);
        location = findViewById(R.id.location);
        repetitions = findViewById(R.id.repetitions);
        duration = findViewById(R.id.duration);
        fPrice = findViewById(R.id.fPrice);
        rPrice = findViewById(R.id.rPrice);
        Button deleteItinerary = findViewById(R.id.deleteItinerary);
        FloatingActionButton updateItinerary = findViewById(R.id.editItinerary);

        RecyclerView recyclerView = findViewById(R.id.reviewList);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        final Map<String, Object> code = new HashMap<>();
        try {
            //Get the bundle
            Bundle bundle = getIntent().getExtras();
            fragment.setArguments(bundle);
            String s = Objects.requireNonNull(bundle).getString("itinerary");

            //Extract the data…
            itinerary = new Itinerary(new JSONObject(s));

            code.put("reviewed_itinerary", itinerary.getCode());
            requestDataForRecycleView(code, recyclerView);
            code.put("itinerary_code", itinerary.getCode());
            getDataFromServer(itinerary);
            getItineraryReviews(code);

            ImageView imageView = findViewById(R.id.imageView2);
            imageView.setImageResource(itinerary.getCicerone().getSex().getAvatarResource());

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

        } catch (JSONException e) {
            Log.e("error", e.toString());
        }


    }

    public void getDataFromServer(Itinerary itinerary) {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        supportActionBar.setTitle(itinerary.getTitle());
        description.setText(itinerary.getDescription());
        Picasso.get().load(itinerary.getImageUrl()).into(image);
        author.setText(itinerary.getCicerone().getUsername());
        String dur = itinerary.getDuration();
        duration.setText(dur.substring(0, 5));
        location.setText(itinerary.getLocation());
        repetitions.setText(String.valueOf(itinerary.getRepetitions()));
        fPrice.setText(Float.toString(itinerary.getFullPrice())); //TODO: Add price tag (branch ui-improvement)
        rPrice.setText(Float.toString(itinerary.getReducedPrice())); //TODO: Add price tag (branch ui-improvement)
        minP.setText(String.valueOf(itinerary.getMinParticipants()));
        maxP.setText(String.valueOf(itinerary.getMaxParticipants()));
        bDate.setText(out.format(itinerary.getBeginningDate()));
        eDate.setText(out.format(itinerary.getEndingDate()));
        rDate.setText(out.format(itinerary.getReservationDate()));
    }

    public void getItineraryReviews(Map<String, Object> itineraryCode) {
        SendInPostConnector<ItineraryReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    if (!list.isEmpty()) {
                        int sum = 0;
                        for (ItineraryReview itineraryReview : list) {
                            sum += itineraryReview.getFeedback();
                        }
                        float total = (float) sum / list.size();
                        review.setRating(total);
                    } else {
                        review.setRating(0);
                    }
                })
                .setObjectToSend(itineraryCode)
                .build();
        connector.execute();
    }

    public void deleteItineraryFromServer() {
        ItineraryManager.deleteItinerary(this, itinerary, success -> Toast.makeText(ItineraryManagement.this, ItineraryManagement.this.getString(R.string.itinerary_deleted), Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
}



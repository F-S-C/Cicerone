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

package com.fsc.cicerone.view.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.ReviewManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Language;
import com.fsc.cicerone.view.system.Refreshable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * A function that manage to display all the information of an Itinerary, based on which Activity is
 * started. It is called by ItineraryDetails and ItineraryManagement.
 */
public abstract class ItineraryActivity extends AppCompatActivity implements Refreshable {
    protected Itinerary itinerary;
    protected int layout = R.layout.activity_itinerary_details;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected CollapsingToolbarLayout collapsingToolbarLayout;

    /**
     * Empty Constructor
     */
    public ItineraryActivity() {
        super();
    }

    /**
     * A Constructor that takes a layout as a parameter.
     *
     * @param contentLayoutId The layout to set.
     */
    public ItineraryActivity(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        Bundle bundle = getIntent().getExtras();
        String s = Objects.requireNonNull(bundle).getString("itinerary");
        itinerary = new Itinerary(s);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        setSupportActionBar(findViewById(R.id.toolbar));

        final ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);


        swipeRefreshLayout = findViewById(R.id.itinerary_activity_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);

        refresh();
    }

    /**
     * A function that sets values of the itinerary in its relative space.
     */
    public void bindDataToView() {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        ImageView image = findViewById(R.id.image);
        TextView description = findViewById(R.id.description);
        TextView bDate = findViewById(R.id.beginningDate);
        TextView eDate = findViewById(R.id.endingDate);
        TextView rDate = findViewById(R.id.reservationDate);
        RatingBar review = findViewById(R.id.itineraryReview);
        TextView author = findViewById(R.id.author);
        TextView minP = findViewById(R.id.minP);
        TextView maxP = findViewById(R.id.maxP);
        TextView location = findViewById(R.id.location);
        TextView repetitions = findViewById(R.id.repetitions);
        TextView duration = findViewById(R.id.duration);
        TextView fPrice = findViewById(R.id.fPrice);
        TextView rPrice = findViewById(R.id.rPrice);
        ImageView imageView = findViewById(R.id.imageView2);

        description.setText(itinerary.getDescription());
        Picasso.get().load(itinerary.getImageUrl()).into(image);
        author.setText(itinerary.getCicerone().getUsername());
        String dur = itinerary.getDuration();
        duration.setText(dur.substring(0, 5));
        location.setText(itinerary.getLocation());
        repetitions.setText(String.valueOf(itinerary.getRepetitions()));
        fPrice.setText(String.format(getString(R.string.price_value), itinerary.getFullPrice()));
        rPrice.setText(String.format(getString(R.string.price_value), itinerary.getReducedPrice()));
        minP.setText(String.valueOf(itinerary.getMinParticipants()));
        maxP.setText(String.valueOf(itinerary.getMaxParticipants()));
        bDate.setText(out.format(itinerary.getBeginningDate()));
        eDate.setText(out.format(itinerary.getEndingDate()));
        rDate.setText(out.format(itinerary.getReservationDate()));
        imageView.setImageResource(itinerary.getCicerone().getSex().getAvatarResource());

        TextView languagesTextView = findViewById(R.id.languagesTextView);

        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        if (languagesTextView.getText().toString().equals("")) {
            for (Language language : itinerary.getLanguages()) {
                sb.append(delimiter).append(language.getName());
                delimiter = ", ";
            }
            languagesTextView.setText(sb.toString());

        }


        ReviewManager.getAvgItineraryFeedback(ItineraryActivity.this, itinerary, review::setRating);

        collapsingToolbarLayout.setTitle(itinerary.getTitle());


        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh()
     */
    @Override
    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        bindDataToView();
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh(SwipeRefreshLayout)
     */
    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        refresh();
    }

    /**
     * @see AppCompatActivity#onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * A function that allows the User to visit the profile of the author of the Itinerary.
     *
     * @param view The current View.
     */
    public abstract void goToAuthor(View view);


    /**
     * A function that starts an Activity with data that needs to be passed.
     *
     * @param targetActivity The Activity to start .
     * @param bundle         The data to pass.
     */
    protected void startActivityWithData(Class targetActivity, Bundle bundle) {
        Intent i = new Intent().setClass(this, targetActivity);
        i.putExtras(bundle);
        startActivity(i);
    }
}

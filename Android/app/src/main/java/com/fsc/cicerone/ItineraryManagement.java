package com.fsc.cicerone;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

public class ItineraryManagement extends AppCompatActivity {
    private TextView itineraryTitle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_management);
        itineraryTitle = findViewById(R.id.title);
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
        Button updateItinerary = findViewById(R.id.editItinerary);

        final Itinerary itinerary;
        final Map<String, Object> code = new HashMap<>();
        try {
            //Get the bundle
            Bundle bundle = getIntent().getExtras();
            String s = Objects.requireNonNull(bundle).getString("itinerary");

            //Extract the data…
            itinerary = new Itinerary(new JSONObject(s));

            code.put("reviewed_itinerary", String.valueOf(itinerary.getCode()));
            code.put("itinerary_code", String.valueOf(itinerary.getCode()));
            getDataFromServer(itinerary);
            getItineraryReviews(code);
            deleteItinerary.setOnClickListener(v -> new MaterialAlertDialogBuilder(ItineraryManagement.this).
                    setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.confirm_delete))
                    .setPositiveButton(getString(R.string.yes), ((dialog, which) -> deleteItineraryFromServer(code)))
                    .setNegativeButton(getString(R.string.no), null)
                    .show());


            updateItinerary.setOnClickListener(v -> {
                Intent i = new Intent().setClass(v.getContext(), ItineraryUpdate.class);
                i.putExtras(bundle);
                v.getContext().startActivity(i);
            });

            review.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent i = new Intent().setClass(ItineraryManagement.this, ItineraryReviewFragment.class);
                    i.putExtra("itinerary", itinerary.toJSONObject().toString());
                    i.putExtra("rating", review.getRating());
                    i.putExtra("reviewed_itinerary", itinerary.getCode());
                    startActivity(i);
                }
                return true;
            });

        } catch (JSONException e) {
            Log.e("error", e.toString());
        }


    }

    public void getDataFromServer(Itinerary itinerary) {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        itineraryTitle.setText(itinerary.getTitle());
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

    public void deleteItineraryFromServer(Map<String, Object> itCode) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.DELETE_ITINERARY)
                .setContext(this)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Intent i = new Intent(ItineraryManagement.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(ItineraryManagement.this, getString(R.string.itinerary_deleted), Toast.LENGTH_LONG).show();
                        startActivity(i);
                    }
                })
                .setObjectToSend(itCode)
                .build();
        connector.execute();
    }
}



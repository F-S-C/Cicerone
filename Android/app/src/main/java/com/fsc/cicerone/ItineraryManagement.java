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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
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

    private static final String ERROR_TAG = "ERROR IN " + ItineraryManagement.class.getName();

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
        final JSONObject code = new JSONObject();
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
        author.setText(itinerary.getUsername());
        String dur = itinerary.getDuration();
        duration.setText(dur.substring(0, 5));
        location.setText(itinerary.getLocation());
        repetitions.setText(String.valueOf(itinerary.getRepetitions()));
        fPrice.setText(Float.toString(itinerary.getFullPrice()));
        rPrice.setText(Float.toString(itinerary.getReducedPrice()));
        minP.setText(String.valueOf(itinerary.getMinParticipants()));
        maxP.setText(String.valueOf(itinerary.getMaxParticipants()));
        bDate.setText(out.format(itinerary.getBeginningDate()));
        eDate.setText(out.format(itinerary.getEndingDate()));
        rDate.setText(out.format(itinerary.getReservationDate()));
    }

    public void getItineraryReviews(JSONObject itineraryCode) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.ITINERARY_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                if (jsonArray.length() > 0) {
                    int n = 0;
                    float sum = 0;
                    do {
                        sum += jsonArray.getJSONObject(n).getInt("feedback");
                        n = n + 1;
                    }
                    while (n < jsonArray.length());
                    float total = sum / n;
                    review.setRating(total);
                } else {
                    review.setRating(0);
                }
            }
        });
        connector.setObjectToSend(itineraryCode);
        connector.execute();
    }

    public void deleteItineraryFromServer(JSONObject itCode) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                if (object.getBoolean("result")) {
                    Intent i = new Intent(ItineraryManagement.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(ItineraryManagement.this, getString(R.string.itinerary_deleted), Toast.LENGTH_LONG).show();
                    startActivity(i);
                }
            }
        });
        connector.setObjectToSend(itCode);
        connector.execute();
    }
}



package com.fsc.cicerone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class ItineraryDetails extends AppCompatActivity {
    private Button requestReservation;
    private Button intoWishlist;
    private Button removeFromWishlist;
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
    private  TextView duration;
    private  TextView fPrice;
    private  TextView rPrice;
    private  JSONObject result;

    private static final String ERROR_TAG = "ERROR IN " + ItineraryDetails.class.getName();
    private static final String IT_CODE = "itinerary_code";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_details);
         requestReservation = findViewById(R.id.requestReservation);
         intoWishlist = findViewById(R.id.intoWishlist);
         removeFromWishlist = findViewById(R.id.removeFromWishlist);
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
        final Dialog deleteDialog = new Dialog(ItineraryDetails.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(deleteDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        deleteDialog.setContentView(R.layout.activity_delete_itinerary);
        deleteDialog.setCancelable(true);
        JSONObject object = new JSONObject();
        JSONObject object2 = new JSONObject();
        JSONObject objectReview = new JSONObject();
        Bundle bundle = getIntent().getExtras();
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        final Itinerary itinerary;


        try {
            String s = Objects.requireNonNull(bundle).getString("itinerary");
            itinerary = new Itinerary(new JSONObject(s));

            object.put("itinerary_in_wishlist", itinerary.getCode());
            object.put("username", currentLoggedUser.getUsername());
            objectReview.put("reviewed_itinerary",itinerary.getCode());
            checkWishlist(object);
            getDataFromServer(itinerary);
            getItineraryReviews(objectReview);
            object2.put("itinerary_code", object.getString("itinerary_in_wishlist"));
            object2.put("username", currentLoggedUser.getUsername());
            object2.put("booked_itinerary", itinerary.getCode());
            isReservated(object2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        removeFromWishlist.setOnClickListener(v -> {
            Button noButton = deleteDialog.findViewById(R.id.no_logout_button);
            noButton.setOnClickListener(view -> deleteDialog.hide());

            Button yesButton = deleteDialog.findViewById(R.id.yes_logout_button);
            yesButton.setOnClickListener(view -> {
                deleteDialog.hide();
                deleteDialog.dismiss();
                deleteFromWishlist(object2);


            });
            deleteDialog.show();
        });



        intoWishlist.setOnClickListener(v -> addToWishlist(object));
    }

    public void addToWishlist ( JSONObject params)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_WISHLIST, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                Log.e("p", object.toString());
                if (object.getBoolean("result")) {
                    Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.itinerary_added), Toast.LENGTH_SHORT).show();
                    checkWishlist(params);
                }
            }
        });
        connector.setObjectToSend(params);
        connector.execute();
    }

    public void deleteFromWishlist (JSONObject params)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_WISHLIST, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                if (object.getBoolean("result")) {
                    Intent i = new Intent(ItineraryDetails.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.itinerary_deleted), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            }
        });
        connector.setObjectToSend(params);
        connector.execute();
    }


    public void checkWishlist(JSONObject object) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.SEARCH_WISHLIST, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {

                if ( jsonArray.length() > 0)
                {
                    intoWishlist.setVisibility(View.GONE);
                    removeFromWishlist.setVisibility(View.VISIBLE);
                }


                else
                {
                    intoWishlist.setVisibility(View.VISIBLE);
                    removeFromWishlist.setVisibility(View.GONE);
                }
            }
        });
        connector.setObjectToSend(object);
        connector.execute();

    }


    public void getDataFromServer(Itinerary itinerary)
    {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        itineraryTitle.setText(itinerary.getTitle());
        description.setText(itinerary.getDescription());
        Picasso.get().load(itinerary.getImageUrl()).into(image);
        author.setText(itinerary.getUsername());
        String dur = itinerary.getDuration();
        duration.setText(dur.substring(0,5));
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

    public void getItineraryReviews(JSONObject itineraryCode)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.ITINERARY_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                if(jsonArray.length() > 0) {
                    int n=0;
                    float sum = 0;
                    do {
                        sum+= jsonArray.getJSONObject(n).getInt("feedback");
                        n = n+1;
                    }
                    while(n < jsonArray.length());
                    float total = sum/n;
                    review.setRating(total);
                }else{
                    review.setRating(0);
                }
            }
        });
        connector.setObjectToSend(itineraryCode);
        connector.execute();
    }

    public void isReservated(JSONObject reservation)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_RESERVATION, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                if(jsonArray.length() > 0) {
                    requestReservation.setVisibility(View.GONE);
                }
            }
        });
        connector.setObjectToSend(reservation);
        connector.execute();
    }



    public void reservation(View view)
    {
        Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.loading), Toast.LENGTH_SHORT).show();
    }

    public void goToAuthor(View view) {
        Intent i = new Intent().setClass(view.getContext(),ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("reviewed_user",author.getText().toString());
        i.putExtras(bundle);
        view.getContext().startActivity(i);
    }

}


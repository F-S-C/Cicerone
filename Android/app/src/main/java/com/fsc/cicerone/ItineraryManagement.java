package com.fsc.cicerone;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private  TextView duration;
    private  TextView fPrice;
    private  TextView rPrice;
    private JSONObject result;

    private static final String ERROR_TAG = "ERROR IN " + ItineraryManagement.class.getName();
    private static final String IT_CODE = "itinerary_code";

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

         final Dialog deleteDialog = new Dialog(ItineraryManagement.this, android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(deleteDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        deleteDialog.setContentView(R.layout.activity_delete_itinerary);
        deleteDialog.setCancelable(true);

        final JSONObject code;
        try {
            //Get the bundle
            Bundle bundle = getIntent().getExtras();
            //Extract the data…
            code = new JSONObject();
            code.put(IT_CODE, Objects.requireNonNull(bundle).getString(IT_CODE));
            getDatafromServer(code);
            getItineraryReviews(code);
            deleteItinerary.setOnClickListener(v -> {
                Button noButton = deleteDialog.findViewById(R.id.no_logout_button);
                noButton.setOnClickListener(view -> deleteDialog.hide());

                Button yesButton = deleteDialog.findViewById(R.id.yes_logout_button);
                yesButton.setOnClickListener(view -> {
                    deleteDialog.hide();
                    deleteDialog.dismiss();
                    deleteItineraryfromServer(code);


                });

                deleteDialog.show();
            });
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        

    }

    public void getDatafromServer(JSONObject itineraryCode)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                result = jsonArray.getJSONObject(0);
                itineraryTitle.setText(result.getString("title"));
                description.setText(result.getString("description"));
                Picasso.get().load(result.getString("image_url")).into(image);
                author.setText(result.getString("username"));
                minP.setText(result.getString("minimum_participants_number"));
                maxP.setText(result.getString("maximum_participants_number"));
                String dur = result.getString("duration");
                duration.setText(dur.substring(0,5));
                location.setText(result.getString("location"));
                repetitions.setText(result.getString("repetitions_per_day"));
                fPrice.setText(result.getString("full_price"));
                rPrice.setText(result.getString("reduced_price"));
                try {
                    SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    Date date = in.parse(result.getString("beginning_date"));
                    bDate.setText(out.format(date));
                    date = in.parse(result.getString("ending_date"));
                    eDate.setText(out.format(date));
                    date = in.parse(result.getString("end_reservations_date"));
                    rDate.setText(out.format(date));
                }catch (ParseException e){
                    Log.e(ERROR_TAG,e.toString());
                }
            }
        });
        connector.setObjectToSend(itineraryCode);
        connector.execute();
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

    public void deleteItineraryfromServer(JSONObject itCode)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                if(object.getBoolean("result"))
                {
                    Intent i = new Intent(ItineraryManagement.this, AccountDetails.class);
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



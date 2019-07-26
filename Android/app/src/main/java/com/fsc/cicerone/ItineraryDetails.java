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

public class ItineraryDetails extends AppCompatActivity {
    private Button requestReservation;
    private Button intoWishlist;
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
        JSONObject object = new JSONObject();
        Bundle bundle = getIntent().getExtras();
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        try {
            object.put(IT_CODE,Objects.requireNonNull(bundle).getString(IT_CODE));
            getDatafromServer(object);
            getItineraryReviews(object);
            object.put("username", currentLoggedUser.getUsername());
            isReservated(object);
            checkWishlist(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setWishlistButton(View view)
    {
        Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.loading), Toast.LENGTH_SHORT).show();

    }

    public void checkWishlist(JSONObject object) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_WISHLIST, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                result = jsonArray.getJSONObject(0);
                if ( result.length() > 0)
                    intoWishlist.setVisibility(View.GONE);
            }
        });
        connector.setObjectToSend(object);
        connector.execute();

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
                String dur = result.getString("duration");
                duration.setText(dur.substring(0,5));
                location.setText(result.getString("location"));
                repetitions.setText(result.getString("repetitions_per_day"));
                fPrice.setText(result.getString("full_price"));
                rPrice.setText(result.getString("reduced_price"));
                minP.setText(result.getString("minimum_participants_number"));
                maxP.setText(result.getString("maximum_participants_number"));
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

    public void goToAuthor(View view)
    {
        Intent i = new Intent().setClass(view.getContext(),ProfileActivity.class);
        Bundle bundle = new Bundle();
        try
        {
            bundle.putString("username",result.getString("username"));
            i.putExtras(bundle);
            view.getContext().startActivity(i);
        }
        catch(JSONException e){
            Log.e(ERROR_TAG,e.toString());
        }
    }

}


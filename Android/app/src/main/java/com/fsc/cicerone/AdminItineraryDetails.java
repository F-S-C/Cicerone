package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class AdminItineraryDetails extends AppCompatActivity {

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
    private Itinerary itinerary; //TODO: Add to class diagram


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_itinerary_details);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setTitle(getString(R.string.details_itinerary));
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

        Bundle bundle = getIntent().getExtras();

        String s = Objects.requireNonNull(bundle).getString("itinerary");
        try {
            itinerary = new Itinerary(new JSONObject(s));
            Map<String, Object> objectReview = new HashMap<>(1);
            objectReview.put("reviewed_itinerary", itinerary.getCode());

            getDataFromServer(itinerary);
            getItineraryReviews(objectReview);

        } catch (JSONException e) {
            e.printStackTrace();
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
        fPrice.setText(Float.toString(itinerary.getFullPrice()));
        rPrice.setText(Float.toString(itinerary.getReducedPrice()));
        minP.setText(String.valueOf(itinerary.getMinParticipants()));
        maxP.setText(String.valueOf(itinerary.getMaxParticipants()));
        bDate.setText(out.format(itinerary.getBeginningDate()));
        eDate.setText(out.format(itinerary.getEndingDate()));
        rDate.setText(out.format(itinerary.getReservationDate()));
    }

    public void getItineraryReviews(Map<String, Object> itineraryCode) {
        SendInPostConnector<ItineraryReview> connector = new SendInPostConnector<>(
                ConnectorConstants.ITINERARY_REVIEW,
                BusinessEntityBuilder.getFactory(ItineraryReview.class),
                new DatabaseConnector.CallbackInterface<ItineraryReview>() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(List<ItineraryReview> list) {
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
                    }
                },
                itineraryCode);
        connector.execute();
    }

    public void goToAuthor(View view) {
        Intent i = new Intent().setClass(view.getContext(), AdminUserProfile.class);//TODO if-41
        Bundle bundle = new Bundle();
        bundle.putString("user", itinerary.getCicerone().toJSONObject().toString());
        i.putExtras(bundle);
        view.getContext().startActivity(i);
    }
}

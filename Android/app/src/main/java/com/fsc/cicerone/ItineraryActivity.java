package com.fsc.cicerone;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fsc.cicerone.model.Itinerary;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public abstract class ItineraryActivity extends AppCompatActivity {
    Itinerary itinerary;
    int layout = R.layout.activity_itinerary_details;

    public ItineraryActivity() {
        super();
    }

    public ItineraryActivity(int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        Bundle bundle = getIntent().getExtras();
        String s = Objects.requireNonNull(bundle).getString("itinerary");
        itinerary = new Itinerary(s);

        // Setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setTitle(itinerary.getTitle());

        bindDataToView();
    }

    void bindDataToView(){
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        ImageView image = findViewById(R.id.image);
        TextView description = findViewById(R.id.description);
        TextView bDate = findViewById(R.id.beginningDate);
        TextView eDate = findViewById(R.id.endingDate);
        TextView rDate = findViewById(R.id.reservationDate);
        RatingBar review = findViewById(R.id.itineraryReview); //TODO Use to bind review
        TextView author = findViewById(R.id.author);
        TextView minP = findViewById(R.id.minP);
        TextView maxP = findViewById(R.id.maxP);
        TextView location = findViewById(R.id.location);
        TextView repetitions = findViewById(R.id.repetitions);
        TextView duration = findViewById(R.id.duration);
        TextView fPrice = findViewById(R.id.fPrice);
        TextView rPrice = findViewById(R.id.rPrice);

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

        //TODO BIND REVIEWS
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

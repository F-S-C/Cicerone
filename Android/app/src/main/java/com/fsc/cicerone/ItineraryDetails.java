package com.fsc.cicerone;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.manager.WishlistManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.ItineraryReview;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

public class ItineraryDetails extends AppCompatActivity {
    private Button requestReservation;
    private FloatingActionButton modifyWishlistButton;
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
    private boolean isInWishlist;

    private Itinerary itinerary;

    private static final String ERROR_TAG = "ERROR IN " + ItineraryDetails.class.getName();

    public ItineraryDetails() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_details);
        requestReservation = findViewById(R.id.requestReservation);
        modifyWishlistButton = findViewById(R.id.intoWishlist);
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

        if (!AccountManager.isLogged()) {
            modifyWishlistButton.setEnabled(false);
            modifyWishlistButton.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            requestReservation.setEnabled(false);
        }

        Map<String, Object> objectReview = new HashMap<>();
        Bundle bundle = getIntent().getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);


        try {
            String s = Objects.requireNonNull(bundle).getString("itinerary");
            itinerary = new Itinerary(new JSONObject(s));

            supportActionBar.setTitle(itinerary.getTitle());

            getDataFromServer(itinerary);
            getItineraryReviews(objectReview);

            review.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent i = new Intent().setClass(ItineraryDetails.this, ItineraryReviewActivity.class);
                    i.putExtra("itinerary", itinerary.toJSONObject().toString());
                    i.putExtra("rating", review.getRating());
                    i.putExtra("reviewed_itinerary", itinerary.getCode());
                    startActivity(i);
                }
                return true;
            });

            if (AccountManager.isLogged()) {
                checkWishlist();
                isReserved();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        modifyWishlistButton.setOnClickListener(v -> {
            if (isInWishlist) {
                new MaterialAlertDialogBuilder(ItineraryDetails.this).
                        setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.confirm_delete))
                        .setPositiveButton(getString(R.string.yes), ((dialog, which) -> deleteFromWishlist()))
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
            } else {
                addToWishlist();
            }
        });
    }

    public void addToWishlist() {
        WishlistManager.addToWishlist(this, itinerary, result -> {
            if (result.getResult()) {
                Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.itinerary_added), Toast.LENGTH_SHORT).show();
                checkWishlist();
            }
        });
    }

    public void deleteFromWishlist() {
        WishlistManager.removeFromWishlist(this, itinerary, result -> {
            if (result.getResult()) {
                Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.itinerary_deleted), Toast.LENGTH_SHORT).show();
                checkWishlist();
            }
        });
    }


    public void checkWishlist() {
        WishlistManager.isInWishlist(this, itinerary, success -> {
            isInWishlist = success;
            modifyWishlistButton.setImageResource(isInWishlist ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_outline_favorite_border_24px);
        });

    }


    public void getDataFromServer(Itinerary itinerary) {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

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

    public void isReserved() {
        ReservationManager.isReserved(this, itinerary, success -> {
            if (success) {
                requestReservation.setText(getString(R.string.remove_reservation));
                requestReservation.setOnClickListener(ItineraryDetails.this::removeReservation);
            } else {
                requestReservation.setText(getString(R.string.request_reservation));
                requestReservation.setOnClickListener(ItineraryDetails.this::askForReservation);
            }
        });
    }


    public void askForReservation(View view) {
        View v = getLayoutInflater().inflate(R.layout.dialog_new_reservation, null);
        final Calendar myCalendar = Calendar.getInstance();

        // Get a reference to all the fields in the dialog
        EditText requestedDateInput = v.findViewById(R.id.requested_date_input);
        EditText numberOfAdultsInput = v.findViewById(R.id.number_of_adults_input);
        EditText numberOfChildrenInput = v.findViewById(R.id.number_of_children_input);

        // Set up the date picker on requestedDateInput click.
        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            requestedDateInput.setText(sdf.format(myCalendar.getTime()));
        };

        requestedDateInput.setOnClickListener(v1 -> {
            DatePickerDialog dialog = new DatePickerDialog(ItineraryDetails.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(itinerary.getBeginningDate().getTime());
            dialog.getDatePicker().setMaxDate(itinerary.getEndingDate().getTime());
            dialog.show();
        });

        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.insert_details))
                .setMessage(getString(R.string.reservation_dialog_message))
                .setView(v)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    try {
                        ReservationManager.addReservation(itinerary,
                                Integer.parseInt(numberOfAdultsInput.getText().toString()),
                                Integer.parseInt(numberOfChildrenInput.getText().toString()),
                                new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(requestedDateInput.getText().toString()));
                    } catch (ParseException e) {
                        Log.e(ERROR_TAG, e.getMessage());
                    }
                    isReserved();
                    Toast.makeText(ItineraryDetails.this, R.string.reservation_added, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, (dialog, id) -> {
                    // Do nothing
                })
                .show();
    }

    public void removeReservation(View v) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.sure_to_remove_reservation))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    ReservationManager.removeReservation(itinerary);
                    Toast.makeText(ItineraryDetails.this, R.string.reservation_removed, Toast.LENGTH_SHORT).show();

                    isReserved();
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    public void goToAuthor(View view) {
        Intent i = new Intent().setClass(view.getContext(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("reviewed_user", author.getText().toString());
        i.putExtras(bundle);
        view.getContext().startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}


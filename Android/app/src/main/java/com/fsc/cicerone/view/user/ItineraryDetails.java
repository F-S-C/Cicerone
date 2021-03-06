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

package com.fsc.cicerone.view.user;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.manager.ReviewManager;
import com.fsc.cicerone.manager.WishlistManager;
import com.fsc.cicerone.model.ItineraryReview;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.itinerary.ItineraryActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A class that represents the user interface to show all the details of an itinerary.
 */
public class ItineraryDetails extends ItineraryActivity {

    private Button requestReservation;
    private FloatingActionButton modifyWishlistButton;
    private Button addReview;

    private boolean isInWishlist;

    private TextView messageNoReview;
    private EditText descriptionReview;
    private RatingBar feedbackReview;

    private RecyclerView recyclerView;

    private EditText requestedDateInput;
    private EditText numberOfAdultsInput;
    private EditText numberOfChildrenInput;

    private static final String ERROR_TAG = "ERROR IN " + ItineraryDetails.class.getName();

    /**
     * Constructor for the class.
     */
    public ItineraryDetails() {
        super();
        this.layout = R.layout.activity_itinerary_details;
    }

    /**
     * A second Constructor for the class that takes a LayoutId as a parameter.
     *
     * @param contentLayoutId the Layout to set.
     */
    public ItineraryDetails(int contentLayoutId) {
        super(contentLayoutId);
        this.layout = R.layout.activity_itinerary_details;
    }

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestReservation = findViewById(R.id.requestReservation);
        modifyWishlistButton = findViewById(R.id.intoWishlist);
        addReview = findViewById(R.id.insertReview);

        if (!AccountManager.isLogged()) {
            modifyWishlistButton.setEnabled(false);
            modifyWishlistButton.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            requestReservation.setEnabled(false);
            addReview.setEnabled(false);
        }


        recyclerView = findViewById(R.id.reviewList);
        messageNoReview = findViewById(R.id.messageNoReview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (AccountManager.isLogged()) {
            // itinerary for wishlist
            checkWishlist();
            // itinerary for reservation
            isReserved();
            // itinerary for review
            permissionReview();

        }

        modifyWishlistButton.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            if (isInWishlist) {
                new MaterialAlertDialogBuilder(ItineraryDetails.this).setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.confirm_delete))
                        .setPositiveButton(getString(R.string.yes), ((dialog, which) -> deleteFromWishlist()))
                        .setNegativeButton(getString(R.string.no), null).show();
            } else {
                addToWishlist();
            }
        });

    }

    /**
     * A function that add to the logged User wishlist the current Itinerary.
     */
    public void addToWishlist() {
        WishlistManager.addToWishlist(this, itinerary, result -> {
            if (result.getResult()) {
                Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.itinerary_added),
                        Toast.LENGTH_SHORT).show();
                checkWishlist();
            }
        });
    }

    /**
     * A function that deletes the current Itinerary from the logged User wishlist.
     */
    public void deleteFromWishlist() {
        WishlistManager.removeFromWishlist(this, itinerary, result -> {
            if (result.getResult()) {
                Toast.makeText(ItineraryDetails.this, ItineraryDetails.this.getString(R.string.itinerary_deleted),
                        Toast.LENGTH_SHORT).show();
                checkWishlist();
            }
        });
    }

    /**
     * A function that checks if the current Itinerary is already in the logged User wishlist.
     */
    public void checkWishlist() {
        WishlistManager.isInWishlist(this, itinerary, success -> {
            isInWishlist = success;
            modifyWishlistButton.setImageResource(
                    isInWishlist ? R.drawable.ic_favorite_black_24dp : R.drawable.ic_outline_favorite_border_24px);
        });

    }

    /**
     * A function that checks if the logged User has already asked for a reservation for the current
     * Itinerary.
     */
    public void isReserved() {
        ReservationManager.isReserved(this, itinerary, success -> {
            if (success) {
                ReservationManager.isConfirmed(this, itinerary, success1 -> {
                    if (success1)
                        requestReservation.setText(getString(R.string.remove_reservation));
                    else
                        requestReservation.setText(getString(R.string.remove_request_reservation));
                });
                requestReservation.setOnClickListener(ItineraryDetails.this::removeReservation);
            } else {
                requestReservation.setText(getString(R.string.request_reservation));
                requestReservation.setOnClickListener(ItineraryDetails.this::askForReservation);
            }
        });
    }

    /**
     * A function that checks if the logged User has the permission to review the current
     * Itinerary.
     */
    public void permissionReview() {
        ReviewManager.permissionReviewItinerary(this, AccountManager.getCurrentLoggedUser(), itinerary, success -> {
            if (success)
                isReviewed();
        }, () -> addReview.setEnabled(false));

    }

    /**
     * A function that checks if the current Itinerary has been already reviewed by the logged
     * User.
     */
    public void isReviewed() {
        ReviewManager.isReviewedItinerary(this, AccountManager.getCurrentLoggedUser(), itinerary, (result, found) -> {
            if (found) {
                ItineraryReview itineraryReview = (ItineraryReview) result;
                addReview.setEnabled(true);
                addReview.setText(getString(R.string.updateReview));
                addReview.setOnClickListener(view -> ItineraryDetails.this.updateReview(itineraryReview));
            } else {
                ItineraryReview itineraryReview = new ItineraryReview.Builder(AccountManager.getCurrentLoggedUser(),
                        itinerary).build();
                addReview.setEnabled(true);
                addReview.setText(getString(R.string.add_review));
                addReview.setOnClickListener(view -> ItineraryDetails.this.addReview(itineraryReview));
            }

        });
    }

    /**
     * A function that allows the logged User to update his Review for the current Itinerary.
     *
     * @param itineraryReview The Review to update.
     */
    private void updateReview(ItineraryReview itineraryReview) {
        View viewReview = getLayoutInflater().inflate(R.layout.dialog_new_review, null);

        // Get a reference to all the fields in the dialog
        descriptionReview = viewReview.findViewById(R.id.objectReview);
        feedbackReview = viewReview.findViewById(R.id.feedbackReview);
        descriptionReview.setText(itineraryReview.getDescription());
        feedbackReview.setRating(itineraryReview.getFeedback());

        AlertDialog dialogUpdate = new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.update_review))
                .setView(viewReview)
                .setPositiveButton(R.string.update_review, null)
                .setNegativeButton(R.string.delete_review, null)
                .setNeutralButton(R.string.cancel, null)
                .create();

        dialogUpdate.setOnShowListener(dialogInterface -> {

            Button buttonPositive = dialogUpdate.getButton(AlertDialog.BUTTON_POSITIVE);
            Button buttonNegative = dialogUpdate.getButton(AlertDialog.BUTTON_NEGATIVE);

            buttonPositive.setOnClickListener(view -> {
                itineraryReview.setDescription(descriptionReview.getText().toString());
                itineraryReview.setFeedback((int) feedbackReview.getRating());
                if (allFilledReview()) {
                    ReviewManager.updateReviewItinerary(this, itineraryReview,
                            success -> Toast.makeText(ItineraryDetails.this,
                                    ItineraryDetails.this.getString(R.string.updated_review), Toast.LENGTH_SHORT)
                                    .show());
                    isReviewed();
                    refresh();
                    dialogUpdate.dismiss();
                } else {
                    if (feedbackReview.getRating() == 0)
                        Toast.makeText(this, ItineraryDetails.this.getString(R.string.empty_feedback_error),
                                Toast.LENGTH_SHORT).show();
                    if (descriptionReview.getText().toString().equals(""))
                        descriptionReview.setError(getString(R.string.empty_description_error));
                }

            });
            buttonNegative.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.are_you_sure)).setMessage(getString(R.string.sure_to_remove_review))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        ReviewManager.deleteReviewItinerary(this, itineraryReview,
                                success -> Toast.makeText(this,
                                        ItineraryDetails.this.getString(R.string.deleted_review), Toast.LENGTH_SHORT)
                                        .show());
                        isReviewed();
                        refresh();
                        dialogUpdate.dismiss();
                    }).setNegativeButton(getString(R.string.no), null).show());
        });
        dialogUpdate.show();
    }

    /**
     * A function that allows the logged User to insert a Review for the current Itinerary.
     *
     * @param itineraryReview The Review to insert.
     */
    private void addReview(ItineraryReview itineraryReview) {
        View viewReview = getLayoutInflater().inflate(R.layout.dialog_new_review, null);
        // Get a reference to all the fields in the dialog
        descriptionReview = viewReview.findViewById(R.id.objectReview);
        feedbackReview = viewReview.findViewById(R.id.feedbackReview);
        descriptionReview.setText("");
        feedbackReview.setRating(0);

        AlertDialog dialogSubmit = new MaterialAlertDialogBuilder(this).setView(viewReview)
                .setTitle(getString(R.string.add_review)).setMessage(getString(R.string.review_dialog_message))
                .setPositiveButton(R.string.submit_review, null).setNegativeButton(android.R.string.cancel, null)
                .create();

        dialogSubmit.setOnShowListener(dialogInterface -> {

            Button button = dialogSubmit.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                itineraryReview.setFeedback((int) feedbackReview.getRating());
                itineraryReview.setDescription(descriptionReview.getText().toString());
                if (allFilledReview()) {
                    ReviewManager
                            .addReviewItinerary(this, itineraryReview,
                                    success -> Toast.makeText(this,
                                            ItineraryDetails.this.getString(R.string.added_review), Toast.LENGTH_SHORT)
                                            .show());

                    isReviewed();
                    refresh();
                    dialogSubmit.dismiss();
                } else {
                    if (feedbackReview.getRating() == 0)
                        Toast.makeText(this, ItineraryDetails.this.getString(R.string.empty_feedback_error),
                                Toast.LENGTH_SHORT).show();
                    if (descriptionReview.getText().toString().equals(""))
                        descriptionReview.setError(getString(R.string.empty_description_error));
                }
            });
        });
        dialogSubmit.show();

    }

    /**
     * A function that allows the logged User to make a reservation for the current Itinerary.
     *
     * @param view The current View.
     */
    public void askForReservation(View view) {
        View v = getLayoutInflater().inflate(R.layout.dialog_new_reservation, null);
        final Calendar myCalendar = Calendar.getInstance();

        // Get a reference to all the fields in the dialog
        requestedDateInput = v.findViewById(R.id.requested_date_input);
        numberOfAdultsInput = v.findViewById(R.id.number_of_adults_input);
        numberOfChildrenInput = v.findViewById(R.id.number_of_children_input);

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
            DatePickerDialog dialog = new DatePickerDialog(ItineraryDetails.this, date, myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMinDate(itinerary.getBeginningDate().getTime());
            dialog.getDatePicker().setMaxDate(itinerary.getEndingDate().getTime());
            dialog.show();
        });

        AlertDialog dialogSubmit = new MaterialAlertDialogBuilder(this).setView(v)
                .setTitle(getString(R.string.insert_details)).setMessage(getString(R.string.reservation_dialog_message))
                .setPositiveButton(R.string.submit_request, null).setNegativeButton(R.string.cancel, null).create();

        dialogSubmit.setOnShowListener(dialogInterface -> {

            Button button = dialogSubmit.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view1 -> {
                if (allFilledReservation()) {
                    if (checkParticipantsLimit()) {
                        try {
                            ReservationManager.addReservation(this,
                                    itinerary,
                                    Integer.parseInt(numberOfAdultsInput.getText().toString()),
                                    Integer.parseInt(numberOfChildrenInput.getText().toString()),
                                    new SimpleDateFormat("yyyy-MM-dd", Locale.US)
                                            .parse(requestedDateInput.getText().toString()));
                        } catch (ParseException e) {
                            Log.e(ERROR_TAG, e.getMessage());
                        }
                        isReserved();
                        Toast.makeText(ItineraryDetails.this, R.string.reservation_added, Toast.LENGTH_SHORT).show();
                        dialogSubmit.dismiss();
                    } else
                        Toast.makeText(this, getString(R.string.participants_limit_reached), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, ItineraryDetails.this.getString(R.string.error_fields_empty),
                            Toast.LENGTH_SHORT).show();
            });
        });
        dialogSubmit.show();

    }

    /**
     * A function that allows the logged User to remove his Reservation for the current Itinerary.
     *
     * @param v The current View.
     */
    public void removeReservation(View v) {
        new MaterialAlertDialogBuilder(this).setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.sure_to_remove_reservation))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    ReservationManager.removeReservation(itinerary);
                    Toast.makeText(ItineraryDetails.this, R.string.reservation_removed, Toast.LENGTH_SHORT).show();

                    isReserved();
                }).setNegativeButton(getString(R.string.no), null).show();
    }

    /**
     * A function that allows the logged User to visit the profile of the author of the current
     * Itinerary.
     *
     * @param view The current View
     */
    @Override
    public void goToAuthor(View view) {
        if (itinerary.getCicerone().getUserType() == UserType.ADMIN) {
            Toast.makeText(this, getString(R.string.warning_deleted_user), Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("reviewed_user", itinerary.getCicerone().toString());
        startActivityWithData(ProfileActivity.class, bundle);
    }

    /**
     * @see ItineraryActivity#bindDataToView()
     */
    @Override
    public void bindDataToView() {
        requestDataForRecycleView();
        super.bindDataToView();
    }

    /**
     * A function that takes the reviews of the current Itinerary and set them into a RecyclerView.
     */
    private void requestDataForRecycleView() {
        ReviewManager.requestItineraryReviews(this, itinerary, list -> {
            if (!list.isEmpty()) {
                messageNoReview.setVisibility(View.GONE);
                RecyclerView.Adapter adapter = new ReviewAdapter(this, list);
                recyclerView.setAdapter(adapter);
            } else {
                messageNoReview.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(null);
            }
        });
    }

    /**
     * A function that checks if every field of the Review is filled.
     *
     * @return True if every field is filled, False otherwise.
     */
    private boolean allFilledReview() {
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }

    /**
     * A function that checks if every field of the Reservation is filled.
     *
     * @return True if every field is filled, False otherwise.
     */
    private boolean allFilledReservation() {
        return !requestedDateInput.getText().toString().equals("")
                && !numberOfAdultsInput.getText().toString().equals("")
                && !numberOfChildrenInput.getText().toString().equals("");
    }

    private boolean checkParticipantsLimit() {
        int sum = Integer.parseInt(numberOfAdultsInput.getText().toString())
                + Integer.parseInt(numberOfChildrenInput.getText().toString());
        return sum >= itinerary.getMinParticipants() && sum <= itinerary.getMaxParticipants();
    }

}

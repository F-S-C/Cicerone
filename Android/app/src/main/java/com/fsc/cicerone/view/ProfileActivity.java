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

package com.fsc.cicerone.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReviewManager;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

/**
 * ProfileActivity is the class that allows you to build a user's profile activity.
 **/
public class ProfileActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;

    private TextView username;
    private TextView name;
    private TextView surname;
    private TextView email;
    private TextView userType;
    private RatingBar star;
    private Button buttReview;
    private RecyclerView recyclerView;

    private TextView descriptionReview;
    private RatingBar feedbackReview;
    private TextView messageNoReview;

    private User reviewedUser;
    private UserReview userReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set elements view
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.username_profile);
        name = findViewById(R.id.name_profile);
        surname = findViewById(R.id.surname_profile);
        email = findViewById(R.id.email_profile);
        userType = findViewById(R.id.user_type_profile);
        star = findViewById(R.id.avg_feedback);
        buttReview = findViewById(R.id.insertUserReview);
        recyclerView = findViewById(R.id.reviewUserList);
        messageNoReview = findViewById(R.id.messageNoReview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.profile);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        reviewedUser = new User(bundle.getString("reviewed_user"));

        //set TextView data user
        getData(reviewedUser);

        requestDataForRecycleView(recyclerView);

        if (!AccountManager.isLogged()) {
            buttReview.setEnabled(false);
        }

        if (AccountManager.isLogged()) {
            permissionReview();
        }
        //set recycler review list

    }

    private void getData(User reviewedUser) {
        final String LABEL_AND_CONTENT = "%s%s %s";
        username.setText(String.format(getString(R.string.username_display), reviewedUser.getUsername()));
        name.setText(String.format(LABEL_AND_CONTENT, getString(R.string.name), getString(R.string.separator), reviewedUser.getName()));
        surname.setText(String.format(LABEL_AND_CONTENT, getString(R.string.surname), getString(R.string.separator), reviewedUser.getSurname()));
        email.setText(String.format(LABEL_AND_CONTENT, getString(R.string.email), getString(R.string.separator), reviewedUser.getEmail()));
        if (reviewedUser.getUserType() == UserType.CICERONE) {
            userType.setText(R.string.user_type_cicerone);
        } else {
            userType.setText(R.string.user_type_globetrotter);
        }
        ImageView imageView = findViewById(R.id.image_profile);
        imageView.setImageResource(reviewedUser.getSex().getAvatarResource());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void permissionReview() {
        ReviewManager.permissionReviewUser(this, reviewedUser, AccountManager.getCurrentLoggedUser(), success -> {
            if (success) isReviewed();
        }, () -> buttReview.setEnabled(false));
    }

    public void isReviewed() {

        ReviewManager.isReviewedUser(this, reviewedUser, AccountManager.getCurrentLoggedUser(), (result, found) -> {
            if (found) {
                UserReview review = (UserReview) result;
                buttReview.setEnabled(true);
                buttReview.setText(getString(R.string.updateReview));
                buttReview.setOnClickListener(view -> ProfileActivity.this.updateReview(review));
            } else {
                userReview = new UserReview.Builder(AccountManager.getCurrentLoggedUser(), reviewedUser).build();
                buttReview.setEnabled(true);
                buttReview.setText(getString(R.string.add_review));
                buttReview.setOnClickListener(view -> ProfileActivity.this.addReview(userReview));
            }

        });

    }

    private void updateReview(UserReview userReview) {
        View viewReview = getLayoutInflater().inflate(R.layout.dialog_new_review, null);
        // Get a reference to all the fields in the dialog
        descriptionReview = viewReview.findViewById(R.id.objectReview);
        feedbackReview = viewReview.findViewById(R.id.feedbackReview);
        descriptionReview.setText(userReview.getDescription());
        feedbackReview.setRating(userReview.getFeedback());

        AlertDialog dialogUpdate = new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.update_review))
                .setView(viewReview)
                .setPositiveButton(R.string.update_review, null)
                .setNegativeButton(R.string.delete_review, null)
                .setNeutralButton(R.string.cancel,null)
                .create();

        dialogUpdate.setOnShowListener(dialogInterface -> {

            Button buttonPositive = dialogUpdate.getButton(AlertDialog.BUTTON_POSITIVE);
            Button buttonNegative = dialogUpdate.getButton(AlertDialog.BUTTON_NEGATIVE);

            buttonPositive.setOnClickListener(view -> {
                userReview.setDescription(descriptionReview.getText().toString());
                userReview.setFeedback((int) feedbackReview.getRating());
                if (allFilled()) {
                    ReviewManager.updateReviewUser(this, userReview, success -> Toast.makeText(this, ProfileActivity.this.getString(R.string.updated_review),
                            Toast.LENGTH_SHORT).show());
                    isReviewed();
                    dialogUpdate.dismiss();
                    requestDataForRecycleView(recyclerView);
                } else{
                    if(feedbackReview.getRating() ==0) Toast.makeText(this, ProfileActivity.this.getString(R.string.empty_feedback_error),
                            Toast.LENGTH_SHORT).show();
                    if(descriptionReview.getText().toString().equals("")) descriptionReview.setError(getString(R.string.empty_description_error));

                }

            });
            buttonNegative.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.sure_to_remove_review))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        ReviewManager.deleteReviewUser(this, userReview, success -> Toast.makeText(this, ProfileActivity.this.getString(R.string.deleted_review),
                                Toast.LENGTH_SHORT).show());
                        isReviewed();
                        dialogUpdate.dismiss();
                        requestDataForRecycleView(recyclerView);
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show());
        });
        dialogUpdate.show();
    }

    private void addReview(UserReview userReview) {
        View viewReview = getLayoutInflater().inflate(R.layout.dialog_new_review, null);
        // Get a reference to all the fields in the dialog
        descriptionReview = viewReview.findViewById(R.id.objectReview);
        feedbackReview = viewReview.findViewById(R.id.feedbackReview);
        descriptionReview.setText("");
        feedbackReview.setRating(0);

        AlertDialog dialogSubmit = new MaterialAlertDialogBuilder(this)
                .setView(viewReview)
                .setTitle(getString(R.string.add_review))
                .setMessage(getString(R.string.review_dialog_message))
                .setPositiveButton(R.string.submit_review, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialogSubmit.setOnShowListener(dialogInterface -> {

            Button button = dialogSubmit.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                userReview.setFeedback((int) feedbackReview.getRating());
                userReview.setDescription(descriptionReview.getText().toString());
                if (allFilled()) {
                    ReviewManager.addReviewUser(this, userReview, success -> Toast.makeText(this, ProfileActivity.this.getString(R.string.added_review),
                            Toast.LENGTH_SHORT).show());
                    isReviewed();
                    requestDataForRecycleView(recyclerView);
                    dialogSubmit.dismiss();
                } else{
                    if(feedbackReview.getRating() ==0) Toast.makeText(this, ProfileActivity.this.getString(R.string.empty_feedback_error),
                            Toast.LENGTH_SHORT).show();
                    if(descriptionReview.getText().toString().equals("")) descriptionReview.setError(getString(R.string.empty_description_error));
                }

            });
        });
        dialogSubmit.show();
    }

    private boolean allFilled() {
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }

    private void requestDataForRecycleView(RecyclerView recyclerView) {
        ReviewManager.getAvgUserFeedback(this, reviewedUser, value -> star.setRating(value));
        ReviewManager.requestUserReviews(this, reviewedUser, null, list -> {
            if(!list.isEmpty()) {
                messageNoReview.setVisibility(View.GONE);
                adapter = new ReviewAdapter(ProfileActivity.this, list);
                recyclerView.setAdapter(adapter);
            }else {
                messageNoReview.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(null);
            }
        });
    }
}
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

package com.fsc.cicerone;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;
import com.fsc.cicerone.model.UserType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

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

    private User reviewed_user;
    private Map<String, Object> params;
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
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        params = new HashMap<>();

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle(R.string.profile);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle bundle =  Objects.requireNonNull(getIntent().getExtras());
        reviewed_user = new User();
        try {
            reviewed_user = new User(new JSONObject(bundle.getString("reviewed_user")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //set TextView data user
        getData(reviewed_user);
        //set avg feedback user
        avgReviewUser(reviewed_user);

        if(!AccountManager.isLogged()){
            buttReview.setEnabled(false);
        }

        if(AccountManager.isLogged()){
            params.put("reviewed_user", reviewed_user.getUsername());
            params.put("username",AccountManager.getCurrentLoggedUser().getUsername());
            permissionReview(params);
        }
        //set recycler review list
        requestDataForRecycleView(params, recyclerView);


    }

    private void getData(User reviewed_user) {

        username.setText("@" + reviewed_user.getUsername());
        name.setText(String.format("%s%s %s", getString(R.string.name), getString(R.string.separator), reviewed_user.getName()));
        surname.setText(String.format("%s%s %s", getString(R.string.surname), getString(R.string.separator), reviewed_user.getSurname()));
        email.setText(String.format("%s%s %s", getString(R.string.email), getString(R.string.separator), reviewed_user.getEmail()));
        if (reviewed_user.getUserType() == UserType.CICERONE) {
            userType.setText(R.string.user_type_cicerone);
        } else {
            userType.setText(R.string.user_type_globetrotter);
        }
        ImageView imageView = findViewById(R.id.image_profile);
        imageView.setImageResource(reviewed_user.getSex().getAvatarResource());
    }

    private void avgReviewUser(User review_user){

        SendInPostConnector<UserReview> connectorReview = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    int sum = 0;
                    for (UserReview review : list) {
                        sum += review.getFeedback();
                    }
                    star.setRating((!list.isEmpty()) ? ((float) sum / list.size()) : 0);
                })
                .setObjectToSend(SendInPostConnector.paramsFromObject(review_user))
                .build();
        connectorReview.execute();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void permissionReview(Map<String, Object> review) {

        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.REQUEST_FOR_REVIEW)
                .setContext(this)
                .setOnStartConnectionListener(() -> buttReview.setEnabled(false))
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if(result.getResult())
                        isReviewed(review);
                })
                .setObjectToSend(review)
                .build();
        connector.execute();
    }

    public void isReviewed(Map<String, Object> review) {
        SendInPostConnector<UserReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    if (!list.isEmpty()) {
                        userReview = list.get(0);
                        buttReview.setEnabled(true);
                        buttReview.setText(getString(R.string.updateReview));
                        buttReview.setOnClickListener(view -> ProfileActivity.this.updateReview());

                    } else {
                        userReview = new UserReview.Builder(AccountManager.getCurrentLoggedUser(), reviewed_user).build();
                        buttReview.setEnabled(true);
                        buttReview.setText(getString(R.string.add_review));
                        buttReview.setOnClickListener(view -> ProfileActivity.this.addReview());
                    }
                })
                .setObjectToSend(review)
                .build();
        connector.execute();

    }

    private void updateReview() {
        View view = getLayoutInflater().inflate(R.layout.dialog_new_review, null);
        // Get a reference to all the fields in the dialog
        descriptionReview = view.findViewById(R.id.objectReview);
        feedbackReview = view.findViewById(R.id.feedbackReview);
        descriptionReview.setText(userReview.getDescription());
        feedbackReview.setRating(userReview.getFeedback());

        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.update_review))
                .setView(view)
                .setPositiveButton(R.string.update_review, (dialog, id) -> {
                    userReview.setDescription(descriptionReview.getText().toString());
                    userReview.setFeedback((int) feedbackReview.getRating());
                    if (allFilled()) {
                        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_USER_REVIEW)
                                .setContext(this)
                                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                                    if (result.getResult()) {
                                        Toast.makeText(this, ProfileActivity.this.getString(R.string.updated_review),
                                                Toast.LENGTH_SHORT).show();
                                        isReviewed(params);
                                        requestDataForRecycleView(params,recyclerView);
                                    }
                                })
                                .setObjectToSend(SendInPostConnector.paramsFromObject(userReview))
                                .build();
                        connector.execute();
                    } else
                        Toast.makeText(this, ProfileActivity.this.getString(R.string.error_fields_empty),
                                Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.delete_review, (dialog, id) -> {
                    BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.DELETE_USER_REVIEW)
                            .setContext(this)
                            .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                                if (result.getResult()) {
                                    Toast.makeText(this, ProfileActivity.this.getString(R.string.deleted_review),
                                            Toast.LENGTH_SHORT).show();
                                    isReviewed(params);
                                    requestDataForRecycleView(params,recyclerView);
                                }
                            })
                            .setObjectToSend(SendInPostConnector.paramsFromObject(userReview))
                            .build();
                    connector.execute();
                })
                .show();
    }

    private void addReview() {
        View view = getLayoutInflater().inflate(R.layout.dialog_new_review, null);
        // Get a reference to all the fields in the dialog
        descriptionReview = view.findViewById(R.id.objectReview);
        feedbackReview = view.findViewById(R.id.feedbackReview);
        descriptionReview.setText("");
        feedbackReview.setRating(0);


        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.add_review))
                .setMessage(getString(R.string.review_dialog_message))
                .setView(view)
                .setPositiveButton(R.string.add_review, (dialog, id) -> {
                    if (allFilled()) {
                        userReview.setFeedback((int) feedbackReview.getRating());
                        userReview.setDescription(descriptionReview.getText().toString());
                        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_USER_REVIEW)
                                .setContext(this)
                                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                                    if (result.getResult())
                                        Toast.makeText(this, ProfileActivity.this.getString(R.string.added_review),
                                                Toast.LENGTH_SHORT).show();
                                    isReviewed(params);
                                    requestDataForRecycleView(params, recyclerView);
                                })
                                .setObjectToSend(SendInPostConnector.paramsFromObject(userReview))
                                .build();
                        connector.execute();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private boolean allFilled() {
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }

    private void requestDataForRecycleView(Map<String, Object> review, RecyclerView recyclerView) {
        SendInPostConnector<UserReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    adapter = new ReviewAdapter(this, list);
                    recyclerView.setAdapter(adapter);
                })
                .setObjectToSend(review)
                .build();
        connector.execute();
    }
}
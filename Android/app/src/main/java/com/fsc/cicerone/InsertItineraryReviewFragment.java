package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.ItineraryReview;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class InsertItineraryReviewFragment extends Fragment {

    private ItineraryReview result;
    private Button submitReview;
    private Button updateReview;
    private Button deleteReview;
    private RatingBar feedbackReview;
    private EditText descriptionReview;
    private TextView message;
    private TextView messageFeedback;
    private TextView messageDescription;

    //TODO: Are param and sendParam both needed?
    private Map<String, Object> param;
    private Map<String, Object> sendParam;

    public InsertItineraryReviewFragment() {
        // Required public empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_itinerary_review, container, false);
        message = view.findViewById(R.id.noInsertReview);
        messageDescription = view.findViewById(R.id.insertDescription);
        messageFeedback = view.findViewById(R.id.insertFeedback);
        submitReview = view.findViewById(R.id.submitReview);
        updateReview = view.findViewById(R.id.updateReview);
        deleteReview = view.findViewById(R.id.deleteReview);
        descriptionReview = view.findViewById(R.id.inputDescription);
        feedbackReview = view.findViewById(R.id.inputFeedback);
        Bundle bundle = getArguments();


        param = new HashMap<>();

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        param.put("username", currentLoggedUser.getUsername());
        param.put("booked_itinerary", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("reviewed_itinerary")));
        param.put("itinerary", Objects.requireNonNull(bundle.getString("itinerary")));

        requestReview(param);

        return view;

    }

    private void requestReview(Map<String, Object> parameters) {
        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(getContext())
                .setOnEndConnectionListener(list -> {
                    sendParam = new HashMap<>();

                    if (!list.isEmpty()) {
                        message.setVisibility(View.GONE);
                        parameters.put("reviewed_itinerary", Objects.requireNonNull(parameters.get("booked_itinerary")).toString());
                        checkReview(parameters);

                        submitReview.setOnClickListener(v -> {
                            if (allFilled()) {
                                sendParam.put("itinerary", Objects.requireNonNull(param.get("itinerary")).toString());
                                sendParam.put("username", Objects.requireNonNull(param.get("username")).toString());
                                sendParam.put("reviewed_itinerary", Objects.requireNonNull(param.get("reviewed_itinerary")).toString());
                                sendParam.put("description", descriptionReview.getText().toString());
                                sendParam.put("feedback", (int) feedbackReview.getRating());
                                Log.e("username:", Objects.requireNonNull(sendParam.get("username")).toString());

                                submitReview(sendParam);
                            } else
                                Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

                        });
                        deleteReview.setOnClickListener(v -> {

                            sendParam.put("username", Objects.requireNonNull(parameters.get("username")).toString());
                            sendParam.put("reviewed_itinerary", Objects.requireNonNull(parameters.get("reviewed_itinerary")).toString());
                            sendParam.put("itinerary", Objects.requireNonNull(parameters.get("itinerary")).toString());
                            deleteReview(sendParam);

                        });
                        updateReview.setOnClickListener(v -> {
                            if (allFilled()) {
                                sendParam.put("username", Objects.requireNonNull(parameters.get("username")).toString());
                                sendParam.put("reviewed_itinerary", Objects.requireNonNull(parameters.get("reviewed_itinerary")).toString());
                                sendParam.put("description", descriptionReview.getText().toString());
                                sendParam.put("feedback", (int) feedbackReview.getRating());
                                sendParam.put("itinerary", Objects.requireNonNull(parameters.get("itinerary")).toString());

                                updateReview(sendParam);
                            } else
                                Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

                        });
                    } else {
                        feedbackReview.setVisibility(View.GONE);
                        descriptionReview.setVisibility(View.GONE);
                        submitReview.setVisibility(View.GONE);
                        deleteReview.setVisibility(View.GONE);
                        updateReview.setVisibility(View.GONE);
                        messageFeedback.setVisibility(View.GONE);
                        messageDescription.setVisibility(View.GONE);
                    }

                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    private void checkReview(Map<String, Object> parameters) {
        SendInPostConnector<ItineraryReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(getContext())
                .setOnEndConnectionListener(list -> {
                    message.setVisibility(View.GONE);
                    if (!list.isEmpty()) {
                        result = list.get(0);
                        submitReview.setVisibility(View.GONE);
                        updateReview.setVisibility(View.VISIBLE);
                        deleteReview.setVisibility(View.VISIBLE);
                        descriptionReview.setText(result.getDescription());
                        feedbackReview.setRating(result.getFeedback());
                    } else {
                        feedbackReview.setRating(0);
                        descriptionReview.setText("");
                        submitReview.setVisibility(View.VISIBLE);
                        updateReview.setVisibility(View.GONE);
                        deleteReview.setVisibility(View.GONE);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    private void submitReview(Map<String, Object> sendParam) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_ITINERARY_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.added_review), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ItineraryReviewFragment.class);
                        Bundle b = new Bundle();
                        b.putString("reviewed_itinerary", Objects.requireNonNull(sendParam.get("reviewed_itinerary")).toString());
                        b.putString("itinerary", Objects.requireNonNull(sendParam.get("itinerary")).toString());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtras(b);
                        startActivity(i);
                    }

                })
                .setObjectToSend(sendParam)
                .build();
        connector.execute();
    }

    private boolean allFilled() {
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }


    private void deleteReview(Map<String, Object> param) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.DELETE_ITINERARY_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.deleted_review), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ItineraryReviewFragment.class);
                        Bundle b = new Bundle();
                        b.putString("reviewed_itinerary", Objects.requireNonNull(param.get("reviewed_itinerary")).toString());
                        b.putString("itinerary", Objects.requireNonNull(param.get("itinerary")).toString());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtras(b);
                        startActivity(i);
                    }

                })
                .setObjectToSend(param)
                .build();
        connector.execute();
    }

    private void updateReview(Map<String, Object> param) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_ITINERARY_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.updated_review), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(getActivity(), ItineraryReviewFragment.class);
                        Bundle b = new Bundle();
                        b.putString("reviewed_itinerary", Objects.requireNonNull(param.get("reviewed_itinerary")).toString());
                        b.putString("itinerary", Objects.requireNonNull(param.get("itinerary")).toString());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtras(b);
                        startActivity(i);
                    }

                })
                .setObjectToSend(param)
                .build();
        connector.execute();
    }

}

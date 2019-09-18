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
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserReview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class InsertReviewFragment extends Fragment {

    private UserReview result;
    private Button submitReview;
    private Button updateReview;
    private Button deleteReview;
    private RatingBar feedbackReview;
    private EditText descriptionReview;
    private TextView message;
    private TextView messageFeedback;
    private TextView messageDescription;

    // TODO: Are both param and sendParam both needed?
    private Map<String, Object> param;
    private Map<String, Object> sendParam;

    public InsertReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_review, container, false);
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
        sendParam = new HashMap<>();

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        param.put("username", currentLoggedUser.getUsername());
        param.put("reviewed_user", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("reviewed_user")));

        requestReview(param);

        return view;

    }

    private void requestReview(Map<String, Object> parameters) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.REQUEST_FOR_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {

                    if (result.getResult()) {
                        message.setVisibility(View.GONE);
                        checkReview(parameters);

                        submitReview.setOnClickListener(v -> {
                            if (allFilled()) {
                                sendParam.put("username", Objects.requireNonNull(param.get("username")).toString());
                                sendParam.put("reviewed_user", Objects.requireNonNull(param.get("reviewed_user")).toString());
                                sendParam.put("description", descriptionReview.getText().toString());
                                sendParam.put("feedback", (int) feedbackReview.getRating());
                                Log.e("username:", Objects.requireNonNull(sendParam.get("username")).toString());
                                Log.e("reviewed_user:", Objects.requireNonNull(sendParam.get("reviewed_user")).toString());
                                Log.e("description:", Objects.requireNonNull(sendParam.get("description")).toString());
                                Log.e("feedback:", Objects.requireNonNull(sendParam.get("feedback")).toString());

                                submitReview(sendParam);
                            } else
                                Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

                        });
                        deleteReview.setOnClickListener(v -> deleteReview(param));
                        updateReview.setOnClickListener(v -> {
                            if (allFilled()) {
                                sendParam.put("username", Objects.requireNonNull(param.get("username")).toString());
                                sendParam.put("reviewed_user", Objects.requireNonNull(param.get("reviewed_user")).toString());
                                sendParam.put("description", descriptionReview.getText().toString());
                                sendParam.put("feedback", (int) feedbackReview.getRating());

                                updateReview(sendParam);
                            } else
                                Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

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
        SendInPostConnector<UserReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
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

    private void submitReview(Map<String, Object> sendparam) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.INSERT_USER_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.added_review), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        Bundle b = new Bundle();
                        b.putString("reviewed_user", Objects.requireNonNull(sendparam.get("reviewed_user")).toString());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtras(b);
                        startActivity(i);
                    }

                })
                .setObjectToSend(sendparam)
                .build();
        connector.execute();
    }

    private boolean allFilled() {
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }


    private void deleteReview(Map<String, Object> param) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.DELETE_USER_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.deleted_review), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        Bundle b = new Bundle();
                        b.putString("reviewed_user", param.get("reviewed_user").toString());
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
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_USER_REVIEW)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Intent i = new Intent(getActivity(), ProfileActivity.class);
                        Bundle b = new Bundle();
                        b.putString("reviewed_user", Objects.requireNonNull(param.get("reviewed_user")).toString());
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

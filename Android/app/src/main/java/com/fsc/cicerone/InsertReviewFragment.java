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

import com.fsc.cicerone.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class InsertReviewFragment extends Fragment {

    //private static final String ERROR_TAG = "ERROR IN " + InsertReviewFragment.class.getName();
    private JSONObject result;
    private Button submitReview;
    private Button updateReview;
    private Button deleteReview;
    private RatingBar feedbackReview;
    private EditText descriptionReview;
    private TextView message;
    private TextView messageFeedback;
    private TextView messageDescription;
    private JSONObject param;
    private JSONObject sendParam;

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


        param = new JSONObject();
        sendParam = new JSONObject();

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        try {

            param.put("username", currentLoggedUser.getUsername());
            param.put("reviewed_user", Objects.requireNonNull(bundle).getString("reviewed_user"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestReview(param);

        return view;

    }

    private void requestReview(JSONObject parameters) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.REQUEST_FOR_REVIEW,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {

                        if (result.getResult()) {
                            message.setVisibility(View.GONE);
                            checkReview(parameters);

                            submitReview.setOnClickListener(v -> {
                                if (allFilled()) {
                                    try {
                                        sendParam.put("username", param.getString("username"));
                                        sendParam.put("reviewed_user", param.getString("reviewed_user"));
                                        sendParam.put("description", descriptionReview.getText().toString());
                                        sendParam.put("feedback", (int) feedbackReview.getRating());
                                        Log.e("username:", sendParam.getString("username"));
                                        Log.e("reviewed_user:", sendParam.getString("reviewed_user"));
                                        Log.e("description:", sendParam.getString("description"));
                                        Log.e("feedback:", sendParam.getString("feedback"));

                                        submitReview(sendParam);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else
                                    Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

                            });
                            deleteReview.setOnClickListener(v -> deleteReview(param));
                            updateReview.setOnClickListener(v -> {
                                if (allFilled()) {
                                    try {
                                        sendParam.put("username", param.getString("username"));
                                        sendParam.put("reviewed_user", param.getString("reviewed_user"));
                                        sendParam.put("description", descriptionReview.getText().toString());
                                        sendParam.put("feedback", (int) feedbackReview.getRating());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

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

                    }
                },
                parameters);
        connector.execute();
    }

    private void checkReview(JSONObject parameters) {
        // TODO: Add review class
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_USER_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                Log.e("lunghezza", String.valueOf(jsonArray.length()));
                message.setVisibility(View.GONE);
                if (jsonArray.length() > 0) {
                    result = jsonArray.getJSONObject(0);
                    submitReview.setVisibility(View.GONE);
                    updateReview.setVisibility(View.VISIBLE);
                    deleteReview.setVisibility(View.VISIBLE);
                    descriptionReview.setText(result.getString("description"));
                    feedbackReview.setRating(Float.parseFloat(result.getString("feedback")));
                } else {
                    feedbackReview.setRating(0);
                    descriptionReview.setText("");
                    submitReview.setVisibility(View.VISIBLE);
                    updateReview.setVisibility(View.GONE);
                    deleteReview.setVisibility(View.GONE);
                }
            }
        }, parameters);
        connector.execute();
    }

    private void submitReview(JSONObject sendparam) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.INSERT_USER_REVIEW,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        Log.e("p", result.toJSONObject().toString());
                        if (result.getResult()) {
                            Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.added_review), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), ProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("reviewed_user", sendparam.getString("reviewed_user"));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtras(b);
                            startActivity(i);
                        }

                    }
                },
                sendparam);
        connector.execute();
    }

    private boolean allFilled() {
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }


    private void deleteReview(JSONObject param) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.DELETE_USER_REVIEW,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        Log.e("p", result.toJSONObject().toString());
                        if (result.getResult()) {
                            Toast.makeText(getActivity(), InsertReviewFragment.this.getString(R.string.deleted_review), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getActivity(), ProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("reviewed_user", param.getString("reviewed_user"));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtras(b);
                            startActivity(i);
                        }

                    }
                },
                param);
        connector.execute();
    }

    private void updateReview(JSONObject param) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.UPDATE_USER_REVIEW,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        Log.e("p", result.toJSONObject().toString());
                        if (result.getResult()) {
                            Intent i = new Intent(getActivity(), ProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("reviewed_user", param.getString("reviewed_user"));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.putExtras(b);
                            startActivity(i);
                        }

                    }
                },
                param);
        connector.execute();
    }

}

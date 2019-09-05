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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class InsertItineraryReviewFragment extends Fragment {

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();
    private  JSONObject result;
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
    public InsertItineraryReviewFragment() {
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


         param = new JSONObject();

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        try {

            param.put("username",currentLoggedUser.getUsername());
            param.put("booked_itinerary",Objects.requireNonNull(bundle).getString("reviewed_itinerary"));
            param.put("itinerary",bundle.getString("itinerary"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestReview(param);

            return view;

    }

    private void requestReview (JSONObject parameters){
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_RESERVATION, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                sendParam = new JSONObject();

                if(jsonArray.length()>0)
                {
                    message.setVisibility(View.GONE);
                    parameters.put("reviewed_itinerary", parameters.getString("booked_itinerary"));
                    checkReview(parameters);

                    submitReview.setOnClickListener(v -> {
                        if (allFilled()) {
                            try {
                                sendParam.put("itinerary",param.getString("itinerary"));
                                sendParam.put("username", param.getString("username"));
                                sendParam.put("reviewed_itinerary", param.getString("reviewed_itinerary"));
                                sendParam.put("description", descriptionReview.getText().toString());
                                sendParam.put("feedback", (int) feedbackReview.getRating());
                                sendParam.put("itinerary",parameters.getString("itinerary"));
                                Log.e("username:", sendParam.getString("username"));

                                submitReview(sendParam);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else
                            Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

                    });
                    deleteReview.setOnClickListener(v -> {

                        try {
                            sendParam.put("username", parameters.getString("username"));
                            sendParam.put("reviewed_itinerary",parameters.getString("reviewed_itinerary"));
                            sendParam.put("itinerary",parameters.getString("itinerary"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        deleteReview(sendParam);

                    });
                    updateReview.setOnClickListener(v -> {
                        if (allFilled()) {
                            try {
                                sendParam.put("username", parameters.getString("username"));
                                sendParam.put("reviewed_itinerary", parameters.getString("reviewed_itinerary"));
                                sendParam.put("description", descriptionReview.getText().toString());
                                sendParam.put("feedback", (int) feedbackReview.getRating());
                                sendParam.put("itinerary",parameters.getString("itinerary"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            updateReview(sendParam);
                        } else
                            Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.error_fields_empty), Toast.LENGTH_SHORT).show();

                    });
                }
                else
                {
                    feedbackReview.setVisibility(View.GONE);
                    descriptionReview.setVisibility(View.GONE);
                    submitReview.setVisibility(View.GONE);
                    deleteReview.setVisibility(View.GONE);
                    updateReview.setVisibility(View.GONE);
                    messageFeedback.setVisibility(View.GONE);
                    messageDescription.setVisibility(View.GONE);
                }

            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

    private void checkReview (JSONObject parameters) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_ITINERARY_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                message.setVisibility(View.GONE);
                if( jsonArray.length() > 0)
                {
                    result = jsonArray.getJSONObject(0);
                    submitReview.setVisibility(View.GONE);
                    updateReview.setVisibility(View.VISIBLE);
                    deleteReview.setVisibility(View.VISIBLE);
                    descriptionReview.setText(result.getString("description"));
                    feedbackReview.setRating(Float.parseFloat(result.getString("feedback")));
                }
                else
                {
                    feedbackReview.setRating(0);
                    descriptionReview.setText("");
                    submitReview.setVisibility(View.VISIBLE);
                    updateReview.setVisibility(View.GONE);
                    deleteReview.setVisibility(View.GONE);
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

    private void submitReview(JSONObject sendParam) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.INSERT_ITINERARY_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);

                if (object.getBoolean("result")) {
                    Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.added_review), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), ItineraryReviewFragment.class);
                    Bundle b = new Bundle();
                    b.putString("reviewed_itinerary",sendParam.getString("reviewed_itinerary"));
                    b.putString("itinerary",sendParam.getString("itinerary"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtras(b);
                    startActivity(i);
                }

            }
        });
        connector.setObjectToSend(sendParam);
        connector.execute();
    }

    private boolean allFilled (){
        return !descriptionReview.getText().toString().equals("") && feedbackReview.getRating() > 0;
    }


    private void deleteReview(JSONObject param) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.DELETE_ITINERARY_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);

                if (object.getBoolean("result")) {
                    Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.deleted_review), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), ItineraryReviewFragment.class);
                    Bundle b = new Bundle();
                    b.putString("reviewed_itinerary",param.getString("reviewed_itinerary"));
                    b.putString("itinerary",param.getString("itinerary"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtras(b);
                    startActivity(i);
                }

            }
        });
        connector.setObjectToSend(param);
        connector.execute();
    }

    private void updateReview(JSONObject param) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.UPDATE_ITINERARY_REVIEW, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);

                Log.e("p", object.toString());
                if (object.getBoolean("result")) {
                    Toast.makeText(getActivity(), InsertItineraryReviewFragment.this.getString(R.string.updated_review), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getActivity(), ItineraryReviewFragment.class);
                    Bundle b = new Bundle();
                    b.putString("reviewed_itinerary",param.getString("reviewed_itinerary"));
                    b.putString("itinerary",param.getString("itinerary"));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtras(b);
                    startActivity(i);
                }

            }
        });
        connector.setObjectToSend(param);
        connector.execute();
    }

}

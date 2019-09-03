package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class ItineraryFragment extends Fragment {

    Adapter adapter;
    Button newItinerary;

    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();

    /**
     * Empty constructor
     */
    public ItineraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_itinerary_fragment, container, false);

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        newItinerary = view.findViewById(R.id.newItinerary);


        final JSONObject parameters = currentLoggedUser.getCredentials();
        parameters.remove("password");
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        newItinerary.setOnClickListener(v -> {
            Intent i = new Intent().setClass(getActivity(), ItineraryCreation.class);
            startActivity(i);
        });

        return view;
    }

    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                progressBar.setVisibility(View.GONE);
                adapter = new Adapter(getActivity(), jsonArray, 1);
                recyclerView.setAdapter(adapter);
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

}

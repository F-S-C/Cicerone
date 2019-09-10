package com.fsc.cicerone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class ItineraryFragment extends Fragment {

    private Activity context;
    Adapter adapter;
    ReservationAdapter adapter2;
    Button newItinerary;
    Button participations;
    Button myItineraries;
    RecyclerView participationList;
    RecyclerView madeItineraries;
    TextView message;

    private static final String ERROR_TAG = "ERROR IN " + ItineraryFragment.class.getName();

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
        context = Objects.requireNonNull(getActivity());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        newItinerary = view.findViewById(R.id.newItinerary);
        participations = view.findViewById(R.id.partecipations);
        myItineraries = view.findViewById(R.id.myitineraries);
        madeItineraries = view.findViewById(R.id.itinerary_list);
        participationList = view.findViewById(R.id.itinerary_list);
        message = view.findViewById(R.id.noItineraries);



        final JSONObject parameters = currentLoggedUser.getCredentials();
        parameters.remove("password");
        if(currentLoggedUser.getUserType() == UserType.CICERONE)
        {
            participations.setVisibility(View.VISIBLE);
            myItineraries.setVisibility(View.VISIBLE);

            // set up the RecyclerView for Cicerone's itineraries
            madeItineraries.setLayoutManager(new LinearLayoutManager(getActivity()));
            madeItineraries.addItemDecoration(new DividerItemDecoration(madeItineraries.getContext(), DividerItemDecoration.VERTICAL));
        }


        // Set up the RecyclerView for Globetrotter's participations
        participationList.setLayoutManager(new LinearLayoutManager(getActivity()));
        participationList.addItemDecoration(new DividerItemDecoration(participationList.getContext(), DividerItemDecoration.VERTICAL));
        getParticipations(view,parameters,participationList);


        myItineraries.setOnClickListener(v -> {
            //disable button (Material Style)
            participations.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            participations.setTextColor(ContextCompat.getColor(context, participations.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            //enable button (Outlined Style)
            myItineraries.setBackgroundColor(ContextCompat.getColor(context, myItineraries.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            myItineraries.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            getMyItineraries(v,parameters,madeItineraries);
            message.setVisibility(View.GONE);
            newItinerary.setVisibility(View.VISIBLE);

        });

        participations.setOnClickListener(v -> {
            //disable button (Material Style)
            myItineraries.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            myItineraries.setTextColor(ContextCompat.getColor(context, myItineraries.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            //enable button (Outlined Style)
            participations.setBackgroundColor(ContextCompat.getColor(context, participationList.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            participations.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            getParticipations(v,parameters,madeItineraries);
            newItinerary.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);


        });

        newItinerary.setOnClickListener(v -> {
            Intent i = new Intent().setClass(getActivity(), ItineraryCreation.class);
            startActivity(i);
        });

        return view;
    }

    private void getMyItineraries(View view, JSONObject parameters, RecyclerView recyclerView) {
        //RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                //progressBar.setVisibility(View.GONE);
                adapter = new Adapter(getActivity(), jsonArray, 1);
                recyclerView.setAdapter(adapter);
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

    private void getParticipations(View view, JSONObject parameters, RecyclerView recyclerView) {
        //RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                //progressBar.setVisibility(View.GONE);
                adapter2 = new ReservationAdapter(getActivity(), jsonArray,R.layout.participation_list);
                recyclerView.setAdapter(adapter2);
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

}

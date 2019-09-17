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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.adapter.ReservationAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details
 * page.
 */
public class ItineraryFragment extends Fragment {

    private Activity context;
    RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter2;
    private Button newItinerary;
    private Button participations;
    private Button myItineraries;
    private RecyclerView itineraryList;
    private TextView message;

    /**
     * Empty constructor
     */
    public ItineraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_itinerary_fragment, container, false);
        context = Objects.requireNonNull(getActivity());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        newItinerary = view.findViewById(R.id.newItinerary);
        participations = view.findViewById(R.id.partecipations);
        myItineraries = view.findViewById(R.id.myitineraries);
        itineraryList = view.findViewById(R.id.itinerary_list);
        // itineraryList = view.findViewById(R.id.itinerary_list);
        message = view.findViewById(R.id.noItineraries);

        final Map<String, Object> parameters = SendInPostConnector
                .paramsFromJSONObject(currentLoggedUser.getCredentials());
        parameters.remove("password");
        if (currentLoggedUser.getUserType() == UserType.CICERONE) {
            participations.setVisibility(View.VISIBLE);
            myItineraries.setVisibility(View.VISIBLE);
        }

        // Set up the RecyclerView for Globetrotter's participations
        itineraryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        itineraryList.addItemDecoration(
                new DividerItemDecoration(itineraryList.getContext(), DividerItemDecoration.VERTICAL));
        getParticipations(parameters, itineraryList);

        myItineraries.setOnClickListener(v -> {
            // disable button (Material Style)
            participations.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            participations.setTextColor(ContextCompat.getColor(context,
                    participations.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            // enable button (Outlined Style)
            myItineraries.setBackgroundColor(ContextCompat.getColor(context,
                    myItineraries.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            myItineraries.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            getMyItineraries(parameters, itineraryList);
            message.setVisibility(View.GONE);
            newItinerary.setVisibility(View.VISIBLE);

        });

        participations.setOnClickListener(v -> {
            // disable button (Material Style)
            myItineraries.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            myItineraries.setTextColor(ContextCompat.getColor(context,
                    myItineraries.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            // enable button (Outlined Style)
            participations.setBackgroundColor(ContextCompat.getColor(context,
                    itineraryList.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            participations.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            getParticipations(parameters, itineraryList);
            newItinerary.setVisibility(View.GONE);

        });

        newItinerary.setOnClickListener(v -> {
            Intent i = new Intent().setClass(getActivity(), ItineraryCreation.class);
            startActivity(i);
        });

        return view;
    }

    private void getMyItineraries(Map<String, Object> parameters, RecyclerView recyclerView) {
        // TODO: Test and check if the new adapter works as expected.
        // RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector<Itinerary> connector = new SendInPostConnector<>(ConnectorConstants.REQUEST_ITINERARY,
                BusinessEntityBuilder.getFactory(Itinerary.class),
                new DatabaseConnector.CallbackInterface<Itinerary>() {
                    @Override
                    public void onStartConnection() {
                        // progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEndConnection(List<Itinerary> jsonArray) {
                        // progressBar.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        while (recyclerView.getItemDecorationCount() > 0) {
                            recyclerView.removeItemDecorationAt(0);
                        }
                        adapter = new ItineraryAdapter(getActivity(), jsonArray);
                        recyclerView.setAdapter(adapter);
                    }
                }, parameters);
        connector.execute();
    }

    private void getParticipations(Map<String, Object> parameters, RecyclerView recyclerView) {
        // RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector<Reservation> connector = new SendInPostConnector<>(
                ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY,
                BusinessEntityBuilder.getFactory(Reservation.class),
                new DatabaseConnector.CallbackInterface<Reservation>() {
                    @Override
                    public void onStartConnection() {
                        // progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEndConnection(List<Reservation> list) {
                        // progressBar.setVisibility(View.GONE);
                        if (!list.isEmpty())
                            message.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                                DividerItemDecoration.VERTICAL));
                        adapter2 = new ReservationAdapter(getActivity(), list, R.layout.participation_list);
                        recyclerView.setAdapter(adapter2);
                    }
                }, parameters);
        connector.execute();
    }

}

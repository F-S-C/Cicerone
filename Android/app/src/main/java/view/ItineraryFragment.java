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

package view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.adapter.ReservationAdapter;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class that contains the elements of the TAB Itinerary on the account details
 * page.
 */
public class ItineraryFragment extends Fragment implements Refreshable {

    private Activity context;
    RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter2;
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

        Button participationsButton = view.findViewById(R.id.partecipations);
        Button myItinerariesButton = view.findViewById(R.id.myitineraries);
        RecyclerView itineraryList = view.findViewById(R.id.itinerary_list);
        message = view.findViewById(R.id.noItineraries);

        final Map<String, Object> parameters = SendInPostConnector
                .paramsFromObject(currentLoggedUser.getCredentials());
        parameters.remove("password");
        if (currentLoggedUser.getUserType() == UserType.CICERONE) {
            participationsButton.setVisibility(View.VISIBLE);
            myItinerariesButton.setVisibility(View.VISIBLE);
        }

        // Set up the RecyclerView for Globetrotter's participations
        itineraryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        itineraryList.addItemDecoration(
                new DividerItemDecoration(itineraryList.getContext(), DividerItemDecoration.VERTICAL));
        getParticipations(parameters, itineraryList);

        myItinerariesButton.setOnClickListener(v -> {
            // disable button (Material Style)
            participationsButton.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            participationsButton.setTextColor(ContextCompat.getColor(context,
                    participationsButton.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            // enable button (Outlined Style)
            myItinerariesButton.setBackgroundColor(ContextCompat.getColor(context,
                    myItinerariesButton.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            myItinerariesButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            getMyItineraries(parameters, itineraryList);
            message.setVisibility(View.GONE);
        });

        participationsButton.setOnClickListener(v -> {
            // disable button (Material Style)
            myItinerariesButton.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            myItinerariesButton.setTextColor(ContextCompat.getColor(context,
                    myItinerariesButton.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            // enable button (Outlined Style)
            participationsButton.setBackgroundColor(ContextCompat.getColor(context,
                    itineraryList.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            participationsButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            getParticipations(parameters, itineraryList);
        });

        return view;
    }

    private void getMyItineraries(Map<String, Object> parameters, RecyclerView recyclerView) {
        SendInPostConnector<Itinerary> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY, BusinessEntityBuilder.getFactory(Itinerary.class))
                .setContext(context)
                .setOnEndConnectionListener(jsonArray -> {
                    message.setVisibility(View.GONE);
                    if (jsonArray.isEmpty()) {
                        message.setText(R.string.no_create_itinerary);
                        message.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        recyclerView.setVisibility(View.VISIBLE);
                        while (recyclerView.getItemDecorationCount() > 0) {
                            recyclerView.removeItemDecorationAt(0);
                        }
                        adapter = new ItineraryAdapter(getActivity(), jsonArray, this);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    private void getParticipations(Map<String, Object> parameters, RecyclerView recyclerView) {
        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnEndConnectionListener(list -> {
                    List<Reservation> filtered = new ArrayList<>(list.size());
                    message.setVisibility(View.GONE);
                    for (Reservation reservation : list) {
                        if (reservation.isConfirmed()) {
                            filtered.add(reservation);
                        }
                    }
                    if (filtered.isEmpty()) {
                        message.setText(R.string.no_itineraries);
                        message.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                    }
                    recyclerView.setVisibility(View.VISIBLE);

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                            DividerItemDecoration.VERTICAL));
                    adapter2 = new ReservationAdapter(getActivity(), filtered, ItineraryFragment.this, R.layout.participation_list);
                    recyclerView.setAdapter(adapter2);
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }
}

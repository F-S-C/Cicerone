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

package com.fsc.cicerone.view.user.registered_user;

import android.app.Activity;
import android.content.Intent;
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
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ItineraryManager;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.user.registered_user.cicerone.ItineraryManagement;
import com.fsc.cicerone.view.system.Refreshable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class that contains the elements of the tab "itinerary" on the account details page.
 */
public class ItineraryFragment extends Fragment implements Refreshable {

    private Activity context;
    private TextView message;
    private RecyclerView itineraryList;
    private boolean lastClicked = false;  // If it's false, then Participation is loaded, if it's true, MyItineraries is loaded instead.
    private Map<String, Object> parameters;

    /**
     * Empty constructor
     */
    public ItineraryFragment() {
        // Required empty public constructor
    }

    /**
     * @see androidx.fragment.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_itinerary_fragment, container, false);
        context = Objects.requireNonNull(getActivity());

        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        Button participationsButton = view.findViewById(R.id.partecipations);
        Button myItinerariesButton = view.findViewById(R.id.myitineraries);
        itineraryList = view.findViewById(R.id.itinerary_list);
        message = view.findViewById(R.id.noItineraries);

        parameters = SendInPostConnector
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
        refresh();


        myItinerariesButton.setOnClickListener(v -> {
            // disable button (Material Style)
            lastClicked = true;
            participationsButton.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            participationsButton.setTextColor(ContextCompat.getColor(context,
                    participationsButton.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            // enable button (Outlined Style)
            myItinerariesButton.setBackgroundColor(ContextCompat.getColor(context,
                    myItinerariesButton.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            myItinerariesButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            refresh();
            message.setVisibility(View.GONE);
        });


        participationsButton.setOnClickListener(v -> {
            // disable button (Material Style)
            if (lastClicked)
                lastClicked = false;

            myItinerariesButton.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
            myItinerariesButton.setTextColor(ContextCompat.getColor(context,
                    myItinerariesButton.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            // enable button (Outlined Style)
            participationsButton.setBackgroundColor(ContextCompat.getColor(context,
                    itineraryList.isEnabled() ? R.color.colorPrimary : android.R.color.darker_gray));
            participationsButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            refresh();
        });

        return view;
    }


    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh()
     */
    @Override
    public void refresh() {
        refresh(null);
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh(SwipeRefreshLayout)
     */

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        if (lastClicked) {
            ItineraryManager.requestItinerary(getActivity(), parameters, () -> {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
            }, list -> {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);

                message.setVisibility(View.GONE);
                if (list.isEmpty()) {
                    message.setText(R.string.no_create_itinerary);
                    message.setVisibility(View.VISIBLE);
                    itineraryList.setVisibility(View.GONE);
                } else {
                    itineraryList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    itineraryList.setVisibility(View.VISIBLE);
                    while (itineraryList.getItemDecorationCount() > 0) {
                        itineraryList.removeItemDecorationAt(0);
                    }
                    RecyclerView.Adapter adapter = new ItineraryAdapter(getActivity(), list, this);
                    itineraryList.setAdapter(adapter);
                }
            });
        } else {
            ReservationManager.getListInvestments(context, parameters, () -> {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
            }, list -> {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);

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
                    itineraryList.setVisibility(View.GONE);

                }
                itineraryList.setVisibility(View.VISIBLE);
                itineraryList.setLayoutManager(new LinearLayoutManager(getActivity()));
                itineraryList.addItemDecoration(new DividerItemDecoration(itineraryList.getContext(),
                        DividerItemDecoration.VERTICAL));
                RecyclerView.Adapter adapter2 = new ReservationAdapter(getActivity(), filtered, ItineraryFragment.this, R.layout.participation_list);
                itineraryList.setAdapter(adapter2);
            });
        }
    }

    /**
     * @see androidx.fragment.app.Fragment#onActivityResult(int, int, Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ItineraryManagement.RESULT_ITINERARY_DELETED && resultCode == Activity.RESULT_OK) {
            refresh();
        }
    }
}

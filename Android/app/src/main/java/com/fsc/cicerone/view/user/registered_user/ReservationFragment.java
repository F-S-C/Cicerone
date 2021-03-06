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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ReservationAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.view.system.Refreshable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class that contains the elements of the tab "reservations" on the account details page.
 */
public class ReservationFragment extends Fragment implements Refreshable {

    private Activity context;
    private RecyclerView recyclerView;
    private TextView message;

    private SwipeRefreshLayout swipeRefreshLayout = null;

    /**
     * Empty constructor
     */
    public ReservationFragment() {
        // Required empty public constructor
    }

    /**
     * A Constructor that takes a SwipeRefreshLayout as a parameter.
     *
     * @param swipeRefreshLayout the SwipeRefreshLayout to set.
     */
    public ReservationFragment(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * @see androidx.fragment.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reservation_fragment, container, false);
        context = Objects.requireNonNull(getActivity());

        message = view.findViewById(R.id.noReservation);
        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.reservation_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        refresh(swipeRefreshLayout);

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
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("cicerone", AccountManager.getCurrentLoggedUser().getUsername());

        ReservationManager.getListInvestments(context, parameters, () -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
            message.setVisibility(View.GONE);

        }, list -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            List<Reservation> filtered = new ArrayList<>(list.size());
            for (Reservation reservation : list) {
                if (!reservation.isConfirmed()) {
                    filtered.add(reservation);
                }
            }
            if (!filtered.isEmpty()) {
                ReservationAdapter adapter = new ReservationAdapter(getActivity(), filtered, ReservationFragment.this);
                recyclerView.setAdapter(adapter);
            } else {
                message.setVisibility(View.VISIBLE);
            }
        });
    }
}

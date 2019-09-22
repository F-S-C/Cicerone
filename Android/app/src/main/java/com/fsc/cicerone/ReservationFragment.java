package com.fsc.cicerone;

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

import com.fsc.cicerone.adapter.ReservationAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Reservation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Reservation on the account
 * details page.
 */
public class ReservationFragment extends Fragment implements Refreshable {

    ReservationAdapter adapter;
    private Activity context;
    private RecyclerView recyclerView;
    private TextView message;

    /**
     * Empty constructor
     */
    public ReservationFragment() {
        // Required empty public constructor
    }

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
        requireData();

        return view;
    }

    private void requireData() {
        requireData(null);
    }

    @Override
    public void requireData(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("cicerone", AccountManager.getCurrentLoggedUser().getUsername());

        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(() -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
                    message.setVisibility(View.GONE);
                })
                .setOnEndConnectionListener(list -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    List<Reservation> filtered = new ArrayList<>(list.size());
                    for (Reservation reservation : list) {
                        if (!reservation.isConfirmed()) {
                            filtered.add(reservation);
                        }
                    }
                    if (!filtered.isEmpty()) {
                        adapter = new ReservationAdapter(getActivity(), filtered);
                        recyclerView.setAdapter(adapter);
                    } else {
                        message.setVisibility(View.VISIBLE);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

}

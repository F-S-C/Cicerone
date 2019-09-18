package com.fsc.cicerone;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReservationAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Reservation on the account
 * details page.
 */
public class ReservationFragment extends Fragment {

    ReservationAdapter adapter;
    private Activity context;

    // private static final String ERROR_TAG = "ERROR IN " +
    // ReservationFragment.class.getName();

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
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("cicerone", currentLoggedUser.getUsername());
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.reservation_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        return view;
    }

    private void requireData(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        TextView message = view.findViewById(R.id.noReservation);
        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(() -> {
                    message.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                })
                .setOnEndConnectionListener(list -> {
                    progressBar.setVisibility(View.GONE);
                    if (!list.isEmpty()) {
                        adapter = new ReservationAdapter(getActivity(), list);
                        recyclerView.setAdapter(adapter);
                    } else {
                        message.setVisibility(View.GONE);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

}

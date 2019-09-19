package com.fsc.cicerone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.AdminItineraryGlobetrotterAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
public class GlobetrotterItineraryListFragment extends Fragment {

    AdminItineraryGlobetrotterAdapter adapter;
    private Activity context;
    private static final String ERROR_TAG = "ERROR IN " + GlobetrotterItineraryListFragment.class.getName();

    /**
     * Empty constructor
     */
    public GlobetrotterItineraryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_globetrotter_itinerary_list_fragment, container, false);
        context = Objects.requireNonNull(getActivity());
        Bundle bundle = getArguments();

        try {
            Map<String, Object> parameters = new HashMap<>(1);
            User user = new User(
                    new JSONObject((String) Objects.requireNonNull(Objects.requireNonNull(bundle).get("user"))));
            parameters.put("username", user.getUsername());

            RecyclerView recyclerView = view.findViewById(R.id.globetrotter_itinerary_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(
                    new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            requireData(view, parameters, recyclerView);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
        return view;
    }

    private void requireData(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        TextView message = view.findViewById(R.id.no_itinerary_history);
        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(context)
                .setOnStartConnectionListener(() -> message.setVisibility(View.GONE))
                .setOnEndConnectionListener(list -> {
                    List<Reservation> filteredList = new ArrayList<>(list.size());
                    for (Reservation reservation : list) {
                        if (reservation.isConfirmed())
                            filteredList.add(reservation);
                    }
                    if (!filteredList.isEmpty()) {
                        adapter = new AdminItineraryGlobetrotterAdapter(getActivity(), filteredList);
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

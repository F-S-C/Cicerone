package com.fsc.cicerone;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReservationAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Reservation on the account details page.
 */
public class ReservationFragment extends Fragment {

    ReservationAdapter adapter;
    private Activity context;

    //private static final String ERROR_TAG = "ERROR IN " + ReservationFragment.class.getName();

    /**
     * Empty constructor
     */
    public ReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reservation_fragment, container, false);
        context = Objects.requireNonNull(getActivity());
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();


        final JSONObject parameters = new JSONObject();
        try {
            parameters.put("cicerone", currentLoggedUser.getUsername());
        } catch (JSONException e) {
            Log.e("ERROR_RESERVATION_FRAG", e.getMessage());
        }
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.reservation_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        return view;
    }

    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector<Reservation> connector = new SendInPostConnector<>(
                ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY,
                BusinessEntityBuilder.getFactory(Reservation.class),
                new DatabaseConnector.CallbackInterface<Reservation>() {
                    @Override
                    public void onStartConnection() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEndConnection(List<Reservation> list) {
                        progressBar.setVisibility(View.GONE);
                        if (list.size() > 0) {
                            adapter = new ReservationAdapter(getActivity(), list);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(context, ReservationFragment.this.getString(R.string.no_requests_reservation), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                parameters);
        connector.execute();
    }


}

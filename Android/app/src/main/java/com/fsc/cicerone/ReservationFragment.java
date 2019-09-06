package com.fsc.cicerone;

import android.app.Activity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONObject;

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


        final JSONObject parameters = currentLoggedUser.getCredentials();
        parameters.remove("password");
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.reservation_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        return view;
    }

    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_RESERVATION_JOIN_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                progressBar.setVisibility(View.GONE);
                if(jsonArray.length()>0) {
                    adapter = new ReservationAdapter(getActivity(), jsonArray);
                    recyclerView.setAdapter(adapter);
                }
                else{
                    Toast.makeText(context , ReservationFragment.this.getString(R.string.no_requests_reservation), Toast.LENGTH_SHORT).show();
                }
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }


}

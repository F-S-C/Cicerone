package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Itinerary on the account details page.
 */
public class CiceroneItineraryListFragment extends Fragment {

    AdminItineraryAdapter adapter;

    private static final String ERROR_TAG = "ERROR IN " + CiceroneItineraryListFragment.class.getName();

    /**
     * Empty constructor
     */
    public CiceroneItineraryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cicerone_itinerary_list_fragment, container, false);

        Bundle bundle = getArguments();

        try {
            JSONObject parameters = new JSONObject();
            User user = new User(new JSONObject((String) Objects.requireNonNull(Objects.requireNonNull(bundle).get("user"))));
            parameters.put("username", user.getUsername());

            RecyclerView recyclerView = view.findViewById(R.id.cicerone_itinerary_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            requireData(view, parameters, recyclerView);
        }catch (JSONException e){
            Log.e(ERROR_TAG,e.toString());
        }
        return view;
    }

    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {

            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                adapter = new AdminItineraryAdapter(getActivity(), jsonArray);
                recyclerView.setAdapter(adapter);
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

}
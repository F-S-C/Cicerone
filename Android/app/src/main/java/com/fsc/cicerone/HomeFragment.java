package com.fsc.cicerone;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private WishlistAdapter adapter;
    private JSONObject param;


    private static final String ERROR_TAG = "ERROR IN " + HomeFragment.class.getName();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
         param  = new JSONObject();
        RecyclerView recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        try {
            param.put("null","null");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requireData(view, recyclerView);

        return view;
    }

    private void requireData(View view, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_ACTIVE_ITINERARY, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                progressBar.setVisibility(View.GONE);
                adapter = new WishlistAdapter(getActivity(), jsonArray);

                recyclerView.setAdapter(adapter);
            }
        });
        connector.setObjectToSend(param);
        connector.execute();
    }

}

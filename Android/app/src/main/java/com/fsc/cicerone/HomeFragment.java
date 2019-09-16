package com.fsc.cicerone;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.GetDataConnector;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ItineraryAdapter adapter;
    private Activity context;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = Objects.requireNonNull(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        requireData(view, recyclerView);

        return view;
    }

    private void requireData(View view, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        GetDataConnector<Itinerary> connector = new GetDataConnector<>(
                ConnectorConstants.REQUEST_ACTIVE_ITINERARY,
                BusinessEntityBuilder.getFactory(Itinerary.class),
                new DatabaseConnector.CallbackInterface<Itinerary>() {
                    @Override
                    public void onStartConnection() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEndConnection(List<Itinerary> list) {
                        List<Itinerary> filteredList = new LinkedList<>();
                        for (Itinerary itinerary : list){
                            if(!itinerary.getCicerone().equals(AccountManager.getCurrentLoggedUser()))
                                filteredList.add(itinerary);
                        }
                        progressBar.setVisibility(View.GONE);
                        if (!filteredList.isEmpty()) {
                            adapter = new ItineraryAdapter(getActivity(), filteredList);
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(context, HomeFragment.this.getString(R.string.no_active_itineraries), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        connector.execute();
    }

}

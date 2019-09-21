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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.GetDataConnector;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ItineraryAdapter adapter;
    private Activity context;
    private TextView noActiveItineraries;
    private SwipeRefreshLayout swipeRefreshLayout;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = Objects.requireNonNull(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        noActiveItineraries = view.findViewById(R.id.noActiveItineraries);
        swipeRefreshLayout = view.findViewById(R.id.HomeRoot);
        swipeRefreshLayout.setOnRefreshListener(() -> requireData(view,recyclerView));

        requireData(view, recyclerView);

        return view;
    }

    private void requireData(View view, RecyclerView recyclerView) {
        GetDataConnector<Itinerary> connector = new GetDataConnector.Builder<>(ConnectorConstants.REQUEST_ACTIVE_ITINERARY, BusinessEntityBuilder.getFactory(Itinerary.class))
                .setContext(context)
                .setOnStartConnectionListener(() -> swipeRefreshLayout.setRefreshing(true) )
                .setOnEndConnectionListener(list -> {
                    swipeRefreshLayout.setRefreshing(false);
                    List<Itinerary> filteredList = new LinkedList<>();
                    for (Itinerary itinerary : list) {
                        if (!itinerary.getCicerone().equals(AccountManager.getCurrentLoggedUser()))
                            filteredList.add(itinerary);
                    }
                    adapter = new ItineraryAdapter(getActivity(), filteredList);
                    recyclerView.setAdapter(adapter);
                    if (!filteredList.isEmpty()) {

                        noActiveItineraries.setVisibility(View.GONE);
                    } else {
                                noActiveItineraries.setVisibility(View.VISIBLE);
                    }
                })
                .build();
        connector.execute();
    }

}

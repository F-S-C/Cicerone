package com.fsc.cicerone;

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

import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.ItineraryReview;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Review on the account details page.
 */
public class SelectedItineraryReviewFragment extends Fragment {

    RecyclerView.Adapter adapter;

    /**
     * Empty Constructor
     */
    public SelectedItineraryReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_review_fragment, container, false);
        Bundle bundle = getArguments();
        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("reviewed_itinerary", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("reviewed_itinerary")));
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.review_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        return view;
    }

    private void requireData(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        TextView message = view.findViewById(R.id.noReview);
        SendInPostConnector<ItineraryReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_ITINERARY_REVIEW, BusinessEntityBuilder.getFactory(ItineraryReview.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
                    message.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                })
                .setOnEndConnectionListener(list -> {
                    progressBar.setVisibility(View.GONE);
                    if (!list.isEmpty()) {
                        adapter = new ReviewAdapter(getActivity(), list);
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

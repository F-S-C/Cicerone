package com.fsc.cicerone;

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

import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.UserReview;

import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Review on the account details page.
 */
public class ReviewFragment extends Fragment implements Refreshable {

    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private TextView message;

    /**
     * Empty Constructor
     */
    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_review_fragment, container, false);

        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.review_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        message = view.findViewById(R.id.noReview);
        requireData();

        return view;
    }

    private void requireData() {
        requireData(null);
    }

    @Override
    public void requireData(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("reviewed_user", AccountManager.getCurrentLoggedUser().getUsername());

        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
                    if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
                    message.setVisibility(View.GONE);
                })
                .setOnEndConnectionListener(list -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    if (!list.isEmpty()) {
                        adapter = new ReviewAdapter(getActivity(), list);
                        recyclerView.setAdapter(adapter);
                    } else {
                        message.setVisibility(View.VISIBLE);
                    }
                })
                .setObjectToSend(parameters)
                .build()
                .execute();
    }
}

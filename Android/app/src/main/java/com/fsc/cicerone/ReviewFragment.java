/*
 * Copyright 2019 FSC - Five Students of Computer Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import java.util.HashMap;
import java.util.Map;

import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;

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
        refresh();

        return view;
    }

    @Override
    public void refresh() {
        refresh(null);
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
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

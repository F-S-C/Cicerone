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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.UserReview;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

/**
 * Class that contains the elements of the TAB Review on the account details page.
 */
public class SelectedUserReviewFragment extends Fragment {

    RecyclerView.Adapter adapter;

    /**
     * Empty Constructor
     */
    public SelectedUserReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_review_fragment, container, false);
        Bundle bundle = getArguments();
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("reviewed_user", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("reviewed_user")));
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.review_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        return view;
    }

    private void requireData(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        TextView message = view.findViewById(R.id.noReview);
        SendInPostConnector<UserReview> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_USER_REVIEW, BusinessEntityBuilder.getFactory(UserReview.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
                    message.setVisibility(View.GONE);
                })
                .setOnEndConnectionListener(list -> {
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

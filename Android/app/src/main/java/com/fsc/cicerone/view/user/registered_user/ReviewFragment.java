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

package com.fsc.cicerone.view.user.registered_user;

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

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ReviewAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReviewManager;
import com.fsc.cicerone.view.system.Refreshable;


/**
 * Class that contains the elements of the tab "reviews" on the account details page.
 */
public class ReviewFragment extends Fragment implements Refreshable {

    private RecyclerView recyclerView;
    private TextView message;


    private SwipeRefreshLayout swipeRefreshLayout = null;

    /**
     * Empty Constructor
     */
    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * A Constructor that takes a SwipeRefreshLayout as a parameter.
     *
     * @param swipeRefreshLayout the SwipeRefreshLayout to set.
     */
    public ReviewFragment(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * @see androidx.fragment.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_review_fragment, container, false);
        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.review_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        message = view.findViewById(R.id.noReview);
        refresh(swipeRefreshLayout);

        return view;
    }

    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh()
     */
    @Override
    public void refresh() {
        refresh(null);
    }


    /**
     * @see com.fsc.cicerone.view.system.Refreshable#refresh(SwipeRefreshLayout)
     */
    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        ReviewManager.requestUserReviews((Activity) getContext(), AccountManager.getCurrentLoggedUser(), () -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        }, list -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            if (!list.isEmpty()) {
                RecyclerView.Adapter adapter = new ReviewAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
            } else {
                message.setVisibility(View.VISIBLE);
            }
        });

    }
}

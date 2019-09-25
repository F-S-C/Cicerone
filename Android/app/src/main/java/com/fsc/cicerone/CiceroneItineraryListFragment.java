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

import com.fsc.cicerone.adapter.AdminItineraryAdapter;
import com.fsc.cicerone.manager.ItineraryManager;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Class that contains the elements of the TAB Itinerary on the account details
 * page.
 */
public class CiceroneItineraryListFragment extends Fragment {

    AdminItineraryAdapter adapter;
    /**
     * Empty constructor
     */
    public CiceroneItineraryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cicerone_itinerary_list_fragment, container, false);

        Bundle bundle = getArguments();

        User user = new User(Objects.requireNonNull(bundle).getString("user"));

        RecyclerView recyclerView = view.findViewById(R.id.cicerone_itinerary_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, user, recyclerView);
        return view;
    }

    private void requireData(View view, User user, RecyclerView recyclerView) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("username", user.getUsername());
        TextView message = view.findViewById(R.id.no_created_itinerary);
        ItineraryManager.requestItinerary(getActivity(), parameters, () -> message.setVisibility(View.GONE), list -> {
            if (!list.isEmpty()) {
                adapter = new AdminItineraryAdapter(getActivity(), list);
                recyclerView.setAdapter(adapter);
            } else {
                message.setVisibility(View.VISIBLE);
            }
        });
    }

}

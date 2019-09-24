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

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.UserListAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.GetDataConnector;
import com.fsc.cicerone.app_connector.SendInPostConnector;

public class UsersListFragment extends Fragment implements Refreshable {

    private UserListAdapter adapter;
    private RecyclerView recyclerView;
    private Itinerary itinerary;


    public UsersListFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        Bundle bundle = getArguments();

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.CICERONE) {
            String s = Objects.requireNonNull(bundle).getString("itinerary");
            try {
                itinerary = new Itinerary(new JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getParticipators();
        } else {
            refresh();
        }

        return view;
    }

    @Override
    public void refresh() {
        refresh(null);
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        GetDataConnector<User> connector = new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
                })
                .setOnEndConnectionListener(list -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    adapter = new UserListAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                })
                .build();
        connector.execute();
    }


    public void getParticipators() {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("booked_itinerary", itinerary.getCode());

        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
                })
                .setOnEndConnectionListener(list -> {
                    List<User> participators = new LinkedList<>();
                    for (Reservation reservation : list) {
                        participators.add(reservation.getClient());
                    }
                    adapter = new UserListAdapter(getActivity(), participators);
                    recyclerView.setAdapter(adapter);

                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }
}

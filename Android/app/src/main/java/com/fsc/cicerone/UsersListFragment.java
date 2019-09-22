package com.fsc.cicerone;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import app_connector.ConnectorConstants;
import app_connector.GetDataConnector;
import app_connector.SendInPostConnector;

public class UsersListFragment extends Fragment implements Refreshable {

    private UserListAdapter adapter;
    private RecyclerView recyclerView;
    private Itinerary itinerary;
    Map<String, Object> parameters;


    public UsersListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);
        Bundle bundle = getArguments();

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        if(AccountManager.getCurrentLoggedUser().getUserType() == UserType.CICERONE)
        {
            String s = Objects.requireNonNull(bundle).getString("itinerary");
            try {
                itinerary = new Itinerary(new JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            parameters = new HashMap<>(1);
            parameters.put("booked_itinerary",itinerary.getCode());
            getParticipators(parameters);
        }
        else
        {
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


    public void getParticipators( Map<String, Object> parameters) {
        SendInPostConnector<Reservation> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_RESERVATION, BusinessEntityBuilder.getFactory(Reservation.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> {
        })
                .setOnEndConnectionListener(list -> {
                    List<User> participators = new LinkedList<>();
                    for(Reservation reservation : list )
                    {
                        Log.e("user",list.get(0).toString());

                        participators.add(reservation.getClient());
                    }
                    Log.e("size",String.valueOf(participators.size()));
                    adapter = new UserListAdapter(getActivity(), participators);
                    recyclerView.setAdapter(adapter);

                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }
}

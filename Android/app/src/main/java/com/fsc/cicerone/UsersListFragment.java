package com.fsc.cicerone;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.adapter.UserListAdapter;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.User;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.GetDataConnector;

public class UsersListFragment extends Fragment {

    private UserListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = view.findViewById(R.id.admin_users_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this::requireData);

        requireData();

        return view;
    }

    private void requireData() {
        GetDataConnector<User> connector = new GetDataConnector.Builder<>(ConnectorConstants.REGISTERED_USER, BusinessEntityBuilder.getFactory(User.class))
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> swipeRefreshLayout.setRefreshing(true))
                .setOnEndConnectionListener(list -> {
                    swipeRefreshLayout.setRefreshing(false);
                    adapter = new UserListAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                })
                .build();
        connector.execute();
    }
}

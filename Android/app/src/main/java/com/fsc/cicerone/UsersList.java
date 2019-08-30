package com.fsc.cicerone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.GetDataConnector;
import app_connector.SendInPostConnector;

public class UsersList extends Fragment {

    private UserListAdapter adapter;
    private static final String ERROR_TAG = "ERROR IN " + UsersList.class.getName();
    private Context context;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = Objects.requireNonNull(getActivity());
        View view = inflater.inflate(R.layout.activity_users_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, recyclerView);

        return view;
    }

    private void requireData(View view, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        GetDataConnector connector = new GetDataConnector(ConnectorConstants.REGISTERED_USER, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                progressBar.setVisibility(View.GONE);
                adapter = new UserListAdapter(view.getContext(), jsonArray);
                recyclerView.setAdapter(adapter);
            }
        });
        connector.execute();
    }
}

package com.fsc.cicerone;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class WishlistFragment extends Fragment {

    private WishlistAdapter adapter;
    private TextView numberItineraries;
    private Button clearWishlist;

    private static final String ERROR_TAG = "ERROR IN " + AccountDetails.class.getName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wishlist, container, false);
        numberItineraries = view.findViewById(R.id.numberOfItineraries);
        clearWishlist = view.findViewById(R.id.clearWishlist);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();
        final Dialog deleteDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        Objects.requireNonNull(deleteDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        deleteDialog.setContentView(R.layout.activity_delete_wishlist);
        deleteDialog.setCancelable(true);

        final JSONObject parameters = currentLoggedUser.getCredentials();
        parameters.remove("password");
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        clearWishlist.setOnClickListener(v -> {
            Button noButton = deleteDialog.findViewById(R.id.no_logout_button);
            noButton.setOnClickListener(view1 -> deleteDialog.hide());

            Button yesButton = deleteDialog.findViewById(R.id.yes_logout_button);
            yesButton.setOnClickListener(view1 -> {
                deleteDialog.hide();
                deleteDialog.dismiss();
                clearWish(view, parameters, recyclerView);


            });
            deleteDialog.show();
        });

        return view;
    }

    private void requireData(View view, JSONObject parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REQUEST_WISHLIST, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) {
                progressBar.setVisibility(View.GONE);
                adapter = new WishlistAdapter(getActivity(), jsonArray);
                Log.e("length",String.valueOf(jsonArray.length()));
                numberItineraries.setText(String.format(getString(R.string.wishlist_number), jsonArray.length()));
                if(jsonArray.length() == 0)
                    clearWishlist.setVisibility(View.GONE);

                recyclerView.setAdapter(adapter);
            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

    private void clearWish (View view, JSONObject parameters, RecyclerView recyclerView)
    {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.CLEAR_WISHLIST, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject object = jsonArray.getJSONObject(0);
                Log.e("p", object.toString());
                if (object.getBoolean("result")) {
                    //Intent i = new Intent(getActivity(), MainActivity.class);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Toast.makeText(getActivity(), WishlistFragment.this.getString(R.string.wishlist_deleted), Toast.LENGTH_SHORT).show();
                    //startActivity(i);
                    requireData(view, parameters, recyclerView);
                }

            }
        });
        connector.setObjectToSend(parameters);
        connector.execute();
    }

}
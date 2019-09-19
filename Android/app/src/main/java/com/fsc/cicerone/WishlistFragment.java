package com.fsc.cicerone;

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

import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.Wishlist;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class WishlistFragment extends Fragment {

    private ItineraryAdapter adapter;
    private TextView numberItineraries;
    private Button clearWishlist;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        numberItineraries = view.findViewById(R.id.numberOfItineraries);
        clearWishlist = view.findViewById(R.id.clearWishlist);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("username", currentLoggedUser.getUsername());
        // set up the RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        requireData(view, parameters, recyclerView);

        clearWishlist.setOnClickListener(v -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.confirm_delete_wishlist))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> clearWish(view, parameters, recyclerView))
                .setNegativeButton(getString(R.string.no), null)
                .show());

        return view;
    }

    private void requireData(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        RelativeLayout progressBar = view.findViewById(R.id.progressContainer);
        SendInPostConnector<Wishlist> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_WISHLIST, BusinessEntityBuilder.getFactory(Wishlist.class)) //TODO: Check)
                .setContext(getActivity())
                .setOnStartConnectionListener(() -> progressBar.setVisibility(View.VISIBLE))
                .setOnEndConnectionListener(list -> {
                    progressBar.setVisibility(View.GONE);

                    List<Itinerary> itineraryList = new ArrayList<>(list.size());
                    for (Wishlist item : list) {
                        itineraryList.add(item.getItinerary());
                    }
                    adapter = new ItineraryAdapter(getActivity(), itineraryList);

                    Log.e("length", String.valueOf(list.size()));
                    numberItineraries.setText(String.format(getString(R.string.wishlist_number), list.size()));
                    if (list.isEmpty())
                        clearWishlist.setVisibility(View.GONE);

                    recyclerView.setAdapter(adapter);
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    private void clearWish(View view, Map<String, Object> parameters, RecyclerView recyclerView) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.CLEAR_WISHLIST)
                .setContext(getActivity())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        //Intent i = new Intent(getActivity(), MainActivity.class);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        Toast.makeText(getActivity(), WishlistFragment.this.getString(R.string.wishlist_deleted), Toast.LENGTH_SHORT).show();
                        //startActivity(i);
                        requireData(view, parameters, recyclerView);
                    }
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

}

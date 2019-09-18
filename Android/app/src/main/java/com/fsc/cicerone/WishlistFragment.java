package com.fsc.cicerone;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import app_connector.SendInPostConnector;

public class WishlistFragment extends Fragment {

    private ItineraryAdapter adapter;
    private TextView numberOfItinerariesTextView;
    private Button clearWishlistButton;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        numberOfItinerariesTextView = view.findViewById(R.id.numberOfItineraries);
        clearWishlistButton = view.findViewById(R.id.clearWishlist);
        User currentLoggedUser = AccountManager.getCurrentLoggedUser();

        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("username", currentLoggedUser.getUsername());
        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        swipeRefreshLayout = view.findViewById(R.id.WishlistRoot);
        swipeRefreshLayout.setOnRefreshListener(() -> requireData(parameters));

        requireData(parameters);

        clearWishlistButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.confirm_delete_wishlist))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> clearWish(parameters))
                .setNegativeButton(getString(R.string.no), null)
                .show());

        return view;
    }

    private void requireData(Map<String, Object> parameters) {
        SendInPostConnector<Wishlist> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_WISHLIST, BusinessEntityBuilder.getFactory(Wishlist.class)) //TODO: Check
                .setContext(getContext())
                .setOnStartConnectionListener(() -> swipeRefreshLayout.setRefreshing(true))
                .setOnEndConnectionListener(list -> {
                    swipeRefreshLayout.setRefreshing(false);

                    List<Itinerary> itineraryList = new ArrayList<>(list.size());
                    for (Wishlist item : list) {
                        itineraryList.add(item.getItinerary());
                    }
                    adapter = new ItineraryAdapter(getActivity(), itineraryList);

                    numberOfItinerariesTextView.setText(String.format(getString(R.string.wishlist_number), list.size()));
                    if (list.isEmpty())
                        clearWishlistButton.setVisibility(View.GONE);

                    recyclerView.setAdapter(adapter);
                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    private void clearWish(Map<String, Object> parameters) {
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.CLEAR_WISHLIST)
                .setContext(getContext())
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(getActivity(), WishlistFragment.this.getString(R.string.wishlist_deleted), Toast.LENGTH_SHORT).show();
                        requireData(parameters);
                    }

                })
                .setObjectToSend(parameters)
                .build();
        connector.execute();
    }

    public void forceRefresh() {
        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("username", AccountManager.getCurrentLoggedUser().getUsername());
        // set up the RecyclerView
        requireData(parameters);
    }
}

package com.fsc.cicerone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.fsc.cicerone.manager.WishlistManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Wishlist;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishlistFragment extends Fragment {

    private ItineraryAdapter adapter;
    private TextView numberOfItinerariesTextView;
    private Button clearWishlistButton;
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    public static final int REQUEST_UPDATE_WISHLIST = 1031;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        numberOfItinerariesTextView = view.findViewById(R.id.numberOfItineraries);
        clearWishlistButton = view.findViewById(R.id.clearWishlist);


        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        swipeRefreshLayout = view.findViewById(R.id.WishlistRoot);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        refreshData();

        clearWishlistButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                .setTitle(getString(R.string.are_you_sure))
                .setMessage(getString(R.string.confirm_delete_wishlist))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> clearWish())
                .setNegativeButton(getString(R.string.no), null)
                .show());

        return view;
    }

    void refreshData() {
        WishlistManager.getWishlist(getActivity(), () -> swipeRefreshLayout.setRefreshing(true), list -> {
            swipeRefreshLayout.setRefreshing(false);

            List<Itinerary> itineraryList = new ArrayList<>(list.size());
            for (Wishlist item : list) {
                itineraryList.add(item.getItinerary());
            }
            adapter = new ItineraryAdapter(getActivity(), itineraryList, this);

            numberOfItinerariesTextView.setText(String.format(getString(R.string.wishlist_number), list.size()));
            if (list.isEmpty())
                clearWishlistButton.setVisibility(View.GONE);

            recyclerView.setAdapter(adapter);
        });
    }

    private void clearWish() {
        WishlistManager.clearWishlist(getActivity(), result -> {
            if (result.getResult()) {
                Toast.makeText(getActivity(), WishlistFragment.this.getString(R.string.wishlist_deleted), Toast.LENGTH_SHORT).show();
                refreshData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReportFragment.RESULT_SHOULD_REPORT_BE_RELOADED) {
            if (resultCode == Activity.RESULT_OK)
                refreshData();
        }
    }
}

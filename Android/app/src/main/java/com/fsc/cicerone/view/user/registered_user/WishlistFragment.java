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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.ItineraryAdapter;
import com.fsc.cicerone.manager.WishlistManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Wishlist;
import com.fsc.cicerone.view.system.Refreshable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishlistFragment extends Fragment implements Refreshable {

    private ItineraryAdapter adapter;
    private TextView noItinerariesTextView;
    private RecyclerView recyclerView;


    public static final int REQUEST_UPDATE_WISHLIST = 1031;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        noItinerariesTextView = view.findViewById(R.id.noItineraries);


        // set up the RecyclerView
        recyclerView = view.findViewById(R.id.itinerary_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        refresh();


        return view;
    }

    private void clearWish() {
        if (Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() == 0) {
            Toast.makeText(getActivity(), WishlistFragment.this.getString(R.string.empty_wishlist), Toast.LENGTH_SHORT).show();
        } else {
            WishlistManager.clearWishlist(getActivity(), result -> {
                if (result.getResult()) {
                    Toast.makeText(getActivity(), WishlistFragment.this.getString(R.string.wishlist_deleted), Toast.LENGTH_SHORT).show();
                    refresh();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReportFragment.RESULT_SHOULD_REPORT_BE_RELOADED && resultCode == Activity.RESULT_OK) {
            refresh();
        }
    }

    @Override
    public void refresh(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        WishlistManager.getWishlist(getActivity(), () -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        }, list -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);

            List<Itinerary> itineraryList = new ArrayList<>(list.size());
            noItinerariesTextView.setVisibility(View.GONE);
            for (Wishlist item : list) {
                itineraryList.add(item.getItinerary());
            }
            adapter = new ItineraryAdapter(getActivity(), itineraryList, this);

            if (list.isEmpty()) {
                noItinerariesTextView.setVisibility(View.VISIBLE);
            }


            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.wishlist_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clearWishlist) {
            new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                    .setTitle(getString(R.string.are_you_sure))
                    .setMessage(getString(R.string.confirm_delete_wishlist))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> clearWish())
                    .setNegativeButton(getString(R.string.no), null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

}

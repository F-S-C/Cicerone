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

package com.fsc.cicerone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.view_holder.ItineraryViewHolder;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.user.ItineraryDetails;
import com.fsc.cicerone.view.user.registered_user.cicerone.ItineraryManagement;
import com.fsc.cicerone.view.user.registered_user.WishlistFragment;
import com.fsc.cicerone.view.admin.AdminItineraryDetails;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryViewHolder> {
    private final Activity context;
    private List<Itinerary> mData;
    private LayoutInflater mInflater;
    private Fragment fragment;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of Itineraries objects in the wishlist.
     */
    public ItineraryAdapter(Activity context, List<Itinerary> list, @Nullable Fragment fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public ItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itineraryView = mInflater.inflate(R.layout.itinerary_list, parent, false);
        return new ItineraryViewHolder(itineraryView, context);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull ItineraryViewHolder holder, int position) {
        holder.setBeginningDate(mData.get(position).getBeginningDate());
        holder.setEndingDate(mData.get(position).getEndingDate());
        holder.setItineraryTitle(mData.get(position).getTitle());
        holder.setLocation(mData.get(position).getLocation());
        holder.setPrice(mData.get(position).getFullPrice());
        holder.setImage(mData.get(position).getImageUrl());

        holder.setLanguages(mData.get(position).getLanguages());
        holder.showError(AccountManager.isLogged() && !mData.get(position).isInLanguages(AccountManager.getCurrentLoggedUser().getLanguages()));

        holder.setOnClickListener(v -> {
            Intent i;
            if (mData.get(position).getCicerone().equals(AccountManager.getCurrentLoggedUser())) {
                i = new Intent().setClass(context, ItineraryManagement.class);
            } else {
                if (AccountManager.isLogged() && AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                    i = new Intent(context, AdminItineraryDetails.class);
                } else {
                    i = new Intent(context, ItineraryDetails.class);
                }
            }
            i.putExtra("itinerary", mData.get(position).toJSONObject().toString());
            if (fragment != null) {
                fragment.startActivityForResult(i, WishlistFragment.REQUEST_UPDATE_WISHLIST);
            } else {
                context.startActivityForResult(i, ItineraryManagement.RESULT_ITINERARY_DELETED);
            }
        });
    }

    /**
     * Return the length of the JSON array passed into the ReviewAdapter.
     *
     * @return Length of JSON array.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }
}


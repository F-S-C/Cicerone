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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.view_holder.AdminItineraryViewHolder;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Reservation;

import java.util.List;

/**
 * The adapter useful to show data in the admin part of the application.
 */
public class AdminItineraryAdapter extends RecyclerView.Adapter<AdminItineraryViewHolder> {
    private final Activity context;
    private List<Itinerary> mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of itineraries got from server.
     */
    public AdminItineraryAdapter(Activity context, List<Itinerary> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public AdminItineraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itineraryView = mInflater.inflate(R.layout.itinerary_admin_list, parent, false);
        return new AdminItineraryViewHolder(itineraryView, context);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull AdminItineraryViewHolder holder, int position) {
        holder.setBeginningDate(mData.get(position).getBeginningDate());
        holder.setEndingDate(mData.get(position).getEndingDate());
        holder.setItineraryTitle(mData.get(position).getTitle());
        holder.setItineraryCode(mData.get(position).getCode());
        holder.setLocation(mData.get(position).getLocation());
        setItineraryAvgPrice(mData.get(position), holder);
    }

    /**
     * Return the length of the array shown by the Adapter.
     *
     * @return Length of the array.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Set the average earnings of an itinerary.
     *
     * @param itinerary The itinerary.
     * @param holder    The view holder which shows the average earnings.
     */
    private void setItineraryAvgPrice(Itinerary itinerary, AdminItineraryViewHolder holder) {
        ReservationManager.getReservations(context, itinerary, list -> {
            int count = 0;
            float price = 0;
            for (Reservation reservation : list) {
                if (reservation.isConfirmed()) {
                    price += reservation.getTotal();
                    count++;
                }
            }
            holder.setAverageItineraryPrice((count > 0) ? price / count : 0);
        });
    }
}


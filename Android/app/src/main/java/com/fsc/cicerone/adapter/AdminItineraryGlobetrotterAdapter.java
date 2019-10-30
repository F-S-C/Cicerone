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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.view_holder.AdminItineraryGlobetrotterViewHolder;
import com.fsc.cicerone.model.Reservation;

import java.util.List;

/**
 * An adapter that holds a list of reservations to be used in the admin section of the app.
 */
public class AdminItineraryGlobetrotterAdapter extends RecyclerView.Adapter<AdminItineraryGlobetrotterViewHolder> {
    private Context context;
    private List<Reservation> mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of JSON Objects got from server.
     */
    public AdminItineraryGlobetrotterAdapter(Context context, List<Reservation> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public AdminItineraryGlobetrotterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itineraryView = mInflater.inflate(R.layout.globetrotter_itinerary_admin_list, parent, false);
        return new AdminItineraryGlobetrotterViewHolder(itineraryView, context);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull AdminItineraryGlobetrotterViewHolder holder, int position) {
        holder.setRequestedDate(mData.get(position).getRequestedDate());
        holder.setItineraryTitle(mData.get(position).getItinerary().getTitle());
        holder.setLocation(mData.get(position).getItinerary().getLocation());
        holder.setCicerone(mData.get(position).getItinerary().getCicerone().getUsername());
    }

    /**
     * Return the length of the array passed into the ReviewAdapter.
     *
     * @return Length of the array.
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

}


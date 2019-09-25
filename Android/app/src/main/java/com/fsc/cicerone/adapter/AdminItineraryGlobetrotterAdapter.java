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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.Reservation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * The adapter useful to show data in the admin part of the application.
 */
public class AdminItineraryGlobetrotterAdapter extends RecyclerView.Adapter<AdminItineraryGlobetrotterAdapter.ViewHolder> {

    private List<Reservation> mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of JSON Objects got from server.
     */
    public AdminItineraryGlobetrotterAdapter(Context context, List<Reservation> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itineraryView = mInflater.inflate(R.layout.globetrotter_itinerary_admin_list, parent, false);
        return new ViewHolder(itineraryView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        holder.requestedDate.setText(outputFormat.format(mData.get(position).getRequestedDate()));
        holder.itineraryTitle.setText(mData.get(position).getItinerary().getTitle());
        holder.location.setText(mData.get(position).getItinerary().getLocation());
        holder.itineraryCicerone.setText(mData.get(position).getItinerary().getCicerone().getUsername());
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

    /**
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        //Defining variables of ITINERARY_LIST view
        TextView itineraryTitle;
        TextView itineraryCicerone;
        TextView location;
        TextView requestedDate;

        ViewHolder(View itemView) {
            super(itemView);
            itineraryTitle = itemView.findViewById(R.id.itinerary_title);
            itineraryCicerone = itemView.findViewById(R.id.itinerary_cicerone);
            location = itemView.findViewById(R.id.location);
            requestedDate = itemView.findViewById(R.id.requested);
        }
    }
}


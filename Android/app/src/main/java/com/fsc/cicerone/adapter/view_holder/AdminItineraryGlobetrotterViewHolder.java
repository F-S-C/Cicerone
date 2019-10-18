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

package com.fsc.cicerone.adapter.view_holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fsc.cicerone.R;

import java.util.Date;

/**
 * ViewHolder stores and recycles reports as they are scrolled off screen.
 */
public class AdminItineraryGlobetrotterViewHolder extends AbstractViewHolder {
    private TextView itineraryTitle;
    private TextView itineraryCicerone;
    private TextView location;
    private TextView requestedDate;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public AdminItineraryGlobetrotterViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        itineraryTitle = itemView.findViewById(R.id.itinerary_title);
        itineraryCicerone = itemView.findViewById(R.id.itinerary_cicerone);
        location = itemView.findViewById(R.id.location);
        requestedDate = itemView.findViewById(R.id.requested);
    }

    /**
     * Show the itinerary's title.
     *
     * @param title The itinerary's title.
     */
    public void setItineraryTitle(String title) {
        itineraryTitle.setText(title);
    }

    /**
     * Show the itinerary's location.
     *
     * @param location The location.
     */
    public void setLocation(String location) {
        this.location.setText(location);
    }

    /**
     * Show the itinerary's cicerone's username.
     *
     * @param username The username.
     */
    public void setCicerone(String username) {
        this.itineraryCicerone.setText(username);
    }

    /**
     * Show the reservation's requested date.
     *
     * @param date The requested date.
     */
    public void setRequestedDate(Date date) {
        requestedDate.setText(formatDate(date));
    }
}

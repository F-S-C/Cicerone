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
 * An abstract view holder to be used for itineraries.
 */
public abstract class AbstractItineraryViewHolder extends AbstractViewHolder {
    private TextView itineraryTitle;
    private TextView location;
    private TextView beginning;
    private TextView ending;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public AbstractItineraryViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        itineraryTitle = itemView.findViewById(R.id.itinerary_title);
        location = itemView.findViewById(R.id.location);
        beginning = itemView.findViewById(R.id.beginning);
        ending = itemView.findViewById(R.id.ending);
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
     * Show the itinerary's beginning date.
     *
     * @param date The beginning date.
     */
    public void setBeginningDate(Date date) {
        beginning.setText(formatDate(date));
    }

    /**
     * Show the itinerary's ending date.
     *
     * @param date The ending date.
     */
    public void setEndingDate(Date date) {
        ending.setText(formatDate(date));
    }
}

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

/**
 * ViewHolder stores and recycles reports as they are scrolled off screen.
 */
public class AdminItineraryViewHolder extends AbstractItineraryViewHolder {
    private TextView itineraryNumber;
    private TextView avgItineraryPrice;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractItineraryViewHolder#AbstractItineraryViewHolder(View, Context)
     */
    public AdminItineraryViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        itineraryNumber = itemView.findViewById(R.id.itinerary_cicerone);
        avgItineraryPrice = itemView.findViewById(R.id.avg_itinerary_price);
    }

    /**
     * Show the itinerary's code.
     *
     * @param code The code.
     */
    public void setItineraryCode(int code) {
        itineraryNumber.setText(String.format(getContext().getString(R.string.print_integer_number), code));
    }

    /**
     * Show the average earnings of the itinerary.
     *
     * @param price The average earnings.
     */
    public void setAverageItineraryPrice(float price) {
        avgItineraryPrice.setText(getContext().getString(R.string.itinerary_earn, price));
    }
}

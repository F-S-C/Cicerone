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
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fsc.cicerone.R;

import java.util.Date;

/**
 * A view holder that shows a reservation.
 */
public class ReservationViewHolder extends AbstractViewHolder {
    private TextView itineraryTitle;
    private TextView globetrotter;
    private TextView numberChildren;
    private TextView numberAdults;
    private TextView requestedDate;
    private Button confirmReservationButton;
    private Button declineReservationButton;
    private Button removeParticipationButton;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public ReservationViewHolder(@NonNull View itemView, @NonNull Context context, int layout) {
        super(itemView, context);
        if (layout == R.layout.reservation_list) {
            confirmReservationButton = itemView.findViewById(R.id.reservation_confirm);
            confirmReservationButton.setVisibility(View.GONE);
            declineReservationButton = itemView.findViewById(R.id.reservation_decline);
            declineReservationButton.setVisibility(View.GONE);
        } else {
            removeParticipationButton = itemView.findViewById(R.id.removeParticipation);
            removeParticipationButton.setVisibility(View.GONE);
        }
        itineraryTitle = itemView.findViewById(R.id.itinerary_requested);
        globetrotter = itemView.findViewById(R.id.reservation_globetrotter);
        numberAdults = itemView.findViewById(R.id.nr_adults_requested);
        numberChildren = itemView.findViewById(R.id.nr_children_requested);
        requestedDate = itemView.findViewById(R.id.date_requested);
    }

    /**
     * Show the globetrotter's username.
     *
     * @param username The username.
     */
    public void setGlobetrotter(String username) {
        globetrotter.setText(username);
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
     * Show the reservation's requested date.
     *
     * @param date The requested date.
     */
    public void setRequestedDate(Date date) {
        requestedDate.setText(formatDate(date));
    }

    /**
     * Show the number of adults.
     *
     * @param numberOfAdults The number of adults.
     */
    public void setNumberOfAdults(int numberOfAdults) {
        numberAdults.setText(String.valueOf(numberOfAdults));
    }

    /**
     * Show the number of children.
     *
     * @param numberOfChildren The number of children.
     */
    public void setNumberOfChildren(int numberOfChildren) {
        numberChildren.setText(String.valueOf(numberOfChildren));
    }

    public Button getConfirmReservationButton() {
        return confirmReservationButton;
    }

    public Button getDeclineReservationButton() {
        return declineReservationButton;
    }

    public Button getRemoveParticipationButton() {
        return removeParticipationButton;
    }
}

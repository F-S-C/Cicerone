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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.view_holder.ReservationViewHolder;
import com.fsc.cicerone.manager.ReservationManager;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.view.system.Refreshable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;
import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationViewHolder> {

    private List<Reservation> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ReservationViewHolder previouslyClickedHolder = null;
    private int layout = R.layout.reservation_list;
    private Fragment fragment;

    /**
     * Constructor.
     *
     * @param context  The parent Context.
     * @param list     The array of JSON Objects got from server.
     * @param fragment A Fragment.
     */
    public ReservationAdapter(Context context, List<Reservation> list, @Nullable Fragment fragment) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.fragment = fragment;
    }

    /**
     * Constructor.
     *
     * @param context  The parent Context.
     * @param list     The array of JSON Objects got from server.
     * @param fragment A Fragment.
     * @param layout   The layout for the item.
     */
    public ReservationAdapter(Context context, List<Reservation> list, @Nullable Fragment fragment, int layout) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.mData = list;
        this.fragment = fragment;
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reservationView = mInflater.inflate(layout, parent, false);
        return new ReservationViewHolder(reservationView, context, layout);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        holder.setRequestedDate(mData.get(position).getRequestedDate());
        holder.setItineraryTitle(mData.get(position).getItinerary().getTitle());

        //set TextView based on the layout. If layout is reservation_list, globetrotter must appear, otherwise, the cicerone.
        if (layout == R.layout.reservation_list)
            holder.setGlobetrotter(mData.get(position).getClient().getUsername());
        else
            holder.setGlobetrotter(mData.get(position).getItinerary().getCicerone().getUsername());

        holder.setNumberOfChildren(mData.get(position).getNumberOfChildren());
        holder.setNumberOfAdults(mData.get(position).getNumberOfAdults());

        if (holder.getConfirmReservationButton() != null) {
            holder.getConfirmReservationButton().setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                        ReservationManager.confirmReservation(mData.get(position));
                        removeAt(position);
                        if (fragment instanceof Refreshable) ((Refreshable) fragment).refresh();
                    })
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        }

        if (holder.getDeclineReservationButton() != null) {
            holder.getDeclineReservationButton().setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), ((dialog, which) -> {
                        ReservationManager.refuseReservation(mData.get(position), null);
                        removeAt(position);
                        if (fragment instanceof Refreshable)
                            ((Refreshable) fragment).refresh();
                    }))
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        }

        if (holder.getRemoveParticipationButton() != null) {
            holder.getRemoveParticipationButton().setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), ((dialog, which) -> {
                        ReservationManager.deleteReservation(mData.get(position));
                        removeAt(position);
                        if (fragment instanceof Refreshable)
                            ((Refreshable) fragment).refresh();
                    }))
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        }


        holder.setOnClickListener(v -> {
            if (layout == R.layout.reservation_list) {
                if (previouslyClickedHolder != null) {
                    previouslyClickedHolder.getConfirmReservationButton().setVisibility(View.GONE);
                    previouslyClickedHolder.getDeclineReservationButton().setVisibility(View.GONE);
                }
                if (previouslyClickedHolder != holder) {
                    holder.getConfirmReservationButton().setVisibility(View.VISIBLE);
                    holder.getDeclineReservationButton().setVisibility(View.VISIBLE);
                }
            } else if (layout == R.layout.participation_list) {
                Date today = new Date();
                if (mData.get(position).getConfirmationDate().before(today) && mData.get(position).getItinerary().getReservationDate().after(today)) {
                    if (previouslyClickedHolder != null)
                        previouslyClickedHolder.getRemoveParticipationButton().setVisibility(View.GONE);
                    if (previouslyClickedHolder != holder)
                        holder.getRemoveParticipationButton().setVisibility(View.VISIBLE);
                }
            }
            previouslyClickedHolder = (previouslyClickedHolder != holder) ? holder : null;
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

    /**
     * Remove an item.
     *
     * @param position The position of the item to be removed.
     */
    private void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }


}


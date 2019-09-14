package com.fsc.cicerone.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.Reservation;
import com.fsc.cicerone.ReservationManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private List<Reservation> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private ViewHolder previouslyClickedHolder = null;
    private int layout = R.layout.reservation_list;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of JSON Objects got from server.
     */
    public ReservationAdapter(Context context, List<Reservation> list) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>(list.size());
        for(Reservation reservation : list){
            if (!reservation.isConfirmed()){
                this.mData.add(reservation);
            }
        }
    }


    public ReservationAdapter(Context context, List<Reservation> list, int layout) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.layout = layout;
        this.mData = new ArrayList<>(list.size());
        for(Reservation reservation : list){
            if (reservation.isConfirmed()){
                this.mData.add(reservation);
            }
        }
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View reservationView = mInflater.inflate(layout, parent, false);
        return new ViewHolder(reservationView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);


        holder.requestedDate.setText(outputFormat.format(mData.get(position).getRequestedDate()));
        holder.itineraryTitle.setText(mData.get(position).getItinerary().getTitle());
        holder.globetrotter.setText(mData.get(position).getClient().getUsername());
        holder.numberChildren.setText(String.valueOf(mData.get(position).getNumberOfChildren()));
        holder.numberAdults.setText(String.valueOf(mData.get(position).getNumberOfAdults()));

        if (holder.confirmReservation != null) {
            holder.confirmReservation.setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                        ReservationManager.confirmReservation(mData.get(position));
                        removeAt(position);
                    })
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        }

        if (holder.declineReservation != null) {
            holder.declineReservation.setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), ((dialog, which) -> {
                        ReservationManager.refuseReservation(mData.get(position));
                        removeAt(position);
                    }))
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        }

        if (holder.removeParticipation != null) {
            holder.removeParticipation.setOnClickListener(v -> new MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), ((dialog, which) -> {
                        ReservationManager.refuseReservation(mData.get(position));
                        removeAt(position);
                    }))
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        }


        holder.itemView.setOnClickListener(v -> {
            if (layout == R.layout.reservation_list) {
                if (previouslyClickedHolder != null) {
                    previouslyClickedHolder.confirmReservation.setVisibility(View.GONE);
                    previouslyClickedHolder.declineReservation.setVisibility(View.GONE);
                }
                if (previouslyClickedHolder != holder) {
                    holder.confirmReservation.setVisibility(View.VISIBLE);
                    holder.declineReservation.setVisibility(View.VISIBLE);
                }
            } else if (layout == R.layout.participation_list) {
                if (previouslyClickedHolder != null)
                    previouslyClickedHolder.removeParticipation.setVisibility(View.GONE);


                if (previouslyClickedHolder != holder)
                    holder.removeParticipation.setVisibility(View.VISIBLE);

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
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Defining variables of reservation details view
        TextView itineraryTitle;
        TextView globetrotter;
        TextView numberChildren;
        TextView numberAdults;
        TextView requestedDate;
        Button confirmReservation;
        Button declineReservation;
        Button removeParticipation;


        ViewHolder(View itemView) {
            super(itemView);
            if (layout == R.layout.reservation_list) {
                confirmReservation = itemView.findViewById(R.id.reservation_confirm);
                confirmReservation.setVisibility(View.GONE);
                declineReservation = itemView.findViewById(R.id.reservation_decline);
                declineReservation.setVisibility(View.GONE);
            } else {
                removeParticipation = itemView.findViewById(R.id.removeParticipation);
                removeParticipation.setVisibility(View.GONE);
            }
            itineraryTitle = itemView.findViewById(R.id.itinerary_requested);
            globetrotter = itemView.findViewById(R.id.reservation_globetrotter);
            numberAdults = itemView.findViewById(R.id.nr_adults_requested);
            numberChildren = itemView.findViewById(R.id.nr_children_requested);
            requestedDate = itemView.findViewById(R.id.date_requested);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }

    public void removeAt(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mData.size());
    }


    /**
     * Item Click Listener for parent activity will implement this method to respond to click events.
     */
    interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}


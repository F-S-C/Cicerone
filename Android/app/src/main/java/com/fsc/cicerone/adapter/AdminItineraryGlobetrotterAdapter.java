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

    private static final String ERROR_TAG = "ERROR IN " + AdminItineraryGlobetrotterAdapter.class.getName();

    private List<Reservation> mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context   The parent Context.
     * @param list The array of JSON Objects got from server.
     */
    public AdminItineraryGlobetrotterAdapter(Context context, List<Reservation> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        for(Reservation reservation : this.mData) {
            // The reservation must be shown if and only if it was confirmed.
            if (!reservation.isConfirmed()) {
                this.mData.remove(reservation);
            }
        }
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
            holder.itineraryCicerone.setText(mData.get(position).getItinerary().getUsername());

    }//END onBindViewHolder

    /**
     * Return the length of the JSON array passed into the Adapter.
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Do nothing
        }
    }
}


package com.fsc.cicerone;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The Adapter of the Recycler View for the styles present in the app.
 */
public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private static final String ERROR_TAG = "ERROR IN " + ReservationAdapter.class.getName();

    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private ViewHolder previouslyClickedHolder = null;

    /**
     * Constructor.
     *
     * @param context   The parent Context.
     * @param jsonArray The array of JSON Objects got from server.
     */
    ReservationAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = jsonArray;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View reservationView = mInflater.inflate(R.layout.reservation_list, parent, false);
        return new ViewHolder(reservationView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Reservation reservation = new Reservation.Builder(mData.getJSONObject(position)).build();
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            holder.requestedDate.setText(outputFormat.format(reservation.getRequestedDate()));
            holder.itineraryTitle.setText(reservation.getItinerary().getTitle());
            holder.globetrotter.setText(reservation.getClient().getUsername());
            holder.numberChildren.setText(String.valueOf(reservation.getNumberOfChildren()));
            holder.numberAdults.setText(String.valueOf(reservation.getNumberOfAdults()));

            holder.confirmReservation.setOnClickListener(v -> new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.are_you_sure))
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> ReservationManager.confirmReservation(reservation))
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        holder.itemView.setOnClickListener(v -> {
            if (previouslyClickedHolder != null) {
                previouslyClickedHolder.confirmReservation.setVisibility(View.GONE);
                previouslyClickedHolder.declineReservation.setVisibility(View.GONE);
            }
            if (previouslyClickedHolder != holder) {
                holder.confirmReservation.setVisibility(View.VISIBLE);
                holder.declineReservation.setVisibility(View.VISIBLE);
            }
            previouslyClickedHolder = (previouslyClickedHolder != holder) ? holder : null;
        });

    }

    /**
     * Return the length of the JSON array passed into the Adapter.
     *
     * @return Length of JSON array.
     */
    @Override
    public int getItemCount() {
        return mData.length();
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


        ViewHolder(View itemView) {
            super(itemView);
            confirmReservation = itemView.findViewById(R.id.reservation_confirm);
            confirmReservation.setVisibility(View.GONE);
            declineReservation = itemView.findViewById(R.id.reservation_decline);
            declineReservation.setVisibility(View.GONE);
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


    /**
     * Item Click Listener for parent activity will implement this method to respond to click events.
     */
    interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}


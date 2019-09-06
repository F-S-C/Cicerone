package com.fsc.cicerone;

import android.annotation.SuppressLint;
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
import java.text.ParseException;
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

    /**
     * Constructor.
     *
     * @param context    The parent Context.
     * @param jsonArray  The array of JSON Objects got from server.
     */
    ReservationAdapter(Context context, JSONArray jsonArray) {
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
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            holder.requestedDate.setText(outputFormat.format(inputDate.parse(mData.getJSONObject(position).getString("requested_date"))));
            holder.itineraryTitle.setText(mData.getJSONObject(position).getString("title"));
            holder.globetrotter.setText(mData.getJSONObject(position).getString("username"));
            Integer numberChildren = mData.getJSONObject(position).getInt("number_of_children");
            holder.numberChildren.setText(String.format("%d",numberChildren));
            Integer numberAdults = mData.getJSONObject(position).getInt("number_of_adults");
            holder.numberAdults.setText(String.format("%d",numberAdults));
        } catch (JSONException | ParseException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        holder.itemView.setOnClickListener(v -> {
           holder.confirmReservation.setVisibility(View.VISIBLE);
           holder.declineReservation.setVisibility(View.VISIBLE);
        });

    }//END onBindViewHolder

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


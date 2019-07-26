package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The Adapter of the Recycler View for the styles present in the app.
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private static final String ERROR_TAG = "ERROR IN " + WishlistAdapter.class.getName();

    private final Context context;
    private JSONArray mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;




    /**
     * Constructor.
     *
     * @param context    The parent Context.
     * @param jsonArray  The array of JSON Objects got from server.
     */
    WishlistAdapter(Context context, JSONArray jsonArray) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = jsonArray;;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itineraryView = mInflater.inflate(R.layout.itinerary_list, parent, false);
                return new ViewHolder(itineraryView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                try {
                    String title = mData.getJSONObject(position).getString("title");
                    Integer itineraryNumber = mData.getJSONObject(position).getInt("itinerary_code");
                    String location = mData.getJSONObject(position).getString("location");

                    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date date = inputFormat.parse(mData.getJSONObject(position).getString("beginning_date"));
                    holder.beginning.setText(outputFormat.format(date));
                    date = inputFormat.parse(mData.getJSONObject(position).getString("ending_date"));
                    holder.ending.setText(outputFormat.format(date));

                    holder.itineraryTitle.setText(title);
                    holder.itineraryNumber.setText(String.format(context.getString(R.string.print_integer_number), itineraryNumber));
                    holder.location.setText(location);
                } catch (JSONException | ParseException e) {
                    Log.e(ERROR_TAG, e.getMessage());
                }

                holder.itemView.setOnClickListener(v -> {
                    Intent i = new Intent().setClass(v.getContext(), ItineraryDetails.class);
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("itinerary_code", mData.getJSONObject(position).getString("itinerary_code"));
                        i.putExtras(bundle);
                        v.getContext().startActivity(i);
                    } catch (JSONException e) {
                        Log.e(ERROR_TAG, e.toString());
                    }
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

        //Defining variables of ITINERARY_LIST view
        TextView itineraryTitle;
        TextView itineraryNumber;
        TextView location;
        TextView beginning;
        TextView ending;


        ViewHolder(View itemView) {
            super(itemView);
            itineraryTitle = itemView.findViewById(R.id.itinerary_title);
            itineraryNumber = itemView.findViewById(R.id.itinerary_number);
            location = itemView.findViewById(R.id.location);
            beginning = itemView.findViewById(R.id.beginning);
            ending = itemView.findViewById(R.id.ending);
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


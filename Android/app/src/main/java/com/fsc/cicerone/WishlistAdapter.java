package com.fsc.cicerone;

import android.content.Context;
import android.content.Intent;
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
import java.text.SimpleDateFormat;
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
        this.mData = jsonArray;
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
        Itinerary itineraryList[] = new Itinerary[mData.length()];


        try {
                    itineraryList[position] = new Itinerary(mData.getJSONObject(position));
                    String title = itineraryList[position].getTitle();
                    Integer itineraryNumber = itineraryList[position].getCode();
                    String location = itineraryList[position].getLocation();

                    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    holder.beginning.setText(outputFormat.format(itineraryList[position].getBeginningDate()));
                    holder.ending.setText(outputFormat.format(itineraryList[position].getEndingDate()));
                    holder.itineraryTitle.setText(title);
                    holder.itineraryNumber.setText(String.format(context.getString(R.string.print_integer_number), itineraryNumber));
                    holder.location.setText(location);
                } catch (JSONException e) {
                    Log.e(ERROR_TAG, e.getMessage());
                }

                holder.itemView.setOnClickListener(v -> {
                    Intent i;
                    if(AccountManager.getCurrentLoggedUser().getUserType()==UserType.ADMIN){
                        i = new Intent().setClass(v.getContext(), AdminItineraryDetails.class);
                    }
                    else{
                        i = new Intent().setClass(v.getContext(), ItineraryDetails.class);
                    }
                    i.putExtra("itinerary",itineraryList[position].toJSONObject().toString());
                    v.getContext().startActivity(i);
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


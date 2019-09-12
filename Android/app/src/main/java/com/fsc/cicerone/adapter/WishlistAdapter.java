package com.fsc.cicerone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.AccountManager;
import com.fsc.cicerone.AdminItineraryDetails;
import com.fsc.cicerone.ItineraryDetails;
import com.fsc.cicerone.R;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.UserType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * The Adapter of the Recycler View for the styles present in the app.
 */
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private static final String ERROR_TAG = "ERROR IN " + WishlistAdapter.class.getName();

    private final Context context;
    private List<Itinerary> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    /**
     * Constructor.
     *
     * @param context   The parent Context.
     * @param jsonArray The array of JSON Objects got from server.
     */
    public WishlistAdapter(Context context, List<Itinerary> jsonArray) {
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
        String title = mData.get(position).getTitle();
        Integer itineraryNumber = mData.get(position).getCode();
        String location = mData.get(position).getLocation();

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        holder.beginning.setText(outputFormat.format(mData.get(position).getBeginningDate()));
        holder.ending.setText(outputFormat.format(mData.get(position).getEndingDate()));
        holder.itineraryTitle.setText(title);
        holder.itineraryNumber.setText(String.format(context.getString(R.string.print_integer_number), itineraryNumber));
        holder.location.setText(location);

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                i = new Intent().setClass(v.getContext(), AdminItineraryDetails.class);
            } else {
                i = new Intent().setClass(v.getContext(), ItineraryDetails.class);
            }
            i.putExtra("itinerary", mData.get(position).toJSONObject().toString());
            v.getContext().startActivity(i);
        });

    }

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


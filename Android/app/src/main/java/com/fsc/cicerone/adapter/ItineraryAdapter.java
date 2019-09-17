package com.fsc.cicerone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.AdminItineraryDetails;
import com.fsc.cicerone.ItineraryDetails;
import com.fsc.cicerone.ItineraryManagement;
import com.fsc.cicerone.R;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.UserType;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private final Context context;
    private List<Itinerary> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of Itineraries objects in the wishlist.
     */
    public ItineraryAdapter(Context context, List<Itinerary> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
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
        //Integer itineraryNumber = mData.get(position).getCode();
        String location = mData.get(position).getLocation();

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        holder.beginning.setText(outputFormat.format(mData.get(position).getBeginningDate()));
//        holder.ending.setText(outputFormat.format(mData.get(position).getEndingDate()));
        holder.itineraryTitle.setText(title);
        //holder.itineraryNumber.setText(String.format(context.getString(R.string.print_integer_number), itineraryNumber));
        holder.location.setText(location);
        holder.priceTagTextView.setText(String.format(context.getString(R.string.price_value), mData.get(position).getFullPrice()));
        Picasso.get().load(mData.get(position).getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            if (mData.get(position).getCicerone().equals(AccountManager.getCurrentLoggedUser())) {
                i = new Intent().setClass(v.getContext(), ItineraryManagement.class);
            } else {
                if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                    i = new Intent().setClass(v.getContext(), AdminItineraryDetails.class);
                } else {
                    i = new Intent().setClass(v.getContext(), ItineraryDetails.class);
                }
            }
            i.putExtra("itinerary", mData.get(position).toJSONObject().toString());
            v.getContext().startActivity(i);
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { //TODO: Add to class diagram

        //Defining variables of ITINERARY_LIST view
        TextView itineraryTitle;
        TextView itineraryNumber;
        TextView location;
        TextView beginning;
        TextView ending;
        ImageView imageView;
        TextView priceTagTextView;


        ViewHolder(View itemView) {
            super(itemView);
            itineraryTitle = itemView.findViewById(R.id.itinerary_title);
            //itineraryNumber = itemView.findViewById(R.id.itinerary_number);
            location = itemView.findViewById(R.id.location);
            beginning = itemView.findViewById(R.id.beginning);
            ending = itemView.findViewById(R.id.ending);
            imageView = itemView.findViewById(R.id.media_image);
            priceTagTextView = itemView.findViewById(R.id.itinerary_price_badge);
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
    interface ItemClickListener { //TODO: Remove?
        void onItemClick(View view, int position);
    }

}


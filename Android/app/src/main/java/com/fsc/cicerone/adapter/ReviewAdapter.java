package com.fsc.cicerone.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.ProfileActivity;
import com.fsc.cicerone.R;
import com.fsc.cicerone.model.Review;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String ERROR_TAG = "ERROR IN " + ReviewAdapter.class.getName();

    private final Context context;
    private List<? extends Review> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The list of reviews to be displayed.
     */
    public ReviewAdapter(Context context, List<? extends Review> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View reviewsView = mInflater.inflate(R.layout.review_list, parent, false);
        return new ViewHolder(reviewsView);

    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingBar.setRating(mData.get(position).getFeedback());
        holder.reviewerUsername.setText(mData.get(position).getAuthor().getUsername());
        holder.reviewDescription.setText(mData.get(position).getDescription());
        //TODO Reviewer profile image

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent().setClass(v.getContext(), ProfileActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("reviewed_user", mData.get(position).getAuthor().getUsername());
            i.putExtras(bundle);
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView reviewerUsername;
        TextView reviewDescription;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);

            reviewerUsername = itemView.findViewById(R.id.reviewer_username);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewDescription = itemView.findViewById(R.id.review_description);
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
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
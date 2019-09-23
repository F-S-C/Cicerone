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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.ItineraryDetails;
import com.fsc.cicerone.ProfileActivity;
import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Review;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<? extends Review> mData;
    private Context context;
    private LayoutInflater mInflater;

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
            if (!mData.get(position).getAuthor().getUsername().equals("deleted_user") || !mData.get(position).getAuthor().getUsername().equals(AccountManager.getCurrentLoggedUser().getUsername()) || !AccountManager.isLogged()) {

                    Intent i = new Intent().setClass(v.getContext(), ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("reviewed_user", mData.get(position).getAuthor().toString());
                    i.putExtras(bundle);
                    v.getContext().startActivity(i);

            } else {
                Toast.makeText(context, context.getString(R.string.warning_deleted_user), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Return the length of the JSON array passed into the ReviewAdapter.
     *
     * @return Length of JSON array.
     */
    @Override
    public int
    getItemCount() {
        return mData.size();
    }

    /**
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerUsername;
        TextView reviewDescription;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);

            reviewerUsername = itemView.findViewById(R.id.reviewer_username);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewDescription = itemView.findViewById(R.id.review_description);
        }
    }
}
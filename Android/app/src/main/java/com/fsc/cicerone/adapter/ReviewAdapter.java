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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.view_holder.ReviewViewHolder;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Review;
import com.fsc.cicerone.view.user.ProfileActivity;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {

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
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reviewsView = mInflater.inflate(R.layout.review_list, parent, false);
        return new ReviewViewHolder(reviewsView, context);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.setRating(mData.get(position).getFeedback());
        holder.setReviewerUsername(mData.get(position).getAuthor().getUsername());
        holder.setReviewDescription(mData.get(position).getDescription());
        holder.setImageResource(mData.get(position).getAuthor().getSex().getAvatarResource());

        holder.setOnClickListener(v -> {
            if (!mData.get(position).getAuthor().getUsername().equals("deleted_user")) {
                if (!mData.get(position).getAuthor().getUsername().equals(AccountManager.getCurrentLoggedUser().getUsername())) {
                    Intent i = new Intent().setClass(v.getContext(), ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("reviewed_user", mData.get(position).getAuthor().toString());
                    i.putExtras(bundle);
                    v.getContext().startActivity(i);
                }
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
    public int getItemCount() {
        return mData.size();
    }
}
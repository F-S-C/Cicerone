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

package com.fsc.cicerone.adapter.view_holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fsc.cicerone.R;

/**
 * ViewHolder stores and recycles reports as they are scrolled off screen.
 */
public class ReviewViewHolder extends AbstractViewHolder {
    private TextView reviewerUsername;
    private TextView reviewDescription;
    private RatingBar ratingBar;
    private ImageView imageView;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public ReviewViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        reviewerUsername = itemView.findViewById(R.id.reviewer_username);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        reviewDescription = itemView.findViewById(R.id.review_description);
        imageView = itemView.findViewById(R.id.imageView2);
    }

    /**
     * Show the review's rating.
     *
     * @param rating The rating.
     */
    public void setRating(float rating) {
        ratingBar.setRating(rating);
    }

    /**
     * Show the reviewer's username.
     *
     * @param username The username.
     */
    public void setReviewerUsername(String username) {
        reviewerUsername.setText(username);
    }

    /**
     * Show the review's description.
     *
     * @param description The description.
     */
    public void setReviewDescription(String description) {
        reviewDescription.setText(description);
    }

    /**
     * Show an image.
     *
     * @param resource The image resource.
     */
    public void setImageResource(int resource) {
        imageView.setImageResource(resource);
    }
}

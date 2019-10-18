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
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.UserType;

/**
 * ViewHolder stores and recycles reports as they are scrolled off screen.
 */
public class UserViewHolder extends AbstractViewHolder {
    private TextView usr;
    private TextView usrType;
    private RatingBar avgRating;
    private ImageView imageView;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public UserViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        usr = itemView.findViewById(R.id.username);
        usrType = itemView.findViewById(R.id.usrType);
        avgRating = itemView.findViewById(R.id.ratingBar2);
        imageView = itemView.findViewById(R.id.imageView2);
    }

    /**
     * Show the user's username.
     * @param username The user's username.
     */
    public void setUsername(String username){
        usr.setText(username);
    }

    /**
     * Show the user's type,
     * @param userType The user's type.
     */
    public void setUserType(@NonNull UserType userType){
        String typeName;
        switch (userType) {
            case GLOBETROTTER:
                typeName = getContext().getString(R.string.user_type_globetrotter);
                break;
            case CICERONE:
                typeName = getContext().getString(R.string.user_type_cicerone);
                break;
            case ADMIN:
                typeName = getContext().getString(R.string.user_type_admin);
                break;
            default:
                typeName = "";
                break;
        }
        usrType.setText(typeName);
    }

    /**
     * Show an image.
     *
     * @param resource The image resource.
     */
    public void setImageResource(int resource) {
        imageView.setImageResource(resource);
    }

    /**
     * Show the user's rating.
     *
     * @param rating The rating.
     */
    public void setRating(float rating) {
        avgRating.setRating(rating);
    }
}

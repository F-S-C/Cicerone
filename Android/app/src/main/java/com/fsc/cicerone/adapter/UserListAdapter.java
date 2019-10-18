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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.manager.ReviewManager;
import com.fsc.cicerone.model.User;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.ProfileActivity;
import com.fsc.cicerone.view.admin_view.AdminUserProfile;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private final Activity context;
    private List<User> mData;
    private LayoutInflater mInflater;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of JSON Objects got from server.
     */
    public UserListAdapter(Activity context, List<User> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itineraryView = mInflater.inflate(R.layout.user_list, parent, false);
        return new ViewHolder(itineraryView);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String usernameStr = mData.get(position).getUsername();
        UserType type = mData.get(position).getUserType();
        String typeName;
        holder.usr.setText(usernameStr);
        switch (type) {
            case GLOBETROTTER:
                typeName = context.getString(R.string.user_type_globetrotter);
                break;
            case CICERONE:
                typeName = context.getString(R.string.user_type_cicerone);
                break;
            case ADMIN:
                typeName = context.getString(R.string.user_type_admin);
                break;
            default:
                typeName = "";
                break;
        }
        holder.usrType.setText(typeName);
        holder.imageView.setImageResource(mData.get(position).getSex().getAvatarResource());
        ReviewManager.getAvgUserFeedback(context, mData.get(position), holder.avgRating::setRating);

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                i = new Intent(context, AdminUserProfile.class);
                i.putExtra("user", mData.get(position).toString());
                context.startActivity(i);
            } else {
                i = new Intent().setClass(context, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("reviewed_user", mData.get(position).toString());
                i.putExtras(bundle);
                context.startActivity(i);
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

    /**
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView usr;
        TextView usrType;
        RatingBar avgRating;
        ImageView imageView;

        /**
         * ViewHolder constructor.
         *
         * @param itemView ViewHolder view.
         * @see androidx.recyclerview.widget.RecyclerView.ViewHolder#ViewHolder(View)
         */
        ViewHolder(View itemView) {
            super(itemView);
            usr = itemView.findViewById(R.id.username);
            usrType = itemView.findViewById(R.id.usrType);
            avgRating = itemView.findViewById(R.id.ratingBar2);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
}



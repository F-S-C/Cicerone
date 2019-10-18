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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Itinerary;
import com.fsc.cicerone.model.Language;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.ItineraryDetails;
import com.fsc.cicerone.view.ItineraryManagement;
import com.fsc.cicerone.view.WishlistFragment;
import com.fsc.cicerone.view.admin_view.AdminItineraryDetails;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.ViewHolder> {

    private final Activity context;
    private List<Itinerary> mData;
    private LayoutInflater mInflater;
    private Fragment fragment;


    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The array of Itineraries objects in the wishlist.
     */
    public ItineraryAdapter(Activity context, List<Itinerary> list, @Nullable Fragment fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itineraryView = mInflater.inflate(R.layout.itinerary_list, parent, false);
        return new ViewHolder(itineraryView);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = mData.get(position).getTitle();
        String location = mData.get(position).getLocation();

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        holder.beginning.setText(outputFormat.format(mData.get(position).getBeginningDate()));
        holder.ending.setText(outputFormat.format(mData.get(position).getEndingDate()));
        holder.itineraryTitle.setText(title);
        holder.location.setText(location);
        holder.priceTagTextView.setText(String.format(context.getString(R.string.price_value), mData.get(position).getFullPrice()));
        Picasso.get().load(mData.get(position).getImageUrl()).into(holder.imageView);

        holder.setLanguages(mData.get(position).getLanguages());
        holder.showError(AccountManager.isLogged() && !mData.get(position).isInLanguages(AccountManager.getCurrentLoggedUser().getLanguages()));

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            if (mData.get(position).getCicerone().equals(AccountManager.getCurrentLoggedUser())) {
                i = new Intent().setClass(v.getContext(), ItineraryManagement.class);
            } else {
                if (AccountManager.isLogged() && AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                    i = new Intent(context, AdminItineraryDetails.class);
                } else {
                    i = new Intent(context, ItineraryDetails.class);
                }
            }
            i.putExtra("itinerary", mData.get(position).toJSONObject().toString());
            if (fragment != null) {
                fragment.startActivityForResult(i, WishlistFragment.REQUEST_UPDATE_WISHLIST);
            } else {
                context.startActivityForResult(i, ItineraryManagement.RESULT_ITINERARY_DELETED);
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

        //Defining variables of ITINERARY_LIST view
        TextView itineraryTitle;
        TextView location;
        TextView beginning;
        TextView ending;
        ImageView imageView;
        TextView priceTagTextView;
        LinearLayout languageContainerLayout;
        TextView languageErrorTextView;

        /**
         * ViewHolder constructor.
         *
         * @param itemView ViewHolder view.
         * @see androidx.recyclerview.widget.RecyclerView.ViewHolder#ViewHolder(View)
         */
        ViewHolder(View itemView) {
            super(itemView);
            itineraryTitle = itemView.findViewById(R.id.itinerary_title);
            location = itemView.findViewById(R.id.location);
            beginning = itemView.findViewById(R.id.beginning);
            ending = itemView.findViewById(R.id.ending);
            imageView = itemView.findViewById(R.id.media_image);
            priceTagTextView = itemView.findViewById(R.id.itinerary_price_badge);
            languageContainerLayout = itemView.findViewById(R.id.language_container);
            languageErrorTextView = itemView.findViewById(R.id.language_error);
        }

        /**
         * Shows the itinerary languages.
         *
         * @param languages The languages.
         */
        void setLanguages(Set<Language> languages) {
            languageContainerLayout.removeAllViews();
            for (Language language : languages) {
                Chip languageChip = new Chip(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(4, 4, 4, 4);
                languageChip.setLayoutParams(layoutParams);
                languageChip.setText(language.getCode());
                languageContainerLayout.addView(languageChip);
            }
        }

        /**
         * Show a "Not available in your languages" error into the itinerary if show is true.
         *
         * @param show If true it shows the error.
         */
        void showError(boolean show) {
            languageErrorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}


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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.Language;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.util.Set;

/**
 * A view holder that shows an itinerary.
 */
public class ItineraryViewHolder extends AbstractItineraryViewHolder {
    private ImageView imageView;
    private TextView priceTagTextView;
    private LinearLayout languageContainerLayout;
    private TextView languageErrorTextView;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public ItineraryViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        imageView = itemView.findViewById(R.id.media_image);
        priceTagTextView = itemView.findViewById(R.id.itinerary_price_badge);
        languageContainerLayout = itemView.findViewById(R.id.language_container);
        languageErrorTextView = itemView.findViewById(R.id.language_error);
    }

    /**
     * Show the itinerary's image.
     *
     * @param url The image's url.
     */
    public void setImage(String url) {
        Picasso.get().load(url).into(imageView);
    }

    /**
     * Show the itinerary's price.
     *
     * @param price The price.
     */
    public void setPrice(float price) {
        priceTagTextView.setText(String.format(getContext().getString(R.string.price_value), price));
    }

    /**
     * Shows the itinerary languages.
     *
     * @param languages The languages.
     */
    public void setLanguages(Set<Language> languages) {
        languageContainerLayout.removeAllViews();
        for (Language language : languages) {
            Chip languageChip = new Chip(getContext());
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
    public void showError(boolean show) {
        languageErrorTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}

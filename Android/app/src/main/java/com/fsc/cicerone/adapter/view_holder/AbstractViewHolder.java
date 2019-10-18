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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;

/**
 * An abstract view holder. It is the base class for all the view holders used in Cicerone.
 */
public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
    private final Context context;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see androidx.recyclerview.widget.RecyclerView.ViewHolder#ViewHolder(View)
     */
    public AbstractViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView);
        this.context = context;
    }

    /**
     * Get the view's holder context.
     *
     * @return The context.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Format a date to a string based on the user's locale.
     *
     * @param date The date to be formatted.
     * @return A string representation of the date.
     */
    protected String formatDate(Date date) {
        DateFormat outputFormat = android.text.format.DateFormat.getDateFormat(context);
        return outputFormat.format(date);
    }
}

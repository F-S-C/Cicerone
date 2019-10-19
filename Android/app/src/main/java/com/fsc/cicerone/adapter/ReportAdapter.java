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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.R;
import com.fsc.cicerone.adapter.view_holder.ReportViewHolder;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.UserType;
import com.fsc.cicerone.view.report.ReportDetailsActivity;
import com.fsc.cicerone.view.user.registered_user.ReportFragment;
import com.fsc.cicerone.view.admin.AdminReportDetailsActivity;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportViewHolder> {
    private final Activity context;
    private List<Report> mData;
    private LayoutInflater mInflater;
    private Fragment fragment;

    /**
     * Constructor.
     *
     * @param context The parent Context.
     * @param list    The list of reports to be shown.
     */
    public ReportAdapter(Activity context, List<Report> list, Fragment fragment) {
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
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportView = mInflater.inflate(R.layout.report_list, parent, false);
        return new ReportViewHolder(reportView, context);
    }

    /**
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder,
     * int)
     */
    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.setStatus(mData.get(position).getStatus());
        holder.setSubject(mData.get(position).getObject());
        holder.setReportCode(mData.get(position).getCode());

        holder.setOnClickListener(v -> {
            Intent i;
            if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                i = new Intent(context, AdminReportDetailsActivity.class);
            } else {
                i = new Intent(context, ReportDetailsActivity.class);
            }
            Bundle bundle = new Bundle();
            bundle.putString("report", mData.get(position).toString());
            i.putExtras(bundle);
            fragment.startActivityForResult(i, ReportFragment.RESULT_SHOULD_REPORT_BE_RELOADED);
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
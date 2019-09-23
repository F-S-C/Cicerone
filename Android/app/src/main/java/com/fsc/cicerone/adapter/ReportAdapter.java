package com.fsc.cicerone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fsc.cicerone.ReportFragment;
import com.fsc.cicerone.manager.AccountManager;
import com.fsc.cicerone.AdminReportDetailsActivity;
import com.fsc.cicerone.R;
import com.fsc.cicerone.ReportDetailsActivity;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.UserType;

import java.util.List;

/**
 * The ReviewAdapter of the Recycler View for the styles present in the app.
 */
public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private final Activity context;
    private List<Report> mData;
    private LayoutInflater mInflater;
    private Drawable reportCanceledImage;
    private Drawable reportClosedImage;
    private Drawable reportInProgressImage;
    private Drawable reportOpenImage;
    private Fragment fragment;


    /**
     * Constructor.
     *
     * @param context    The parent Context.
     * @param list       The list of reports to be shown.
     */
    public ReportAdapter(Activity context, List<Report> list, Fragment fragment) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
        this.context = context;
        this.fragment = fragment;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View reportView = mInflater.inflate(R.layout.report_list, parent, false);
        reportCanceledImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_canceled, null);
        reportClosedImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_closed, null);
        reportInProgressImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_in_progress, null);
        reportOpenImage = ResourcesCompat.getDrawable(reportView.getResources(), R.drawable.report_open, null);
        return new ViewHolder(reportView);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (mData.get(position).getStatus()) {
            case OPEN:
                holder.status.setImageDrawable(reportOpenImage);
                break;
            case CLOSED:
                holder.status.setImageDrawable(reportClosedImage);
                break;
            case PENDING:
                holder.status.setImageDrawable(reportInProgressImage);
                break;
            case CANCELED:
                holder.status.setImageDrawable(reportCanceledImage);
                break;
            default:
                break;
        }
        holder.object.setText(mData.get(position).getObject());
        holder.reportCode.setText(String.format(context.getString(R.string.print_integer_number), mData.get(position).getCode()));

        holder.itemView.setOnClickListener(v -> {
            Intent i;
            if (AccountManager.getCurrentLoggedUser().getUserType() == UserType.ADMIN) {
                i = new Intent(context, AdminReportDetailsActivity.class);
            } else {
                i = new Intent(context, ReportDetailsActivity.class);
            }
            Bundle bundle = new Bundle();
            bundle.putString("report_code", String.valueOf(mData.get(position).getCode()));
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

    /**
     * ViewHolder stores and recycles reports as they are scrolled off screen.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView object;
        TextView reportCode;
        ImageView status;

        ViewHolder(View itemView) {
            super(itemView);

            object = itemView.findViewById(R.id.object);
            reportCode = itemView.findViewById(R.id.report_code);
            status = itemView.findViewById(R.id.report_status);
        }
    }
}
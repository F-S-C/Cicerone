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
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.fsc.cicerone.R;
import com.fsc.cicerone.model.ReportStatus;

/**
 * A view holder that shows a report.
 */
public class ReportViewHolder extends AbstractViewHolder {
    private TextView object;
    private TextView reportCode;
    private ImageView status;

    /**
     * The view holder's constructor.
     *
     * @param itemView The view holder's view.
     * @param context  The view holder's context.
     * @see AbstractViewHolder#AbstractViewHolder(View, Context)
     */
    public ReportViewHolder(@NonNull View itemView, @NonNull Context context) {
        super(itemView, context);
        object = itemView.findViewById(R.id.object);
        reportCode = itemView.findViewById(R.id.report_code);
        status = itemView.findViewById(R.id.report_status);
    }

    /**
     * Show the report's subject.
     *
     * @param subject The subject.
     */
    public void setSubject(String subject) {
        this.object.setText(subject);
    }

    /**
     * Show the report's code.
     *
     * @param code The code.
     */
    public void setReportCode(int code) {
        reportCode.setText(String.format(getContext().getString(R.string.print_integer_number), code));
    }

    /**
     * Show the report status.
     *
     * @param status The status.
     */
    public void setStatus(ReportStatus status) {
        Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.report_open, null);
        switch (status) {
            case OPEN:
                ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.report_open, null);
                break;
            case CLOSED:
                ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.report_closed, null);
                break;
            case PENDING:
                ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.report_in_progress, null);
                break;
            case CANCELED:
                ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.report_canceled, null);
                break;
            default:
                break;
        }
        this.status.setImageDrawable(drawable);
    }
}

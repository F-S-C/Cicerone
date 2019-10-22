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

package com.fsc.cicerone.view.admin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.R;
import com.fsc.cicerone.manager.ReportManager;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.ReportStatus;

import java.util.Objects;

/**
 * A class that manage to show the details of  a Report Admin-side.
 */
public class AdminReportDetailsActivity extends AppCompatActivity {

    private TextView reportTitle;
    private TextView reportCode;
    private TextView status;
    private TextView reportedUser;
    private TextView reporterUser;
    private TextView bodyText;
    private Button takeChargeReport;

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setTitle(getString(R.string.report_details_title));
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        Report report;

        setContentView(R.layout.activity_admin_report_details);
        takeChargeReport = findViewById(R.id.take_charge_report);
        Button closeReport = findViewById(R.id.close_report);
        reportTitle = findViewById(R.id.report_title_admin);
        reportCode = findViewById(R.id.report_code_admin);
        status = findViewById(R.id.status_text_admin);
        reportedUser = findViewById(R.id.report_user_activity_admin);
        reporterUser = findViewById(R.id.reporter_user_activity_admin);
        bodyText = findViewById(R.id.body_report_activity_admin);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data
        report = new Report(Objects.requireNonNull(bundle.getString("report")));
        bindDataToView(report);

        takeChargeReport.setOnClickListener(v -> {
            ReportManager.takeCharge(this, report, success -> Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_taking_charge), Toast.LENGTH_SHORT).show());
            report.setStatus(ReportStatus.PENDING);
            bindDataToView(report);
            takeChargeReport.setEnabled(false);
            setResult(Activity.RESULT_OK);
        });

        closeReport.setOnClickListener(v -> {
            ReportManager.closeReport(this, report, success -> Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_closed), Toast.LENGTH_SHORT).show());
            report.setStatus(ReportStatus.CLOSED);
            bindDataToView(report);
            closeReport.setEnabled(false);
            takeChargeReport.setEnabled(false);
            setResult(Activity.RESULT_OK);
        });

    }

    /**
     * A function that sets the data of the current Report in the correct spaces.
     *
     * @param report The current Report
     */
    private void bindDataToView(Report report) {
        if (report.getStatus().toString().equals("Pending"))
            takeChargeReport.setEnabled(false);

        reportCode.setText(String.valueOf(report.getCode()));
        reportTitle.setText(report.getObject());
        status.setText(report.getStatus().toString());
        reportedUser.setText(report.getReportedUser().getUsername());
        reporterUser.setText(report.getAuthor().getUsername());
        bodyText.setText(report.getBody());

    }

    /**
     * @see AppCompatActivity#onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}


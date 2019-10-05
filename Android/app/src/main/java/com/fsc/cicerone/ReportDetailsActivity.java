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

package com.fsc.cicerone;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.manager.ReportManager;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.ReportStatus;

import java.util.Objects;

public class ReportDetailsActivity extends AppCompatActivity {

    private TextView reportTitle;
    private TextView reportCode;
    private TextView status;
    private TextView reportedUser;
    private TextView bodyText;
    private Button cancButton;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        final ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

        reportTitle = findViewById(R.id.report_title);
        reportCode = findViewById(R.id.report_code);
        status = findViewById(R.id.status_text);
        reportedUser = findViewById(R.id.report_user_activity);
        bodyText = findViewById(R.id.body_report_activity);
        cancButton = findViewById(R.id.delete_report_btn);
        // Get the bundle
        Bundle bundle = getIntent().getExtras();
        // Extract the data
        report = new Report(Objects.requireNonNull(bundle).getString("report"));

        bindDataToView(report);

        cancButton.setOnClickListener(view -> {
            cancButton.setEnabled(false);
            ReportManager.removeReport(this, report, success -> Toast.makeText(ReportDetailsActivity.this, ReportDetailsActivity.this.getString(R.string.report_canceled), Toast.LENGTH_SHORT).show());
            report.setStatus(ReportStatus.CANCELED);
            bindDataToView(report);
            setResult(Activity.RESULT_OK);
        });
    }

    private void bindDataToView(Report report) {
        if (report.getStatus().toString().equals("Open")){
            cancButton.setVisibility(View.VISIBLE);
            cancButton.setEnabled(true);
        }else{
            cancButton.setEnabled(false);
            cancButton.setTextColor(Color.GRAY);
        }

        reportTitle.setText(report.getObject());
        reportCode.setText(String.valueOf(report.getCode()));
        status.setText(report.getStatus().toString());
        reportedUser.setText(report.getReportedUser().getUsername());
        bodyText.setText(report.getBody());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
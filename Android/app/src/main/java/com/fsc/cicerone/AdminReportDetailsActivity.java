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
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.ReportStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.SendInPostConnector;

public class AdminReportDetailsActivity extends AppCompatActivity {

    private TextView reportTitle;
    private TextView reportCode;
    private TextView status;
    private TextView reportedUser;
    private TextView reporterUser;
    private TextView bodyText;
    private Button takeChargeReport;
    private Button closeReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar supportActionBar = Objects.requireNonNull(getSupportActionBar());
        supportActionBar.setTitle(getString(R.string.report_details_title));
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);
        
        setContentView(R.layout.activity_admin_report_details);
        takeChargeReport = findViewById(R.id.take_charge_report);
        closeReport = findViewById(R.id.close_report);
        reportTitle = findViewById(R.id.report_title_admin);
        reportCode = findViewById(R.id.report_code_admin);
        status = findViewById(R.id.status_text_admin);
        reportedUser = findViewById(R.id.report_user_activity_admin);
        reporterUser = findViewById(R.id.reporter_user_activity_admin);
        bodyText = findViewById(R.id.body_report_activity_admin);
        Map<String, Object> parameters = new HashMap<>(3);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();
        //Extract the data
        parameters.put("report_code", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("report_code")));
        getReportFromServer(parameters);

        takeChargeReport.setOnClickListener(v -> takeCharge(parameters));

        closeReport.setOnClickListener(v -> close(parameters));

    }

    private void getReportFromServer(Map<String, Object> params) {
        SendInPostConnector<Report> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REPORT_FRAGMENT, BusinessEntityBuilder.getFactory(Report.class))
                .setContext(this)
                .setOnStartConnectionListener(() -> {
                    takeChargeReport.setEnabled(false);
                    closeReport.setEnabled(false);
                })
                .setOnEndConnectionListener(list -> {
                    Report result = list.get(0);
                    String code = "Nr. " + result.getCode();
                    String statusText = "Status: ";
                    reportTitle.setText(result.getObject());
                    reportCode.setText(code);
                    switch (result.getStatus()) {
                        case OPEN:
                            statusText += getString(R.string.open);
                            takeChargeReport.setEnabled(true);
                            closeReport.setEnabled(true);
                            break;
                        case CLOSED:
                            statusText += getString(R.string.closed);
                            break;
                        case PENDING:
                            statusText += getString(R.string.pending);
                            closeReport.setEnabled(true);
                            break;
                        case CANCELED:
                            statusText += getString(R.string.canceled);
                            break;
                        default:
                            break;
                    }
                    status.setText(statusText);
                    reportedUser.setText(result.getReportedUser().getUsername());
                    reporterUser.setText(result.getAuthor().getUsername());
                    bodyText.setText(result.getBody());
                })
                .setObjectToSend(params)
                .build();
        connector.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void takeCharge(Map<String, Object> params) {
        params.put("object", reportTitle.getText().toString());
        params.put("report_body", bodyText.getText().toString());
        params.put("state", ReportStatus.PENDING.toInt());
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(this)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_taking_charge), Toast.LENGTH_SHORT).show();
                        getReportFromServer(params);
                        setResult(Activity.RESULT_OK);
                    }
                })
                .setObjectToSend(params)
                .build();
        connector.execute();
    }

    public void close(Map<String, Object> params) {
        params.put("object", reportTitle.getText().toString());
        params.put("report_body", bodyText.getText().toString());
        params.put("state", ReportStatus.CLOSED.toInt());
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(this)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("p", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_closed), Toast.LENGTH_SHORT).show();
                        getReportFromServer(params);
                        setResult(Activity.RESULT_OK);
                    }
                })
                .setObjectToSend(params)
                .build();
        connector.execute();
    }
}


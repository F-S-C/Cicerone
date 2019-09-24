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
import android.view.View;
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

import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;

public class ReportDetailsActivity extends AppCompatActivity {

    private TextView reportTitle;
    private TextView reportCode;
    private TextView status;
    private TextView reportedUser;
    private TextView bodyText;
    private Button cancButton;

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
        Map<String, Object> parameters = new HashMap<>();
        // Get the bundle
        Bundle bundle = getIntent().getExtras();
        // Extract the data
        parameters.put("report_code", Objects.requireNonNull(Objects.requireNonNull(bundle).getString("report_code")));
        cancButton.setVisibility(View.GONE);
        getReportFromServer(parameters);

        cancButton.setOnClickListener(view -> {
            deleteReport(parameters);
            cancButton.setEnabled(false);
        });
    }

    private void getReportFromServer(Map<String, Object> params) {
        SendInPostConnector<Report> connector = new SendInPostConnector.Builder<>(ConnectorConstants.REPORT_FRAGMENT, BusinessEntityBuilder.getFactory(Report.class))
                .setContext(this)
                .setOnEndConnectionListener(list -> {
                    Report result = list.get(0);
                    String code = "Nr. " + result.getCode();
                    String statusText = "Status: ";
                    reportTitle.setText(result.getObject());
                    reportCode.setText(code);
                    switch (result.getStatus()) {
                        case OPEN:
                            statusText += getString(R.string.open);
                            cancButton.setEnabled(true);
                            cancButton.setVisibility(View.VISIBLE);
                            break;
                        case CLOSED:
                            statusText += getString(R.string.closed);
                            cancButton.setVisibility(View.GONE);
                            break;
                        case PENDING:
                            statusText += getString(R.string.pending);
                            cancButton.setVisibility(View.GONE);
                            break;
                        case CANCELED:
                            statusText += getString(R.string.canceled);
                            cancButton.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }
                    status.setText(statusText);
                    reportedUser.setText(result.getReportedUser().getUsername());
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

    private void deleteReport(Map<String, Object> params) {
        params.put("state", ReportStatus.CLOSED.toInt());
        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(this)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.report_canceled), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        params.remove("state");
                        getReportFromServer(params);
                    }
                })
                .setObjectToSend(params)
                .build();
        connector.execute();
    }
}
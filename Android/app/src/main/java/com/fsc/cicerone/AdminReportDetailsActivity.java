package com.fsc.cicerone;

import android.content.Intent;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
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
        SendInPostConnector<Report> connector = new SendInPostConnector<>(
                ConnectorConstants.REPORT_FRAGMENT,
                BusinessEntityBuilder.getFactory(Report.class),
                new DatabaseConnector.CallbackInterface<Report>() {
                    @Override
                    public void onStartConnection() {
                        takeChargeReport.setEnabled(false);
                        closeReport.setEnabled(false);
                    }

                    @Override
                    public void onEndConnection(List<Report> list) {
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
                    }
                },
                params);
        connector.execute();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, AdminMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void takeCharge(Map<String, Object> params) {
        params.put("object", reportTitle.getText().toString());
        params.put("report_body", bodyText.getText().toString());
        params.put("state", ReportStatus.toInt(ReportStatus.PENDING));
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.UPDATE_REPORT_DETAILS,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        Log.e("p", result.toJSONObject().toString());
                        if (result.getResult()) {
                            Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_taking_charge), Toast.LENGTH_SHORT).show();
                            getReportFromServer(params);
                        }
                    }
                },
                params);
        connector.execute();
    }

    public void close(Map<String, Object> params) {
        params.put("object", reportTitle.getText().toString());
        params.put("report_body", bodyText.getText().toString());
        params.put("state", ReportStatus.toInt(ReportStatus.CLOSED));
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.UPDATE_REPORT_DETAILS,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        Log.e("p", result.toJSONObject().toString());
                        if (result.getResult()) {
                            Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_closed), Toast.LENGTH_SHORT).show();
                            getReportFromServer(params);
                        }
                    }
                },
                params);
        connector.execute();
    }
}


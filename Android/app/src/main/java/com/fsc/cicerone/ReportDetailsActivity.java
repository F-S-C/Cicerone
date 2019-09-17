package com.fsc.cicerone;

import android.content.Intent;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import app_connector.BooleanConnector;
import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

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
        getReportFromServer(parameters);
        cancButton.setEnabled(false);
        cancButton.setVisibility(View.GONE);
        cancButton.setOnClickListener(view -> deleteReport(parameters));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void getReportFromServer(Map<String, Object> params) {
        SendInPostConnector<Report> connector = new SendInPostConnector<>(ConnectorConstants.REPORT_FRAGMENT,
                BusinessEntityBuilder.getFactory(Report.class), new DatabaseConnector.CallbackInterface<Report>() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
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
                            cancButton.setEnabled(true);
                            cancButton.setVisibility(View.VISIBLE);
                            break;
                        case CLOSED:
                            statusText += getString(R.string.closed);
                            break;
                        case PENDING:
                            statusText += getString(R.string.pending);
                            break;
                        case CANCELED:
                            statusText += getString(R.string.canceled);
                            break;
                        default:
                            break;
                        }
                        status.setText(statusText);
                        reportedUser.setText(result.getReportedUser().getUsername());
                        bodyText.setText(result.getBody());
                    }
                }, params);
        connector.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void deleteReport(Map<String, Object> params) {
        params.put("state", ReportStatus.toInt(ReportStatus.CLOSED));
        BooleanConnector connector = new BooleanConnector(ConnectorConstants.UPDATE_REPORT_DETAILS,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) {
                        if (result.getResult()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.report_canceled),
                                    Toast.LENGTH_SHORT).show();
                            params.remove("state");
                            getReportFromServer(params);
                        }
                    }
                }, params);
        connector.execute();
    }
}
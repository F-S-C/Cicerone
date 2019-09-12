package com.fsc.cicerone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String ERROR_TAG = "ERROR IN " + AdminReportDetailsActivity.class.getName();

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
        JSONObject parameters = new JSONObject();

        try {
            //Get the bundle
            Bundle bundle = getIntent().getExtras();
            //Extract the data
            parameters.put("report_code", Objects.requireNonNull(bundle).getString("report_code"));
            getReportFromServer(parameters);

            takeChargeReport.setOnClickListener(v -> takeCharge(parameters));

            closeReport.setOnClickListener(v -> close(parameters));

        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    private void getReportFromServer(JSONObject params) {
        // TODO: Add report class
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REPORT_FRAGMENT, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                takeChargeReport.setEnabled(false);
                closeReport.setEnabled(false);
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                JSONObject result = jsonArray.getJSONObject(0);
                String code = "Nr. " + result.getString("report_code");
                String statusText = "Status: ";
                reportTitle.setText(result.getString("object"));
                reportCode.setText(code);
                switch (Objects.requireNonNull(ReportStatus.getValue(result.getInt("state")))) {
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
                reportedUser.setText(result.getString("reported_user"));
                reporterUser.setText(result.getString("username"));
                bodyText.setText(result.getString("report_body"));
            }
        });
        connector.setObjectToSend(params);
        connector.execute();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, AdminMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void takeCharge(JSONObject params) {
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
                });
        try {
            params.put("object", reportTitle.getText().toString());
            params.put("report_body", bodyText.getText().toString());
            params.put("state", ReportStatus.getInt(ReportStatus.PENDING));
            connector.setObjectToSend(params);
            connector.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    public void close(JSONObject params) {
        BooleanConnector connector = new BooleanConnector(
                ConnectorConstants.UPDATE_REPORT_DETAILS,
                new BooleanConnector.CallbackInterface() {
                    @Override
                    public void onStartConnection() {
                        // Do nothing
                    }

                    @Override
                    public void onEndConnection(BooleanConnector.BooleanResult result) throws JSONException {
                        Log.e("p", result.toJSONObject().toString());
                        if (result.getResult()) {
                            Toast.makeText(AdminReportDetailsActivity.this, AdminReportDetailsActivity.this.getString(R.string.report_closed), Toast.LENGTH_SHORT).show();
                            getReportFromServer(params);
                        }
                    }
                });
        try {
            params.put("object", reportTitle.getText().toString());
            params.put("report_body", bodyText.getText().toString());
            params.put("state", ReportStatus.getInt(ReportStatus.CLOSED));
            connector.setObjectToSend(params);
            connector.execute();
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }
}


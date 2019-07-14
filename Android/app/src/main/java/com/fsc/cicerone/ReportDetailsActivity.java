package com.fsc.cicerone;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class ReportDetailsActivity extends AppCompatActivity {

    private TextView reportTitle;
    private TextView reportCode;
    private TextView status;
    private TextView reportedUser;
    private TextView bodyText;
    private static final String ERROR_TAG = "ERROR IN " + LoginActivity.class.getName();

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
        JSONObject parameters = new JSONObject();
        try {
            //Get the bundle
            Bundle bundle = getIntent().getExtras();
            //Extract the data
            parameters.put("report_code", Objects.requireNonNull(bundle).getString("report_code"));
            getReportFromServer(parameters);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.toString());
        }
    }

    private void getReportFromServer(JSONObject params){
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.REPORT_FRAGMENT, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                // Do nothing
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
                reportedUser.setText(result.getString("reported_user"));
                bodyText.setText(result.getString("report_body"));
            }
        });
        connector.setObjectToSend(params);
        connector.execute();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
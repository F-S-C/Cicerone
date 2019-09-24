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

package com.fsc.cicerone.manager;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.fsc.cicerone.R;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.ReportStatus;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.Map;

public abstract class ReportManager {
    private ReportManager(){
        throw new IllegalStateException("Utility class");
    }


    /**
     * Insert a Report made by a User to another.
     *
     * @param context               The context of the activity.
     * @param author                The author of the report.
     * @param reportedUserUsername  The username of the reported user.
     * @param object                The object of the report
     * @param body                  The body of the report.
     */
    public static void addNewReport(Activity context, User author, String reportedUserUsername, String object, String body)
    {
        Map<String, Object> param = new HashMap<>(5);
        param.put("username", author.getUsername());
        param.put("reported_user", reportedUserUsername);
        param.put("report_body", body);
        param.put("state", ReportStatus.OPEN.toInt());
        param.put("object", object);

        new BooleanConnector.Builder(ConnectorConstants.INSERT_REPORT)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult())
                        Toast.makeText(context, context.getString(R.string.report_sent), Toast.LENGTH_SHORT).show();
                    Log.e("REPORT", result.getMessage());
                })
                .setObjectToSend(param)
                .build()
                .execute();
    }

    /**
     * Change the status of the report to "Pending".
     *
     * @param context The context of the activity.
     * @param report  The report to close.
     */
    public static void takeCharge(Activity context, Report report)
    {
        Map<String, Object> param = new HashMap<>(2);
        param.put("report_code", report.getCode());
        param.put("state",ReportStatus.PENDING.toInt());

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult())
                        Toast.makeText(context, context.getString(R.string.report_taking_charge), Toast.LENGTH_SHORT).show();
                    Log.e("REPORT", result.getMessage());
                })
                .setObjectToSend(param)
                .build()
                .execute();

    }

    /**
     * Change the status of the report to "Canceled".
     *
     * @param context The context of the activity.
     * @param report  The report to close.
     */
    public static void  removeReport (Activity context, Report report)
    {
        Map<String, Object> param = new HashMap<>(2);
        param.put("report_code", report.getCode());
        param.put("state", ReportStatus.CANCELED.toInt());

        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (result.getResult()) {
                        Toast.makeText(context, context.getString(R.string.report_canceled), Toast.LENGTH_SHORT).show();

                    }
                })
                .setObjectToSend(param)
                .build();
        connector.execute();


    }

    /**
     * Change the status of the report to "Closed".
     *
     * @param context The context of the activity.
     * @param report  The report to close.
     */
    public static void  closeReport (Activity context, Report report)
    {
        Map<String, Object> param = new HashMap<>(2);
        param.put("report_code", report.getCode());
        param.put("state", ReportStatus.CLOSED.toInt());

        BooleanConnector connector = new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    Log.e("remove", result.toJSONObject().toString());
                    if (result.getResult()) {
                        Toast.makeText(context, context.getString(R.string.report_closed), Toast.LENGTH_SHORT).show();

                    }
                })
                .setObjectToSend(param)
                .build();
        connector.execute();
    }
}

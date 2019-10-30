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

import androidx.annotation.Nullable;

import com.fsc.cicerone.app_connector.AsyncDatabaseConnector;
import com.fsc.cicerone.app_connector.BooleanConnector;
import com.fsc.cicerone.app_connector.ConnectorConstants;
import com.fsc.cicerone.app_connector.SendInPostConnector;
import com.fsc.cicerone.functional_interfaces.Consumer;
import com.fsc.cicerone.model.BusinessEntityBuilder;
import com.fsc.cicerone.model.Report;
import com.fsc.cicerone.model.ReportStatus;
import com.fsc.cicerone.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that provides useful functions for the management of the reports.
 */
public class ReportManager {
    private ReportManager() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * Insert a Report made by a User to another.
     *
     * @param context              The context of the activity.
     * @param author               The author of the report.
     * @param reportedUserUsername The username of the reported user.
     * @param object               The object of the report
     * @param body                 The body of the report.
     * @param callback             A callback to be executed after the operation is completed.
     */
    public static void addNewReport(Activity context, User author, String reportedUserUsername, String object, String body, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> param = new HashMap<>(5);
        param.put(User.Columns.USERNAME_KEY, author.getUsername());
        param.put(Report.Columns.REPORTED_USER_KEY, reportedUserUsername);
        param.put(Report.Columns.BODY_KEY, body);
        param.put(Report.Columns.STATE_KEY, ReportStatus.OPEN.toInt());
        param.put(Report.Columns.OBJECT_KEY, object);

        new BooleanConnector.Builder(ConnectorConstants.INSERT_REPORT)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(param)
                .build()
                .getData();
    }

    /**
     * Change the status of the report to "Pending".
     *
     * @param context  The context of the activity.
     * @param report   The report to close.
     * @param callback A callback to be executed after the operation is completed.
     */
    public static void takeCharge(Activity context, Report report, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> param = new HashMap<>(2);
        param.put(Report.Columns.REPORT_CODE_KEY, report.getCode());
        param.put(Report.Columns.STATE_KEY, ReportStatus.PENDING.toInt());

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(param)
                .build()
                .getData();

    }

    /**
     * Change the status of the report to "Canceled".
     *
     * @param context  The context of the activity.
     * @param report   The report to close.
     * @param callback A callback to be executed after the operation is completed.
     */
    public static void removeReport(Activity context, Report report, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> param = new HashMap<>(2);
        param.put(Report.Columns.REPORT_CODE_KEY, report.getCode());
        param.put(Report.Columns.STATE_KEY, ReportStatus.CANCELED.toInt());

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(param)
                .build()
                .getData();


    }

    /**
     * Change the status of the report to "Closed".
     *
     * @param context  The context of the activity.
     * @param report   The report to close.
     * @param callback A callback to be executed after the operation is completed.
     */
    public static void closeReport(Activity context, Report report, @Nullable Consumer<Boolean> callback) {
        Map<String, Object> param = new HashMap<>(2);
        param.put(Report.Columns.REPORT_CODE_KEY, report.getCode());
        param.put(Report.Columns.STATE_KEY, ReportStatus.CLOSED.toInt());

        new BooleanConnector.Builder(ConnectorConstants.UPDATE_REPORT_DETAILS)
                .setContext(context)
                .setOnEndConnectionListener((BooleanConnector.OnEndConnectionListener) result -> {
                    if (callback != null) callback.accept(result.getResult());
                })
                .setObjectToSend(param)
                .build()
                .getData();
    }

    /**
     * Get list of the report.
     *
     * @param context                   The context of the activity.
     * @param user                      The user of request view reports' list.
     * @param onStartConnectionListener On start connection callback.
     * @param callback                  A callback to be executed after the operation is completed.
     */
    public static void requestReport(Activity context, User user, @Nullable AsyncDatabaseConnector.OnStartConnectionListener onStartConnectionListener, @Nullable AsyncDatabaseConnector.OnEndConnectionListener<Report> callback) {
        final Map<String, Object> parameters = new HashMap<>();
        if (user != null) parameters.put(User.Columns.USERNAME_KEY, user.getUsername());
        new SendInPostConnector.Builder<>(ConnectorConstants.REQUEST_REPORT, BusinessEntityBuilder.getFactory(Report.class))
                .setContext(context)
                .setOnStartConnectionListener(onStartConnectionListener)
                .setOnEndConnectionListener(callback)
                .setObjectToSend(parameters)
                .build()
                .getData();
    }
}

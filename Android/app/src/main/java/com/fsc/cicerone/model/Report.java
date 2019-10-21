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

package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A report has represented in Cicerone. Each report is identified by a number.
 */
public class Report extends BusinessEntity {

    private int code;
    private User author;
    private User reportedUser;
    private String object;
    private String body;
    private ReportStatus status;

    private static final String ERROR_TAG = "REPORT_ERROR";

    /**
     * A utility class that contains various strings to be used as keys to communicate with the
     * remote server.
     */
    public static class Columns {
        private Columns() {
            throw new IllegalStateException("Utility class");
        }

        public static final String AUTHOR_KEY = "username";
        public static final String REPORTED_USER_KEY = "reported_user";
        public static final String REPORT_CODE_KEY = "report_code";
        public static final String OBJECT_KEY = "object";
        public static final String BODY_KEY = "report_body";
        public static final String STATE_KEY = "state";
    }

    /**
     * Report's constructor. Convert a json string to Report.
     *
     * @param json The json string.
     */
    public Report(String json) {
        this(getJSONObject(json));
    }

    /**
     * Report's constructor. Convert a JSONObject to Report.
     *
     * @param jsonObject The JSONObject.
     */
    public Report(JSONObject jsonObject) {
        loadFromJSONObject(jsonObject);
    }

    /**
     * @see BusinessEntity#loadFromJSONObject(JSONObject)
     */
    @Override
    protected void loadFromJSONObject(JSONObject jsonObject) {
        int tempCode;
        User tempAuthor;
        User tempReportedUser;
        String tempObject;
        String tempBody;

        try {
            tempCode = jsonObject.getInt(Columns.REPORT_CODE_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempCode = 0;
        }
        code = tempCode;

        try {
            tempAuthor = new User(jsonObject.getJSONObject(User.Columns.USERNAME_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempAuthor = new User();
        }
        author = tempAuthor;

        try {
            tempReportedUser = new User(jsonObject.getJSONObject(Columns.REPORTED_USER_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempReportedUser = new User();
        }
        reportedUser = tempReportedUser;

        try {
            tempObject = jsonObject.getString(Columns.OBJECT_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempObject = "Error";
        }
        object = tempObject;

        try {
            tempBody = jsonObject.getString(Columns.BODY_KEY);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempBody = "There was an error.";
        }
        body = tempBody;

        try {
            status = ReportStatus.getValue(jsonObject.getInt(Columns.STATE_KEY));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            status = ReportStatus.OPEN;
        }
    }

    /**
     * Get the Report's code.
     *
     * @return The code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the Report's author.
     *
     * @return The author.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Get the reported user.
     *
     * @return The reported user.
     */
    public User getReportedUser() {
        return reportedUser;
    }

    /**
     * Get the Report's object.
     *
     * @return The object.
     */
    public String getObject() {
        return object;
    }

    /**
     * Get the Report's body.
     *
     * @return The body.
     */
    public String getBody() {
        return body;
    }

    /**
     * Get the Report's status.
     *
     * @return The status.
     */
    public ReportStatus getStatus() {
        return status;
    }

    /**
     * Set the Report's status.
     *
     * @param status The status.
     */
    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    /**
     * @see BusinessEntity#toJSONObject()
     */
    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(User.Columns.USERNAME_KEY, author.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.REPORTED_USER_KEY, reportedUser.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.REPORT_CODE_KEY, code);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.OBJECT_KEY, object);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.BODY_KEY, body);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put(Columns.STATE_KEY, status.toInt());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        return jsonObject;
    }
}

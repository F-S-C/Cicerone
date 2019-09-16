package com.fsc.cicerone.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Report extends BusinessEntity {
    private final int code;
    private final User author;
    private final User reportedUser;
    private final String object;
    private final String body;
    private ReportStatus status;

    private static final String ERROR_TAG = "REPORT_ERROR";

    public Report(JSONObject jsonObject) {
        int tempCode;
        User tempAuthor;
        User tempReportedUser;
        String tempObject;
        String tempBody;

        try {
            tempCode = jsonObject.getInt("report_code");
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempCode = 0;
        }
        code = tempCode;

        try {
            tempAuthor = new User(jsonObject.getJSONObject("username"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempAuthor = new User();
        }
        author = tempAuthor;

        try {
            tempReportedUser = new User(jsonObject.getJSONObject("reported_user"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempReportedUser = new User();
        }
        reportedUser = tempReportedUser;

        try {
            tempObject = jsonObject.getString("object");
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempObject = "Error";
        }
        object = tempObject;

        try {
            tempBody = jsonObject.getString("report_body");
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            tempBody = "There was an error.";
        }
        body = tempBody;

        try {
            status = ReportStatus.getValue(jsonObject.getInt("state"));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
            status = ReportStatus.OPEN;
        }
    }

    public int getCode() {
        return code;
    }

    public User getAuthor() {
        return author;
    }

    public User getReportedUser() {
        return reportedUser;
    }

    public String getObject() {
        return object;
    }

    public String getBody() {
        return body;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", author.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("reported_user", reportedUser.toJSONObject());
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("report_code", code);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("object", object);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("report_body", body);
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }
        try {
            jsonObject.put("state", ReportStatus.toInt(status));
        } catch (JSONException e) {
            Log.e(ERROR_TAG, e.getMessage());
        }

        return jsonObject;
    }
}

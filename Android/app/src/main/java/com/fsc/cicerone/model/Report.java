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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

        public static final String REPORTED_USER_KEY = "reported_user";
        public static final String REPORT_CODE_KEY = "report_code";
        public static final String OBJECT_KEY = "object";
        public static final String BODY_KEY = "report_body";
        public static final String STATE_KEY = "state";
    }

    public Report(String json) {
        this(getMapFromJson(json));
    }

    public Report(Map<String, Object> jsonObject) {
        loadFromMap(jsonObject);
    }

    @Override
    protected void loadFromMap(Map<String, Object> jsonObject) {
        System.out.println(jsonObject);
        code = (int) jsonObject.get(Columns.REPORT_CODE_KEY);
        author = new User(jsonObject.get(User.Columns.USERNAME_KEY).toString());
        reportedUser = new User(jsonObject.get(Columns.REPORTED_USER_KEY).toString());
        object = (String) jsonObject.get(Columns.OBJECT_KEY);
        body = (String) jsonObject.get(Columns.BODY_KEY);
        status = ReportStatus.getValue((Integer) jsonObject.get(Columns.STATE_KEY));
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
    public Map<String, Object> toMap() {
        Map<String, Object> jsonObject = new HashMap<>();

        jsonObject.put(User.Columns.USERNAME_KEY, author.toString());
        jsonObject.put(Columns.REPORTED_USER_KEY, reportedUser.toString());
        jsonObject.put(Columns.REPORT_CODE_KEY, code);
        jsonObject.put(Columns.OBJECT_KEY, object);
        jsonObject.put(Columns.BODY_KEY, body);
        jsonObject.put(Columns.STATE_KEY, status.toInt());

        return jsonObject;
    }
}

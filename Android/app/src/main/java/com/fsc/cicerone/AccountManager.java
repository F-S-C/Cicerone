package com.fsc.cicerone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app_connector.ConnectorConstants;
import app_connector.DatabaseConnector;
import app_connector.SendInPostConnector;

public class AccountManager {

    public interface RunnableUsingJson {
        void run(JSONArray jsonArray) throws JSONException;
    }

    public void attemptLogin(JSONObject user, Runnable onStart, RunnableUsingJson onEnd) {
        SendInPostConnector connector = new SendInPostConnector(ConnectorConstants.LOGIN_CONNECTOR, new DatabaseConnector.CallbackInterface() {
            @Override
            public void onStartConnection() {
                onStart.run();
            }

            @Override
            public void onEndConnection(JSONArray jsonArray) throws JSONException {
                onEnd.run(jsonArray);
            }
        }, user);

        connector.execute();
    }
}
